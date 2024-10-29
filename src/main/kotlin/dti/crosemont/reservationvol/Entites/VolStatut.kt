package dti.crosemont.reservationvol.Entites

import java.time.LocalTime

data class VolStatut(
        val numéroVol : String,
        val Statut : String,
        val heure : LocalTime
)