package dti.crosemont.reservationvol.Domaine.Modele

import java.time.LocalDateTime
import java.time.Duration


data class Vol(
        val id: Int,
        val dateDepart: LocalDateTime,
        val dateArrivee: LocalDateTime,
        val avion: Avion,
        val prixParClasse: Map<String, Double>,
        val poidsMaxBag: Int,
        val trajet: Trajet,
        val vol_statut: List<VolStatut>,
        val duree: Duration,
        var sièges: List<Siège>?
)

