package dti.crosemont.reservationvol.Entites

import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.time.Duration


data class Vol(
        val id: Int,
        val dateDepart: LocalDateTime,
        val dateArrivee: LocalDateTime,
        val avion: Avion,
        val prixParClasse: Map<String, Double>,
        val poidsMaxBag: Int,
        val trajet: Trajet,
        val vol_statut: List<VolStatut>,
        val duree: Duration
)
