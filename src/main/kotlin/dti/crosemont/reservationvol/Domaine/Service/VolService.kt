package dti.crosemont.reservationvol.Domaine.Service

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import dti.crosemont.reservationvol.AccesAuxDonnees.SourcesDeDonnees.VolsDAO
import dti.crosemont.reservationvol.Controleurs.Exceptions.RequêteMalFormuléeException
import org.springframework.stereotype.Service
import dti.crosemont.reservationvol.Domaine.Modele.Vol
import dti.crosemont.reservationvol.Domaine.Modele.`Siège`
import java.time.LocalDateTime
import dti.crosemont.reservationvol.Controleurs.Exceptions.RessourceInexistanteException
import dti.crosemont.reservationvol.Domaine.Modele.VolStatut
import dti.crosemont.reservationvol.Domaine.OTD.VolOTD
import java.time.temporal.ChronoUnit
import java.time.chrono.ChronoLocalDateTime
import org.springframework.scheduling.annotation.Scheduled

@Service
class VolService(private val volsDAO: VolsDAO) {

    fun obtenirVolParParam(dateDebut: LocalDateTime, aeroportDebut: String, aeroportFin: String): List<Vol> {
        val authentication = SecurityContextHolder.getContext().authentication
        val chronoLocalDateTime: ChronoLocalDateTime<*> = LocalDateTime.now()

        if(authentication.authorities.any { it.authority == "consulter:vols" }) {
            return volsDAO.obtenirVolParParam(dateDebut, aeroportDebut, aeroportFin)
        }
        else if(dateDebut.isAfter(chronoLocalDateTime)){
            return volsDAO.obtenirVolParParam(dateDebut, aeroportDebut, aeroportFin)
        }
        else {
            throw RequêteMalFormuléeException("La date d'aller $dateDebut ne peut pas être avant la date d'aujourd'hui $chronoLocalDateTime.")
        }
    }

    @PreAuthorize("hasAuthority('créer:vols')")
    fun ajouterVol(vol: Vol): Vol {
        val chronoLocalDateTime: ChronoLocalDateTime<*> = LocalDateTime.now()
        if (!volsDAO.trajetExiste(vol.trajet.id)) {
            throw RessourceInexistanteException("Le trajet avec l'ID ${vol.trajet.id} n'existe pas.")
        }
        
        if (!volsDAO.avionExiste(vol.avion.id)) {
            throw RessourceInexistanteException("L'avion avec l'ID ${vol.avion.id} n'existe pas.")
        }

        if (volsDAO.volExiste(vol)) {
            throw RequêteMalFormuléeException("Un vol avec les mêmes informations existe déjà.")
        }

        if (vol.dateArrivee.isBefore(vol.dateDepart)) {
        throw RequêteMalFormuléeException("La date d'arrivée (${vol.dateArrivee}) ne peut pas être avant la date de départ (${vol.dateDepart}).")      
        }

        if (vol.dateArrivee.isBefore(chronoLocalDateTime)) {
            throw RequêteMalFormuléeException("\"La date d'aller ${vol.dateDepart} ne peut pas être avant la date d'aujourd'hui $chronoLocalDateTime.")
        }

        val nouveauVol = volsDAO.ajouterVol(vol)

        val statuts = if (vol.vol_statut.isNullOrEmpty()) {
            listOf(VolStatut(idVol = nouveauVol.id, statut = "en attente", heure = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)))
        } else {
            vol.vol_statut.map { statut ->
                statut.copy(idVol = nouveauVol.id)
            }
        }

        statuts.forEach { statut ->
            volsDAO.ajouterStatutVol(nouveauVol.id, statut)
        }

        volsDAO.ajouterPrixParClasse(nouveauVol.id, vol.prixParClasse)

