package dti.crosemont.reservationvol.Domaine.Modele

data class Client(
    val id : Int,
    var nom : String,
    var prénom : String,
    var adresse : String,
    val numéroPasseport  : String,
    var email : String?,
    var numéroTéléphone : String?,
)
