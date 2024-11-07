package dti.crosemont.reservationvol.Entites

import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.time.Duration

data class Vol(
        val id: Int,
        val dateDepart: LocalDateTime,
        val dateArrivee: LocalDateTime,
        val avion: Avion,
        val poidsMaxBag: Int,
        val trajet: Trajet,
        val duree: LocalTime      
)
