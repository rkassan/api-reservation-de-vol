package dti.crosemont.reservationvol.AccesAuxDonnees.SourcesDeDonnees

import dti.crosemont.reservationvol.Domaine.Modele.Client

interface ClientDAO : DAO<Client>{
    fun chercherParMotCle(motClé : String) : List<Client>
}