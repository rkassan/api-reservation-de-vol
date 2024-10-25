package dti.crosemont.reservationvol.Entites

data class Reservation (
    val numéro_réservation: String,
    val numéro_vol: String,
    val classe: Classe,
    val siège_selectionné: Int
)