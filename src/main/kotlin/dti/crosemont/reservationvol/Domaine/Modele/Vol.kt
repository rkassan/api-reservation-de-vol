package dti.crosemont.reservationvol.Domaine.Modele

import java.time.LocalDateTime
import java.time.Duration


data class Vol(
        val id: Int,
        var dateDepart: LocalDateTime,
        var dateArrivee: LocalDateTime,
        var avion: Avion,
        var prixParClasse: Map<String, Double>,
        var poidsMaxBag: Int,
        var trajet: Trajet,
        var vol_statut: List<VolStatut>,
        var duree: Duration,
        var sièges: List<Siège>?
)

