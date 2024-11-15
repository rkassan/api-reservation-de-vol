package dti.crosemont.reservationvol.Domaine.Modele


import java.time.LocalDateTime

data class VolStatut(
        val idVol : Int,
        val statut : String,
        val heure : LocalDateTime
)