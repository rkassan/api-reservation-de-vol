package dti.crosemont.reservationvol.Domaine.OTD
import dti.crosemont.reservationvol.Domaine.Modele.Client
import dti.crosemont.reservationvol.Domaine.Modele.Siège

class ReservationOTD (
    val idVol: Int?,
    val client: Client?,
    val siège: Siège?,
    val classe: String?,
    val bagages: Int?
)