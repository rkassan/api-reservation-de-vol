package dti.crosemont.reservationvol.Domaine.Modele

data class Reservation (
        val id:Int,
        val numéroRéservation: String,
        val idVol: Int,
        val client: Client, //changment pour 1 client et pas un liste de clients
        val siège: Siège,
        val classe: String,
        //val siegeSelectionne: String,
        val bagages: Int
    )