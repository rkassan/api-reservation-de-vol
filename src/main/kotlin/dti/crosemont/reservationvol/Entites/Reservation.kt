package dti.crosemont.reservationvol.Entites

data class Reservation(
        val numéroRéservation: String,
        val numeroVol: String,
        val clients: List<Client>,
        val sièges: List<Siège>,
        val classe: String,
        val siegeSelectionne: String,
        val bagages: Int
)
