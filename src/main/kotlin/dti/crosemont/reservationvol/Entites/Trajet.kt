package dti.crosemont.reservationvol.Entites

data class Trajet(
   val id: Int,
   val  numéroTrajet: String,
   val aéroportDébut: Aeroport,
   val aéroportFin: Aeroport
)