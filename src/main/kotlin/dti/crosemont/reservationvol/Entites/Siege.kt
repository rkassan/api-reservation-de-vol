package dti.crosemont.reservationvol.Entites

data class Siege(
    val numéro: String,             
    val classe: String,            
    val statut: String,             
    val avion_id: Int,            
    val numéroRéservation: String
)
