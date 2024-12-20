package dti.crosemont.reservationvol.Domaine.OTD
import dti.crosemont.reservationvol.Domaine.Modele.Client
import dti.crosemont.reservationvol.Domaine.Modele.Siège

class PostReservationOTD (
    val idVol: Int,
    val clientCourriel: String,
    val siège: Siège,
    val classe: String,
    val bagages: Int,
)