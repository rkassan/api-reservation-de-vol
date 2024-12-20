package dti.crosemont.reservationvol.Domaine.Service

import dti.crosemont.reservationvol.AccesAuxDonnees.SourcesDeDonnees.ReservationsDAO
import dti.crosemont.reservationvol.AccesAuxDonnees.SourcesDeDonnees.SiègeDAO
import dti.crosemont.reservationvol.Controleurs.Exceptions.RessourceInexistanteException
import dti.crosemont.reservationvol.Controleurs.Exceptions.RéservationInexistanteException
import dti.crosemont.reservationvol.Domaine.Modele.*
import dti.crosemont.reservationvol.Domaine.OTD.PostReservationOTD
import dti.crosemont.reservationvol.ReservationsService
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import java.time.Duration
import kotlin.test.assertFailsWith


@ExtendWith(MockitoExtension::class)
class ReservationsServiceTest {

    @Mock
    lateinit var mockDAO: ReservationsDAO

    @Mock
    lateinit var mockDAOSiege: SiègeDAO

    @Mock
    lateinit var mockDAOVols: VolService

    @Mock
    lateinit var mockClientService: ClientsService

    @InjectMocks
    lateinit var reservationsService: ReservationsService

    private val listeReservations = listOf(
            Reservation(1, "AB123", 101, Client(1, "Jean", "Lee", "X123456", "127 rue", "jean.lee@example.com", "0123456789"), Siège(1, "A1", "économique"), "économique", 2),
            Reservation(2, "AB124", 102, Client(2, "Marie", "Suh", "B1223", "456 rue", "marie.suh@example.com", "1011121314"), Siège(2, "B2", "affaire"), "affaire", 1)
    )

    private val listeRéservationsClient = listOf(
        Reservation(1, "AB123", 101, Client(1, "Jean", "Lee", "X123456", "127 rue", "jean.lee@example.com", "0123456789"), Siège(1, "A1", "économique"), "économique", 2),
        Reservation(1, "AB147", 109, Client(1, "Jean", "Lee", "X123456", "127 rue", "jean.lee@example.com", "0123456789"), Siège(1, "A1", "économique"), "économique", 4),
    )

    val ville = Ville(id = 1, nom = "Paris", pays = "France", url_photo = "paris.jpg")
    val aeroportDebut = Aeroport(id = 1, code = "CDG", nom = "Charles de Gaulle", ville = ville, adresse = "Paris, France")
    val aeroportFin = Aeroport(id = 2, code = "JFK", nom = "John F. Kennedy", ville = ville.copy(nom = "New York"), adresse = "New York, USA")
    val trajet = Trajet(id = 1, numéroTrajet = "TR-123", aéroportDébut = aeroportDebut, aéroportFin = aeroportFin)

    val avion = Avion(id = 1, type = "Airbus A320")
    val vol = Vol(
            id = 1,
            dateDepart = LocalDateTime.of(2024, 12, 10, 14, 0),
            dateArrivee = LocalDateTime.of(2024, 12, 10, 18, 0),
            avion = avion,
            prixParClasse = mapOf("économique" to 300.0, "affaire" to 600.0, "première" to 1100.0),
            poidsMaxBag = 23,
            trajet = trajet,
            vol_statut = listOf(VolStatut(idVol = 1, statut = "En attente", heure = LocalDateTime.now())),
            duree = Duration.ofHours(4),
            sièges = emptyList()
    )


    @Test
    fun `Étant donné un service ReservationsService et la permissions consulterClient d'un compte valide, lorsque la méthode obtenirToutesLesReservations est appelée, la liste complètes des réservations est obtenue`() {
        val service = ReservationsService(mockDAO, mockDAOSiege, mockDAOVols, mockClientService)
        Mockito.`when`(mockDAO.chercherTous()).thenReturn(listeReservations)
        
        val courriel = "courriel.à.test@email.com"
        val listePermissions = listOf<String>("consulter:réservations") 

        val résultat_obtenu = service.obtenirToutesLesReservations(listePermissions, courriel)
        val résultat_attendu = listeReservations

        assertEquals(résultat_attendu, résultat_obtenu)
    }

