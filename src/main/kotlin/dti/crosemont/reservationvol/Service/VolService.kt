package dti.crosemont.reservationvol.Service

import org.springframework.stereotype.Service
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import dti.crosemont.reservationvol.VolsDAO
import dti.crosemont.reservationvol.Entites.Vol
import java.time.LocalDateTime

@Service
class VolService(private val volsDAO: VolsDAO) {

    fun obtenirVolParParam(dateDebut: LocalDateTime, aeroportDebut: String, aeroportFin: String): List<Vol> {
        return volsDAO.obtenirVolParParam(dateDebut, aeroportDebut, aeroportFin)
    }

    fun ajouterVol(vol: Vol): ResponseEntity<Vol> {
        if (!volsDAO.trajetExiste(vol.trajet.id)) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
        
        if (!volsDAO.avionExiste(vol.avion.id)) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
        
    
        val nouveauVol = volsDAO.ajouterVol(vol)
    
        val statutsMisAJour = vol.vol_statut.map { statut ->
            statut.copy(idVol = nouveauVol.id)
        }
        
        statutsMisAJour.forEach { statut -> volsDAO.ajouterStatutVol(nouveauVol.id, statut) }
        volsDAO.ajouterPrixParClasse(nouveauVol.id, vol.prixParClasse)
    
        return ResponseEntity(nouveauVol.copy(vol_statut = statutsMisAJour), HttpStatus.CREATED)
    }

    fun modifierVol(id: Int, modifieVol: Vol): ResponseEntity<Vol> {
        val volExistant = volsDAO.chercherParId(id)
        if (volExistant == null) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }

        if (!volsDAO.trajetExiste(modifieVol.trajet.id)) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
        if (!volsDAO.avionExiste(modifieVol.avion.id)) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }

        val volMisAJour = volsDAO.modifierVol(id, modifieVol)
        return ResponseEntity(volMisAJour, HttpStatus.OK)
    }

    
   
    
    fun chercherTous(): List<Vol> = volsDAO.chercherTous()

    fun chercherParId(id: Int): Vol? = volsDAO.chercherParId(id)

    fun effacer(id: Int) = volsDAO.effacer(id)
}