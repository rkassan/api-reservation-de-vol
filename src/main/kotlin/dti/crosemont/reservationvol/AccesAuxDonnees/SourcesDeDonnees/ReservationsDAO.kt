package dti.crosemont.reservationvol.AccesAuxDonnees.SourcesDeDonnees

import dti.crosemont.reservationvol.Domaine.Modele.Reservation

interface ReservationsDAO: DAO<Reservation>{
        //override fun chercherTous() : List<Reservation>
        fun ajouterReservation(reservation: Reservation): Reservation
        //fun chercherParId(id: Int): Reservation?
        override fun effacer(id: Int)   
}
