package dti.crosemont.reservationvol

import org.springframework.stereotype.Service
import dti.crosemont.reservationvol.Domaine.Modele.Reservation
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
import dti.crosemont.reservationvol.Controleurs.Exceptions.ModificationException
import kotlin.enums.enumEntries
import org.springframework.http.ResponseEntity



@Service
class ReservationsService(private val reservationsDAO: ReservationsDAO,
                          private val siegeDAO: SiègeDAO,
                          private val volService: VolService,
                          private val clientService: ClientsService) {

    
    val typeClasse = arrayListOf<String>("économique","business","première")

    @PreAuthorize("hasAnyAuthority('consulter:réservations')")
    fun obtenirToutesLesReservations(): List<Reservation> = reservationsDAO.chercherTous()
    
    @PreAuthorize("hasAnyAuthority('créer:réservations')")
    fun ajouterReservation(reservationOTD: PostReservationOTD): Reservation {
        // Vérification de l'email du client
        val emailClient = reservationOTD.clientEmail
            ?: throw RequêteMalFormuléeException("L'email du client est requis.")

        // Récupérer le client via l'email
        val client = clientService.obtenirClientParEmail(emailClient)
            ?: throw RessourceInexistanteException("Client avec l'email $emailClient introuvable.")

        val idVol = reservationOTD.idVol ?: throw RequêteMalFormuléeException("L'ID du vol est requis.")

        val vol = volService.chercherParId(idVol)
            ?: throw RessourceInexistanteException("Vol avec l'ID $idVol introuvable.")

        val siègeSélectionné = volService.chercherSiegeParVolId(idVol)
            .find { it.numéroSiège == reservationOTD.siège?.numéroSiège }
            ?: throw RequêteMalFormuléeException("Le siège ${reservationOTD.siège?.numéroSiège} n'est pas disponible.")

        if (siègeSélectionné.statut == "occupé") {
            throw RequêteMalFormuléeException("Le siège ${reservationOTD.siège?.numéroSiège} est déjà réservé.")
        }

        val classe = reservationOTD.classe ?: throw RequêteMalFormuléeException("La classe est requise.")
        val bagages = reservationOTD.bagages ?: 0

        val numéroRéservation = reservationOTD.numéroRéservation
            ?: throw RequêteMalFormuléeException("Le numéro de réservation est requis.")

        val reservation = Reservation(
            id = 0, 
            client = client,
            idVol = idVol,
            siège = siègeSélectionné,
            classe = classe,
            bagages = bagages,
            numéroRéservation = numéroRéservation
        )

        val nouvelleRéservation = reservationsDAO.ajouterReservation(reservation)

        siègeSélectionné.statut = "occupé"
        siegeDAO.save(siègeSélectionné)

        return nouvelleRéservation
    }

    @PreAuthorize("hasAnyAuthority('consulter:réservations')")
    fun obtenirReservationParId(id: Int): Reservation {

        val réservationObtenue = reservationsDAO.chercherParId(id)

        if ( réservationObtenue != null ) { 
            return réservationObtenue
        } else {
            throw RéservationInexistanteException("Réservation avec le id: $id est inexistante")
        }
    }
    
    @PreAuthorize("hasAnyAuthority('modifier:réservations')")
    fun modifierRéservation( id: Int, réservationOTD: ReservationOTD ): Reservation {
        
        val réservation = this.obtenirReservationParId(id)

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
        this.obtenirReservationParId(id)
        
        reservationsDAO.effacer(id)
    }

    private fun vérifierParamètre( réservationOTD: ReservationOTD ) {
        if ( !(réservationOTD.classe != null && typeClasse.contains( réservationOTD.classe ) ) ) {
            throw ModificationException("Classe saisit non valide.")
        }         
    }
}