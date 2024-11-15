package dti.crosemont.reservationvol.Domaine.Modele

data class Aeroport(
        val id: Int,
        val code: String,
        val nom: String,
        val ville: Ville,
        val adresse: String
)