        return nouveauVol.copy(vol_statut = statuts)
    }
  @Scheduled(fixedRate = 60000)
  fun mettreAJourStatutsVolsDepart() {
      val volsAPartir = volsDAO.chercherVolsPourDepart(LocalDateTime.now())
      volsAPartir.forEach { vol ->
          val statut = VolStatut(vol.id, "depart", LocalDateTime.now())
          volsDAO.ajouterStatutVol(vol.id, statut)
      }
  }
    @Scheduled(fixedRate = 60000)
    fun mettreAJourStatutsVolsArrivee() {
        val volsAPartir = volsDAO.chercherVolsPourArrive(LocalDateTime.now())
        volsAPartir.forEach { vol ->
            val statut = VolStatut(vol.id, "arrivé", LocalDateTime.now())
            volsDAO.ajouterStatutVol(vol.id, statut)
        }
    }


  @PreAuthorize("hasAuthority('modifier:vols')")
  fun modifierVol(id: Int, modifieVol: VolOTD): Vol {

      val volExistant = chercherParId(id) ?: throw RessourceInexistanteException("Le vol avec l'ID $id n'existe pas.")


      modifieVol.apply {
          dateDepart?.let { volExistant.dateDepart = it }
          dateArrivee?.let { volExistant.dateArrivee = it }
          avion?.let {volExistant.avion= it}
          trajet?.let {volExistant.trajet= it}
          poidsMaxBag?.let { volExistant.poidsMaxBag = it }
          prixParClasse?.let { volExistant.prixParClasse = it }
          vol_statut?.let {volExistant.vol_statut = it }
          duree?.let { volExistant.duree = it }
      }

      if (modifieVol.trajet != null) {
          if (!volsDAO.trajetExiste(modifieVol.trajet.id)) {
              throw RessourceInexistanteException("Le trajet avec l'ID ${modifieVol.trajet.id} n'existe pas.")
          }
          volExistant.trajet = modifieVol.trajet
      }

      if (modifieVol.avion != null) {
          if (!volsDAO.avionExiste(modifieVol.avion.id)) {
              throw RessourceInexistanteException("L'avion avec l'ID ${modifieVol.avion.id} n'existe pas.")
          }
          volExistant.avion = modifieVol.avion
      }

      if (modifieVol.dateArrivee != null && modifieVol.dateDepart != null) {
          if (modifieVol.dateArrivee.isBefore(modifieVol.dateDepart)) {
              throw RequêteMalFormuléeException("La date d'arrivée (${modifieVol.dateArrivee}) ne peut pas être avant la date de départ (${modifieVol.dateDepart}).")
          }
      }


      if (modifieVol.vol_statut?.any { it.idVol != id } == true) {
          throw RequêteMalFormuléeException("Le statut fait référence à un ID de vol incorrect")
      }

      if (modifieVol.prixParClasse != null) {
          val prixActuels = volExistant.prixParClasse.toMutableMap()
          modifieVol.prixParClasse.forEach { (classe, prix) -> prixActuels[classe] = prix }

          val economique = prixActuels["économique"] ?: throw RequêteMalFormuléeException("Le prix pour la classe économique est requis.")
          val affaire = prixActuels["affaire"] ?: throw RequêteMalFormuléeException("Le prix pour la classe affaire est requis.")
          val premiere = prixActuels["première"] ?: throw RequêteMalFormuléeException("Le prix pour la classe première est requis.")

          if (affaire - economique < 200) {
              throw RequêteMalFormuléeException("La différence entre les prix des classes économique et affaire doit être d'au moins 200$.")
          }
          if (premiere - economique < 500) {
              throw RequêteMalFormuléeException("La différence entre les prix des classes économique et première doit être d'au moins 500$.")
          }
          if (premiere - affaire < 300) {
              throw RequêteMalFormuléeException("La différence entre les prix des classes affaire et première doit être d'au moins 300$.")
          }

          volExistant.prixParClasse = prixActuels
      }
      return volsDAO.modifierVol(id, volExistant)
  }

  @PreAuthorize("hasAuthority('consulter:vols')")
  fun chercherTous(): List<Vol> = volsDAO.chercherTous()

  fun chercherParId(id: Int): Vol? {
      val vol = volsDAO.chercherParId(id)

      if(vol == null){
          throw RessourceInexistanteException("Le vol $id n'existe pas.")
      }
      return vol;
  }

  fun effacer(id: Int) {
      val vol = volsDAO.chercherParId(id)

      if(vol == null){
          throw RessourceInexistanteException("Le vol $id n'existe pas.")
      }
      volsDAO.effacer(id)
  }

  fun chercherSiegeParVolId(id: Int): List<Siège> {
      val vol = volsDAO.chercherParId(id)

      if(vol == null){
          throw RessourceInexistanteException("Le vol $id n'existe pas.")
      }
      return volsDAO.obtenirSiegeParVolId(id)
  }

}