package dti.crosemont.reservationvol.Domaine.Modele

data class Reservation (
        val id:Int,
        var numéroRéservation: String,
        var idVol: Int,
        var client: Client, 
        var siège: Siège,
        var classe: String,
        var bagages: Int
    )