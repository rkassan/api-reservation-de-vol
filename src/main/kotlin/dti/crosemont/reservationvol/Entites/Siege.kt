package dti.crosemont.reservationvol.Entites

data class Siège(
    val numéro: String,             
    val classe: String,            
    val statut: String,             
    val avion_id: Int,            
    val numéroRéservation: String
)