    @Test
    fun `Étant donné un service ReservationsService sans permissions avec un courriel client, lorsqu'on appel la méthode obtenirToutesLesReservations, la liste complètes des reservations du client seulement est retourné`() {
        val service = ReservationsService(mockDAO, mockDAOSiege, mockDAOVols, mockClientService)
        Mockito.`when`(mockDAO.chercherTous(1)).thenReturn(listeRéservationsClient)
        Mockito.`when`(mockClientService.obtenirClientParEmail("jean.lee@example.com")).thenReturn(Client(1, "Jean", "Lee", "X123456", "127 rue", "jean.lee@example.com", "0123456789"))

        val courriel = "jean.lee@example.com"
        val listePermissions = listOf<String>() 

        val résultat_obtenu = service.obtenirToutesLesReservations(listePermissions, courriel)
        val résultat_attendu = listeRéservationsClient

        assertEquals(résultat_attendu, résultat_obtenu)

    }

    /** 
    @Test
    fun `Étant donné un ReservationService, lorsque la méthode ajouterReservation est appelée avec une réservation valide, la réservation est retourné`() {
        val service = ReservationsService(mockDAO, mockDAOSiege, mockDAOVols, mockClientService)
        val réservation = PostReservationOTD(1, "jean.lee@example.com", Siège(1, "A1", "économique", "disponible") , "économique", 1)
        Mockito.`when`(mockClientService.obtenirClientParEmail(réservation.clientCourriel)).thenReturn(Client(1, "Jean", "Lee", "X123456", "127 rue", "jean.lee@example.com", "0123456789"))
        Mockito.`when`(mockDAOVols.chercherSiegeParVolId(réservation.idVol)).thenReturn(listOf( Siège(1 , "A1", "économique", "disponible"), Siège(2 , "A2", "économique", "disponible") ))
        Mockito.`when`(mockDAOVols.chercherParId(réservation.idVol)).thenReturn(vol)
        Mockito.`when`(service.generateNuméroRéservation()).thenReturn("Rab123")
        Mockito.`when`(mockDAO.ajouterReservation(Reservation(0, "Rab123", 1, Client(1, "Jean", "Lee", "X123456", "127 rue", "jean.lee@example.com", "0123456789"), Siège(1 , "A1", "économique", "disponible"), "économique", 1))).thenReturn((Reservation(0, "Rab123", 1, Client(1, "Jean", "Lee", "X123456", "127 rue", "jean.lee@example.com", "0123456789"), Siège(1 , "A1", "économique", "disponible"), "économique", 1)))

        val résultat_obtenu = service.ajouterReservation(réservation)
        val résultat_attendu = (Reservation(0, "Rab123", 1, Client(1, "Jean", "Lee", "X123456", "127 rue", "jean.lee@example.com", "0123456789"), Siège(1 , "A1", "économique", "disponible"), "économique", 1))
        assertEquals(résultat_attendu, résultat_obtenu)
    } **/

    @Test
    fun `Étant donné un ReservationsService, lorsque la méthode obtenirReservationParId est appelée avec un ID existant, la réservation correspondante est retournée`() {

        val reservationId = 1
        val expectedReservation = listeReservations.first { it.id == reservationId }
        Mockito.`when`(mockDAO.chercherParId(reservationId)).thenReturn(expectedReservation)

        val service = ReservationsService(mockDAO, mockDAOSiege, mockDAOVols, mockClientService)

        val result = service.obtenirReservationParId(reservationId, "jean.lee@example.com")

        assertEquals(expectedReservation, result)
    }

    @Test
    fun `Étant donné un ReservationsService, lorsque la méthode obtenirReservationParId est appelée avec un ID inexistant, une RéservationInexistanteException est lancée`() {

        val reservationId = 556
        Mockito.`when`(mockDAO.chercherParId(reservationId)).thenReturn(null)
        val service = ReservationsService(mockDAO, mockDAOSiege, mockDAOVols,mockClientService)

        val exception = assertThrows(RéservationInexistanteException::class.java) {
            service.obtenirReservationParId(reservationId, "jean.lee@example.com")
        }

        assertEquals("Réservation avec le id: $reservationId est inexistante", exception.message)
    }
}