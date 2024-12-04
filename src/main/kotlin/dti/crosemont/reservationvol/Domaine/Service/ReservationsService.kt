package dti.crosemont.reservationvol

import org.springframework.stereotype.Service
import dti.crosemont.reservationvol.Domaine.Modele.Reservation
import dti.crosemont.reservationvol.Domaine.Modele.Siège
import dti.crosemont.reservationvol.Domaine.Modele.Vol
import dti.crosemont.reservationvol.Domaine.Modele.Client 
import dti.crosemont.reservationvol.AccesAuxDonnees.BD.ReservationsDAOImpl
import dti.crosemont.reservationvol.AccesAuxDonnees.SourcesDeDonnees.SiègeDAO
import dti.crosemont.reservationvol.Domaine.Service.VolService
import dti.crosemont.reservationvol.Controleurs.Exceptions.RequêteMalFormuléeException
import dti.crosemont.reservationvol.Controleurs.Exceptions.RessourceInexistanteException
import dti.crosemont.reservationvol.Controleurs.Exceptions.RéservationInexistanteException


@Service
class ReservationsService(  private val reservationsDAO: ReservationsDAOImpl,
                            private val siegeDAO: SiègeDAO, 
                            private val volService: VolService) {


    fun obtenirToutesLesReservations(): List<Reservation> = reservationsDAO.chercherTous()

    // --------------------changer ici pour associe la reservation avec le vol, client  et siege--------------------------------
    fun ajouterReservation(reservation: Reservation): Reservation {
        // Vérification si le vol existe
        val vol = volService.chercherParId(reservation.idVol)
            ?: throw RessourceInexistanteException("Vol avec l'ID ${reservation.idVol} introuvable.")
    
        // ici c'est pour avoir les sièges du vol
        val sièges = volService.chercherSiegeParVolId(reservation.idVol)

        // Vérifier si le siège sélectionné existe dans la liste des sièges disponibles
        val siègeSélectionné = sièges.find { it.numéroSiège == reservation.siège.numéroSiège }
            ?: throw RequêteMalFormuléeException("Le siège ${reservation.siège.numéroSiège} n'est pas disponible pour ce vol.")

        // Vérification si le siège est occupé
        if (siègeSélectionné.statut == "occupé") {
            throw RequêteMalFormuléeException("Le siège ${reservation.siège.numéroSiège} est déjà réservé.")
        }
    
        
        val nouvelleRéservation = reservationsDAO.ajouterReservation(reservation)

        // Mise à jour du statut du siège en "occupé"
        siègeSélectionné.statut = "occupé"
        siegeDAO.save(siègeSélectionné)

        return nouvelleRéservation
    }




    fun obtenirReservationParId(id: Int): Reservation? {
        return reservationsDAO.chercherParId(id)
    }
    
    fun modifierRéservation( id: Int, réservation: Reservation ): Reservation {
        val réservationVérification = this.obtenirReservationParId(id)
        
        if ( réservationVérification != null ) {
            return reservationsDAO.modifierRéservation(id, réservation)
        } else {
            throw RéservationInexistanteException("Réservation avec le id: $id est inexistante")
        }
    }

    fun supprimerRéservation(id: Int) {
        val réservation = this.obtenirReservationParId(id)
        
        if ( réservation != null ) {
            reservationsDAO.effacer(id)
        } else {
            throw RéservationInexistanteException("Réservation avec le id: $id est inexistante")
        }
    }
}