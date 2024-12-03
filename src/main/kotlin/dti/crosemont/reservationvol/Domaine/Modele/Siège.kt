package dti.crosemont.reservationvol.Domaine.Modele

data class Siège(
    val id: Int,
    val numéroSiège: String,             
    val classe: String,
    val statut: String = "occupé"
)
