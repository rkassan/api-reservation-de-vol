package dti.crosemont.reservationvol

import dti.crosemont.reservationvol.Entites.Vol
import java.time.LocalDateTime

interface VolsDAO: DAO<Vol>{
    override fun chercherTous() : List<Vol>
    override fun chercherParId(id: Int): Vol?
    override fun effacer(id: Int)
    fun obtenirVolParParam(dateDebut: LocalDateTime, aeroportDebut: String, aeroportFin: String): List<Vol>
}
