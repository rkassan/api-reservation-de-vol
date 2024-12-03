package dti.crosemont.reservationvol.AccesAuxDonnees.SourcesDeDonnees

import dti.crosemont.reservationvol.Domaine.Modele.Reservation

interface ReservationsDAO: DAO<Reservation>{
        fun ajouterReservation(reservation: Reservation): Reservation
        fun modifierRéservation(id: Int, réservation: Reservation): Reservation
        override fun effacer(id: Int)   
}
