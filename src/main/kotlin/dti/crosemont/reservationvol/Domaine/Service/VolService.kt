package dti.crosemont.reservationvol.Domaine.Service

import dti.crosemont.reservationvol.AccesAuxDonnees.SourcesDeDonnees.VolsDAO
import dti.crosemont.reservationvol.Controleurs.Exceptions.RequêteMalFormuléeException
import org.springframework.stereotype.Service
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import dti.crosemont.reservationvol.Domaine.Modele.Vol
import dti.crosemont.reservationvol.Domaine.Modele.`Siège`
import org.springframework.http.HttpMethod
import org.springframework.web.servlet.resource.NoResourceFoundException
import java.time.LocalDateTime
import kotlin.math.exp

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
        val trajetOriginal = volExistant.trajet
        val avionOriginal = volExistant.avion
        
        if (modifieVol.trajet != trajetOriginal.copy(id = modifieVol.trajet.id) || 
            modifieVol.avion != avionOriginal.copy(id = modifieVol.avion.id)) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }
        if (modifieVol.vol_statut.any { it.idVol != id }) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        val volMisAJour = volsDAO.modifierVol(id, modifieVol)
        return ResponseEntity(volMisAJour, HttpStatus.OK)
    }
    
    fun chercherTous(): List<Vol> = volsDAO.chercherTous()

    fun chercherParId(id: Int): Vol? {

        val vol = volsDAO.chercherParId(id)

        if(vol == null){
            throw NoResourceFoundException(HttpMethod.GET, "vols/$id")
        }
        return vol;
    }

    fun effacer(id: Int) {
        val vol = volsDAO.chercherParId(id)

        if(vol == null){
            throw NoResourceFoundException(HttpMethod.GET, "vols/$id")
        }
        volsDAO.effacer(id)
    }

    fun chercherSiegeParVolId(id: Int): List<Siège> {
        val vol = volsDAO.chercherParId(id)

        if(vol == null){
            throw NoResourceFoundException(HttpMethod.GET, "vols/$id")
        }
        return volsDAO.obtenirSiegeParVolId(id)
    }
}