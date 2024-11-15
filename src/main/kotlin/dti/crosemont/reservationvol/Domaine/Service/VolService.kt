package dti.crosemont.reservationvol.Domaine.Service

import dti.crosemont.reservationvol.AccesAuxDonnees.SourcesDeDonnees.VolsDAO
import org.springframework.stereotype.Service
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import org.springframework.web.servlet.resource.NoResourceFoundException
import dti.crosemont.reservationvol.Domaine.Modele.Vol
import dti.crosemont.reservationvol.Domaine.Modele.`Siège`
import java.time.LocalDateTime
import dti.crosemont.reservationvol.Controleurs.Exceptions.RequêteMalFormuléeException
import dti.crosemont.reservationvol.Controleurs.Exceptions.RessourceInexistanteException

@Service
class VolService(private val volsDAO: VolsDAO) {

    fun obtenirVolParParam(dateDebut: LocalDateTime, aeroportDebut: String, aeroportFin: String): List<Vol> {
        return volsDAO.obtenirVolParParam(dateDebut, aeroportDebut, aeroportFin)
    }

    fun ajouterVol(vol: Vol): Vol {
        if (!volsDAO.trajetExiste(vol.trajet.id)) {
            throw RessourceInexistanteException("Le trajet avec l'ID ${vol.trajet.id} n'existe pas.")
        }
        
        if (!volsDAO.avionExiste(vol.avion.id)) {
            throw RessourceInexistanteException("L'avion avec l'ID ${vol.avion.id} n'existe pas.")
        }
        
    
        val nouveauVol = volsDAO.ajouterVol(vol)
    
        val statutsMisAJour = vol.vol_statut.map { statut ->
            statut.copy(idVol = nouveauVol.id)
        }
        
        statutsMisAJour.forEach { statut -> volsDAO.ajouterStatutVol(nouveauVol.id, statut) }
        volsDAO.ajouterPrixParClasse(nouveauVol.id, vol.prixParClasse)
    
        return nouveauVol.copy(vol_statut = statutsMisAJour)
    }

    fun modifierVol(id: Int, modifieVol: Vol): Vol {
        val volExistant = volsDAO.chercherParId(id) ?: throw RessourceInexistanteException("Le vol avec l'ID $id n'existe pas.")
        
        if (!volsDAO.trajetExiste(modifieVol.trajet.id)) {
            throw RessourceInexistanteException("Le trajet avec l'ID ${modifieVol.trajet.id} n'existe pas.")
        }
        if (!volsDAO.avionExiste(modifieVol.avion.id)) {
            throw RessourceInexistanteException("L'avion avec l'ID ${modifieVol.avion.id} n'existe pas.")
        }
        
        val trajetOriginal = volExistant.trajet
        val avionOriginal = volExistant.avion
        
        if (modifieVol.trajet != trajetOriginal.copy(id = modifieVol.trajet.id) || 
            modifieVol.avion != avionOriginal.copy(id = modifieVol.avion.id)) {
            throw RequêteMalFormuléeException("Modification du trajet ou de l'avion n'est pas autorisée au-delà de la mise à jour de l'ID")
        }
        if (modifieVol.vol_statut.any { it.idVol != id }) {
            throw RequêteMalFormuléeException("Le statut fait référence à un ID de vol incorrect")
        }
        return volsDAO.modifierVol(id, modifieVol)
    }
    
    fun chercherTous(): List<Vol> = volsDAO.chercherTous()

    fun chercherParId(id: Int): Vol? = volsDAO.chercherParId(id)

    fun effacer(id: Int) = volsDAO.effacer(id)

    fun chercherSiegeParVolId(id: Int): List<Siège> = volsDAO.obtenirSiegeParVolId(id)
}