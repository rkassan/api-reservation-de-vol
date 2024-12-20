package dti.crosemont.reservationvol.AccesAuxDonnees.SourcesDeDonnees

import dti.crosemont.reservationvol.Domaine.Modele.Client

interface ClientDAO : DAO<Client>{
    fun chercherParMotCle(motClé : String) : List<Client>
    fun obtenirParEmail(email : String) : Client?
    fun ajouter( client : Client ) : Client?
    fun modifier( client : Client ) : Client?
}