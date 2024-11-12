package dti.crosemont.reservationvol.Entites


import java.time.LocalDateTime
import com.fasterxml.jackson.annotation.JsonProperty

data class VolStatut(
        val idVol : Int,
        val statut : String,
        val heure : LocalDateTime
)