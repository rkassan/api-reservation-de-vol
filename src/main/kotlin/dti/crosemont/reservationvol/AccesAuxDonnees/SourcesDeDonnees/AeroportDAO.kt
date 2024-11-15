package dti.crosemont.reservationvol.AccesAuxDonnees.SourcesDeDonnees

import dti.crosemont.reservationvol.Domaine.Modele.Aeroport

interface AeroportDAO : DAO<Aeroport> {
    fun chercherParCode(code: String): Aeroport?
    fun chercherParNom(nom: String): List<Aeroport>
    fun modifier(aeroport: Aeroport): Aeroport
    fun ajouter(aeroport: Aeroport): Aeroport
}
