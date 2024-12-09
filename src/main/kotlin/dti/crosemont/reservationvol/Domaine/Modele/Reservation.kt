package dti.crosemont.reservationvol.Domaine.Modele

data class Reservation (
        val id:Int,
        var numéroRéservation: String,
        var idVol: Int,
        var client: Client, //changment pour 1 client et pas un liste de clients
        var siège: Siège,
        var classe: String,
        //val siegeSelectionne: String,
        var bagages: Int
    )