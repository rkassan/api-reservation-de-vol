package dti.crosemont.reservationvol

import org.springframework.stereotype.Service
import dti.crosemont.reservationvol.Domaine.Modele.Reservation
import dti.crosemont.reservationvol.AccesAuxDonnees.BD.ReservationsDAOImpl
import dti.crosemont.reservationvol.Controleurs.Exceptions.RéservationInexistanteException

@Service
class ReservationsService(private val reservationsDAO: ReservationsDAOImpl) {

    fun obtenirToutesLesReservations(): List<Reservation> = reservationsDAO.chercherTous()

    fun ajouterReservation(reservation: Reservation): Reservation {
        return reservationsDAO.ajouterReservation(reservation)
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