package dti.crosemont.reservationvol

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.InjectMocks
import org.junit.jupiter.api.extension.ExtendWith

import dti.crosemont.reservationvol.Domaine.Modele.Reservation
import dti.crosemont.reservationvol.Domaine.Modele.Siège
import dti.crosemont.reservationvol.Domaine.Modele.Client
import dti.crosemont.reservationvol.AccesAuxDonnees.BD.ReservationsDAOImpl

@ExtendWith(MockitoExtension::class)
class ReservationvolApplicationTests {

	@Mock
    lateinit var mockDAOImpl: ReservationsDAOImpl

    val client1 = Client(id = 1, nom = "Mark", prénom = "Lee", adresse = "127 Rue", numéroPasseport = "NC1999", email = "dream.lee@example.com", numéroTéléphone = "12738485")
    val siège1 = Siège(id = 1, numéroSiège = "1A", classe = "économique")
    val reservation = Reservation(
        id = 1,
        numéroRéservation = "N12345",
        idVol = 102,
        clients = listOf(client1),
        sièges = listOf(siège1),
        classe = "économique",
        siegeSelectionne = "1A",
        bagages = 2
    	)

	//Injection du SourcesDeDonnees (mock) dans service
   	@InjectMocks
    	lateinit var reservationsService: ReservationsService 
		

    @Test
    	fun `étant donné une réservation d'ID 1 lorsqu'on cherche la réservation par ID, on obtient la réservation complète`() {
    
        Mockito.`when`(mockDAOImpl.chercherParId(1)).thenReturn(reservation)

        val résultat = reservationsService.obtenirReservationParId(1)

        assertEquals(reservation, résultat)
    	}


/* Debut du test avec exeptions - pas encore fait car les exeptions sont pAS DANS LE CONTROLEUR
	
	@Test
	fun `étant donné une banque de réservations, lorsqu'on cherche une réservation avec un id non existant, on obtient une RessourceInexistanteException`() {
    	Mockito.doReturn(null).`when`(mockDAOImpl).chercherParId(2)

    	val cobaye = ReservationsService(mockDAOImpl)

    	assertFailsWith( RessourceInexistanteException::class ) {
        	cobaye.obtenirReservationParId(2)
   		 	}
		} 
 */
	
}