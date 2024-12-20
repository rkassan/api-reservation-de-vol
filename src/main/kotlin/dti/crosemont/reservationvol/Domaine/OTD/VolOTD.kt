package dti.crosemont.reservationvol.Domaine.OTD

import dti.crosemont.reservationvol.Domaine.Modele.Avion
import dti.crosemont.reservationvol.Domaine.Modele.Siège
import dti.crosemont.reservationvol.Domaine.Modele.Trajet
import dti.crosemont.reservationvol.Domaine.Modele.VolStatut
import java.time.Duration
import java.time.LocalDateTime

class VolOTD(
        val id: Int,
        val dateDepart: LocalDateTime?,
        val dateArrivee: LocalDateTime?,
        val avion: Avion?,
        val prixParClasse: Map<String, Double>?,
        val poidsMaxBag: Int?,
        val trajet: Trajet?,
        val vol_statut: List<VolStatut>?,
        val duree: Duration?,

)