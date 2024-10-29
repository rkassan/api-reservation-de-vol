package dti.crosemont.reservationvol.Entites

import java.time.LocalDateTime
import java.time.LocalTime

data class Vol(
        val numeroVol: String,
        val aeroportDebut: Aeroport,
        val aeroportFin: Aeroport,
        val dateDepart: LocalDateTime,
        val dateArrivee: LocalDateTime,
        val avion: Avion,
        val prixParClasse: Map<String, Double>,
        val poidsMaxBag: Int,
        val statutVol: List<String>,
        val duree: LocalTime
        
)
