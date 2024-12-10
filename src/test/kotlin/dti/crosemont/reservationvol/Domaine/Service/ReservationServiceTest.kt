package dti.crosemont.reservationvol.Domaine.Service

import dti.crosemont.reservationvol.Domaine.Modele.Reservation
import dti.crosemont.reservationvol.AccesAuxDonnees.SourcesDeDonnees.ReservationsDAO
import dti.crosemont.reservationvol.AccesAuxDonnees.SourcesDeDonnees.SiègeDAO
import dti.crosemont.reservationvol.Controleurs.Exceptions.RessourceInexistanteException
import dti.crosemont.reservationvol.Controleurs.Exceptions.RéservationInexistanteException
import dti.crosemont.reservationvol.Domaine.Modele.Client
import dti.crosemont.reservationvol.Domaine.Modele.Siège
import dti.crosemont.reservationvol.ReservationsService
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import kotlin.test.Test
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
class ReservationsServiceTest {

    @Mock
    lateinit var mockDAO: ReservationsDAO

    @Mock
    lateinit var mockDAOSiege: SiègeDAO

    @Mock
    lateinit var mockDAOVols: VolService

    private val listeReservations = listOf(
            Reservation(1, "AB123", 101, Client(1, "Jean", "Lee", "X123456", "127 rue", "jean.lee@example.com", "0123456789"), Siège(1, "A1", "économique"), "économique", 2),
            Reservation(2, "AB124", 102, Client(2, "Marie", "Suh", "B1223", "456 rue", "marie.suh@example.com", "1011121314"), Siège(2, "B2", "affaire"), "affaire", 1)
    )


    @Test
    fun `Étant donné un ReservationsService, lorsque la méthode obtenirToutesLesReservations est appelée, la liste des réservations est obtenue`() {

        Mockito.`when`(mockDAO.chercherTous()).thenReturn(listeReservations)
        val service = ReservationsService(mockDAO, mockDAOSiege, mockDAOVols)

        val résultat_obtenu = service.obtenirToutesLesReservations()

        assertEquals(listeReservations, résultat_obtenu)
    }

    @Test
    fun `Étant donné un ReservationsService, lorsque la méthode obtenirReservationParId est appelée avec un ID existant, la réservation correspondante est retournée`() {

        val reservationId = 1
        val expectedReservation = listeReservations.first { it.id == reservationId }
        Mockito.`when`(mockDAO.chercherParId(reservationId)).thenReturn(expectedReservation)

        val service = ReservationsService(mockDAO, mockDAOSiege, mockDAOVols)

        val result = service.obtenirReservationParId(reservationId)

        assertEquals(expectedReservation, result)
    }

    @Test
    fun `Étant donné un ReservationsService, lorsque la méthode obtenirReservationParId est appelée avec un ID inexistant, une RéservationInexistanteException est lancée`() {

        val reservationId = 556
        Mockito.`when`(mockDAO.chercherParId(reservationId)).thenReturn(null)
        val service = ReservationsService(mockDAO, mockDAOSiege, mockDAOVols)

        val exception = assertThrows(RéservationInexistanteException::class.java) {
            service.obtenirReservationParId(reservationId)
        }

        assertEquals("Réservation avec le id: $reservationId est inexistante", exception.message)
    }



}