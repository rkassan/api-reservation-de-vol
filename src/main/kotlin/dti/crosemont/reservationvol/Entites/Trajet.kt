package dti.crosemont.reservationvol.Entites

import com.fasterxml.jackson.annotation.JsonProperty

data class Trajet(
        val id: Int,
        @JsonProperty("numéroTrajet") val numéroTrajet: String,
        val aéroportDébut: Aeroport,
        val aéroportFin: Aeroport
)
