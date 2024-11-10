package dti.crosemont.reservationvol

import org.springframework.stereotype.Service
import dti.crosemont.reservationvol.Entites.Reservation
import dti.crosemont.reservationvol.ReservationsDAOImpl


@Service
class ReservationsService(private val reservationsDAO: ReservationsDAOImpl) {

    fun obtenirToutesLesReservations(): List<Reservation> = reservationsDAO.chercherTous()

    fun ajouterReservation(reservation: Reservation): Reservation {
        return reservationsDAO.ajouterReservation(reservation)
    }

}