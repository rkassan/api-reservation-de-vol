package dti.crosemont.reservationvol.Entites

data class Reservation (
    val id:Int,
    val numéroRéservation: String,
    val idVol: Int,
    val clients: List<Client>,
    val sièges: List<Siège>,
    val classe: String,
    val siegeSelectionne: String,
    val bagages: Int
    )