package dti.crosemont.reservationvol.Entites

import java.time.LocalTime

data class VolStatut(
        val idVol : Int,
        val Statut : String,
        val heure : LocalTime
)