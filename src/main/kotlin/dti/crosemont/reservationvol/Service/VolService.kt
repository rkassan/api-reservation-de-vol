package dti.crosemont.reservationvol.Service

import org.springframework.stereotype.Service
import dti.crosemont.reservationvol.VolsDAO
import dti.crosemont.reservationvol.Entites.Vol
import java.time.LocalDateTime

@Service
class VolService(private val volsDAO: VolsDAO) {

    fun obtenirVolParParam(dateDebut: LocalDateTime, aeroportDebut: String, aeroportFin: String): List<Vol> {
        return volsDAO.obtenirVolParParam(dateDebut, aeroportDebut, aeroportFin)
    }
    
    fun chercherTous(): List<Vol> = volsDAO.chercherTous()

    fun chercherParId(id: Int): Vol? = volsDAO.chercherParId(id)

    fun effacer(id: Int) = volsDAO.effacer(id)

}