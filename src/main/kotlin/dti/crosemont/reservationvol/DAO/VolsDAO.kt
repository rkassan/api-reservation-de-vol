package dti.crosemont.reservationvol

import dti.crosemont.reservationvol.Entites.Vol
import dti.crosemont.reservationvol.Entites.VolStatut


import java.time.LocalDateTime

interface VolsDAO: DAO<Vol>{
    override fun chercherTous() : List<Vol>
    override fun chercherParId(id: Int): Vol?
    override fun effacer(id: Int)
    fun obtenirVolParParam(dateDebut: LocalDateTime, aeroportDebut: String, aeroportFin: String): List<Vol>
    fun ajouterVol(vol: Vol): Vol
    fun ajouterStatutVol(volId: Int, statut: VolStatut)
    fun ajouterPrixParClasse(volId: Int, prixParClasse: Map<String, Double>)
    fun trajetExiste(id: Int): Boolean
    fun avionExiste(id: Int): Boolean
}
