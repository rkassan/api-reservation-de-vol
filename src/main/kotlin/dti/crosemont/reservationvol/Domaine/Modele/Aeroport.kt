package dti.crosemont.reservationvol.Domaine.Modele

data class Aeroport(
        val id: Int,
        var code: String,
        var nom: String,
        var ville: Ville,
        var adresse: String,
)
