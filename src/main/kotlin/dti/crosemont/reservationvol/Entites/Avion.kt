package dti.crosemont.reservationvol.Entites

data class Avion (
    val id: Int,
    val type: String,
    val sièges: List<Siege>,
    val numéroVol: String
)