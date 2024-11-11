package dti.crosemont.reservationvol

import dti.crosemont.reservationvol.Entites.Client
import dti.crosemont.reservationvol.DAO

interface ClientDAO : DAO<Client>{
    fun chercherParMotCle(motClé : String) : List<Client>
}