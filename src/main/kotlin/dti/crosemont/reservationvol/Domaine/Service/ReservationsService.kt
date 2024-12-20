package dti.crosemont.reservationvol

import org.springframework.stereotype.Service
import dti.crosemont.reservationvol.Domaine.Modele.Reservation
import dti.crosemont.reservationvol.Domaine.Modele.Client
import dti.crosemont.reservationvol.Domaine.Modele.Siège
import dti.crosemont.reservationvol.AccesAuxDonnees.SourcesDeDonnees.ReservationsDAO
import dti.crosemont.reservationvol.AccesAuxDonnees.SourcesDeDonnees.SiègeDAO
import dti.crosemont.reservationvol.Domaine.Service.VolService
import dti.crosemont.reservationvol.Domaine.Service.ClientsService
import dti.crosemont.reservationvol.Domaine.OTD.ReservationOTD
import dti.crosemont.reservationvol.Domaine.OTD.PostReservationOTD
import dti.crosemont.reservationvol.Controleurs.Exceptions.RequêteMalFormuléeException
import dti.crosemont.reservationvol.Controleurs.Exceptions.RessourceInexistanteException
import dti.crosemont.reservationvol.Controleurs.Exceptions.RéservationInexistanteException
import org.springframework.security.access.prepost.PostAuthorize
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.oauth2.jwt.Jwt
import dti.crosemont.reservationvol.Controleurs.Exceptions.ModificationException
import dti.crosemont.reservationvol.Controleurs.Exceptions.AccèsRefuséException
import dti.crosemont.reservationvol.Controleurs.Exceptions.NombreDeBagageInvalide
import kotlin.enums.enumEntries
import org.springframework.http.ResponseEntity



@Service
class ReservationsService(private val reservationsDAO: ReservationsDAO,
                          private val siegeDAO: SiègeDAO,
                          private val volService: VolService,
                          private val clientService: ClientsService) {

    
    val typeClasse = arrayListOf<String>("économique","affaire","première")
    val statutSiège = arrayListOf<String>("disponible","occupé")

    fun obtenirToutesLesReservations(listePermissions: List<String>?, courrielAuthentification: String): List<Reservation> {

        if ( listePermissions != null ) {
            if ( listePermissions.contains("consulter:réservations")) {
                return reservationsDAO.chercherTous()
            }
        }
   
        val client = clientService.obtenirClientParEmail(courrielAuthentification)
        return reservationsDAO.chercherTous(client.id)
    }
    
    fun ajouterReservation(réservationOTD: PostReservationOTD): Reservation {
    
        val client = clientService.obtenirClientParEmail(réservationOTD.clientCourriel) 

        val siègeSélectionné = volService.chercherSiegeParVolId(réservationOTD.idVol)
            .find { it.numéroSiège == réservationOTD.siège.numéroSiège && it.classe == réservationOTD.classe }
            ?: throw RequêteMalFormuléeException("Le siège ${réservationOTD.siège.numéroSiège} n'est pas disponible.")


        volService.chercherParId(réservationOTD.idVol)
        
        if (siègeSélectionné.statut == statutSiège[1]) {
            throw RequêteMalFormuléeException("Le siège ${réservationOTD.siège.numéroSiège} est déjà réservé.")
        }
        
        if (réservationOTD.bagages < 0 ) throw NombreDeBagageInvalide("Le nombre de bagage doit être un numéro supérieur ou égal à 0.")

        val réservation = Reservation(
            id = 0, 
            client = client,
            idVol = réservationOTD.idVol, 
            siège = siègeSélectionné,
            classe = réservationOTD.classe,
            bagages = réservationOTD.bagages,
            numéroRéservation = generateNuméroRéservation()
        )

        réservation.siège.statut = "occupé"
        siegeDAO.sauvegarder(siègeSélectionné)
        return reservationsDAO.ajouterRéservation(réservation)

    }

    fun obtenirReservationParId(id: Int, courrielAuthentification: String, listePermissions: List<String>?): Reservation {

    if (listePermissions != null && listePermissions.contains("consulter:réservations")) {
        val réservationObtenue = reservationsDAO.chercherParId(id) 
            ?: throw RéservationInexistanteException("Réservation avec le id: $id est inexistante")
        
        if (réservationObtenue.client.email == courrielAuthentification) {
            return réservationObtenue
        } else {
            throw RéservationInexistanteException("Cette réservation n'est pas à vous.")
            }
        }
        throw RéservationInexistanteException("Permissions insuffisantes pour consulter la réservation.")
    }
    
    
    @PreAuthorize("hasAnyAuthority('modifier:réservations')")
    fun modifierRéservation( id: Int, réservationOTD: ReservationOTD): Reservation {
        
        val réservation = reservationsDAO.chercherParId(id) ?: throw RéservationInexistanteException("Réservation avec le id: $id est inexistante")

        this.vérifierParamètre(réservationOTD)

        réservationOTD.apply {
            idVol?.let { réservation.idVol = it }
            client?.let { réservation.client = it }
            siège?.let { réservation.siège = it }
            classe?.let { réservation.classe = it }
            bagages?.let { réservation.bagages = it }
        }
        
        return reservationsDAO.modifierRéservation(id, réservation)
    }

    @PreAuthorize("hasAnyAuthority('supprimer:réservations')")
    fun supprimerRéservation(id: Int) {
        val réservation = reservationsDAO.chercherParId(id) ?: throw RéservationInexistanteException("Réservation avec le id: $id est inexistante")

        this.modifierSiègeVol( réservation )
        
        reservationsDAO.effacer(id)
    }

    private fun vérifierParamètre( réservationOTD: ReservationOTD ) {
        if ( !(réservationOTD.classe != null && typeClasse.contains( réservationOTD.classe ) ) ) {
            throw ModificationException("Classe saisit non valide.")
        }         
    }

    private fun modifierSiègeVol( réservation: Reservation ) {
        
        réservation.siège.statut = statutSiège[0]
        reservationsDAO.modifierSiègeVol( réservation )

    }

    private fun generateNuméroRéservation(): String {
        return "R" + java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 10).uppercase()
    }

}