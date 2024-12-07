package dti.crosemont.reservationvol.Domaine.Service

import dti.crosemont.reservationvol.Domaine.Modele.Reservation
import dti.crosemont.reservationvol.AccesAuxDonnees.SourcesDeDonnees.ReservationsDAO 
import dti.crosemont.reservationvol.Domaine.Modele.Client
import dti.crosemont.reservationvol.Domaine.Modele.Siège
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.junit.jupiter.MockitoExtension
import kotlin.test.Test
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
class ReservationsServiceTest {

    lateinit var mockDAO : ReservationsDAO

    val listeReservations = listOf(
        Reservation(1, "AB123", 101, Client(1, "Jean", "Lee", "X123456", "127 rue", "jean.lee@example.com", "0123456789"), Siège(1, "A1", "économique"), "économique", 2),
        Reservation(2, "AB124", 102, Client(2, "Marie", "Suh", "B1223", "456 rue", "marie.suh@example.com", "1011121314"), Siège(2, "B2", "affaire"), "affaire", 1)
    )

    @Test
    fun `Étant donné un ReservationsService, lorsque la méthode obtenirToutesLesReservations est appelée, la liste des réservations est obtenue`() {
    
    Mockito.`when`( mockDAO.chercherTous() ).thenReturn( listeReservations )
        val cobaye = ReservationsService( mockDAO )

        val résultat_attendu = listeReservations
        val résultat_obtenue = cobaye.obtenirToutesLesReservations()

        assertEquals( résultat_attendu, résultat_obtenue )
    }

}
