package dti.crosemont.reservationvol.Domaine.Service

import dti.crosemont.reservationvol.AccesAuxDonnees.SourcesDeDonnees.ClientDAO
import dti.crosemont.reservationvol.Controleurs.Exceptions.RessourceInexistanteException
import dti.crosemont.reservationvol.Domaine.Modele.Client
import dti.crosemont.reservationvol.Domaine.OTD.ClientOTD
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@ExtendWith(MockitoExtension::class)
class ClientsServiceTest {
    lateinit var mockDAO : ClientDAO

    val listeClients = listOf(
        Client( 1, "Jean", "Dubois", "10 rue des Lilas", "123456789", "jean.dubois@email.com", "0123456789" ),
        Client( 2, "Jeanne", "Dubois", "10 rue des Lilas", "123456789", "jeanne.dubois@email.com", "0123456789" ),
        Client( 3, "Jean-Louis", "Dubois", "10 rue des Lilas", "123456789", "jean-louis.dubois@email.com", "0123456789" ),
    )

    @Test
    fun `Étant donné un ClientService, lorsque la méthode obtenirToutLesClients est appelée, la liste de clients est obtenue`(){
        Mockito.`when`( mockDAO.chercherTous() ).thenReturn( listeClients )
        val cobaye = ClientsService( mockDAO )

        val résultat_attendu = listeClients
        val résultat_obtenue = cobaye.obtenirToutLesClient()

        assertEquals( résultat_attendu, résultat_obtenue )
    }

    @Test
    fun `Étant donné un ClientService, lorsque la méthode obtenirClientsParMotCle est appelée avec le mot clé Dubois, la liste de clients est obtenue avec tout les clients dont le nom ou le prénom contient Dubois`(){
        Mockito.`when`( mockDAO.chercherParMotCle( "Dubois" ) ).thenReturn( listeClients )
        val cobaye = ClientsService( mockDAO )

        val résultat_attendu = listeClients
        val résultat_obtenue = cobaye.obtenirClientsParMotCle( "Dubois" )

        assertEquals( résultat_attendu, résultat_obtenue )
    }

    @Test
    fun `Étant donné un Client service lorsque la méthode obtenirClientsParMotCle est appelée avec le mot clé xx, qui ne correspond pas a un client, une liste vide est obtenu`(){
        Mockito.`when`( mockDAO.chercherParMotCle( "xx" ) ).thenReturn( listOf() )
        val cobaye = ClientsService( mockDAO )

        val résultat_attendu = listOf<Client>()
        val résultat_obtenue = cobaye.obtenirClientsParMotCle( "xx" )

        assertEquals( résultat_attendu, résultat_obtenue )

    }

    @Test
    fun `Étant donné un ClientService, lorsque la méthode obtenirClientParEmail est appelée avec le email jean dubois@email com, le client Jean Dubois est obtenue`(){
        Mockito.`when`( mockDAO.obtenirParEmail( "jean.dubois@email.com" ) ).thenReturn( listeClients[0] )
        val cobaye = ClientsService( mockDAO )

        val résultat_attendu = listeClients[0]
        val résultat_obtenue = cobaye.obtenirClientParEmail( "jean.dubois@email.com" )

        assertEquals( résultat_attendu, résultat_obtenue )
    }

    @Test
    fun `Étant donné un ClientService, lorsque la méthode obtenirClientParEmail est appelée avec un email inexistant, une exception de type RessourceInexistanteException est lancée`() {
        Mockito.`when`(mockDAO.obtenirParEmail("efew")).thenReturn(null)
        val cobaye = ClientsService(mockDAO)

        assertFailsWith<RessourceInexistanteException> {
            cobaye.obtenirClientParEmail("efew")
        }
    }

    @Test
    fun `Étant donné un ClientService, lorsque la méthode obtenirParId est appelée avec l'id 1, le client Jean Dubois est obtenue`(){
        Mockito.`when`( mockDAO.chercherParId( 1 ) ).thenReturn( listeClients[0] )
        val cobaye = ClientsService( mockDAO )

        val résultat_attendu = listeClients[0]
        val résultat_obtenue = cobaye.obtenirParId( 1 )

        assertEquals( résultat_attendu, résultat_obtenue )
    }

    @Test
    fun `Étant donné un ClientService, lorsque la méthode ajouterClient est appelée avec un client, le client est ajouté`() {
        val client = Client(4, "Jean", "Dubois", "10 rue des Lilas", "123456789", "jean.dubois@email.com", "0123456789")
        Mockito.`when`( mockDAO.ajouter( client ) ).thenReturn( client )
        val cobaye = ClientsService( mockDAO )

        val résultat_attendu = client
        val résultat_obtenue = cobaye.ajouterClient( client )

        assertEquals(résultat_attendu, résultat_obtenue)
    }

    @Test
    fun `Étant donné un ClientService, lorsque la méthode modifierClient est appelée avec un clientOTD et un id, le client est modifié`() {
        val clientOTD =
            ClientOTD("Johnny", "Dubois", "10 rue des Lilas", "123456789", "jean.dubois@email.com", "0123456789")
        val client = Client(1, "Johnny", "Dubois", "10 rue des Lilas", "123456789", "jean.dubois@email.com", "0123456789")
        Mockito.`when`( mockDAO.chercherParId( 1 ) ).thenReturn( listeClients[0] )
        Mockito.`when`( mockDAO.modifier( client ) ).thenReturn( client )

        val cobaye = ClientsService( mockDAO )

        val résultat_attendu = client
        val résultat_obtenue = cobaye.modifierClient( clientOTD, 4 )

        assertEquals( résultat_attendu, résultat_obtenue )
    }

    @Test
    fun `Étant donné un ClientService, lorsque la méthode modifierClient est appelée avec un clientOTD et un id inexistant, une exception de type RessourceInexistanteException est lancée`() {
        val clientOTD =
            ClientOTD("Johnny", "Dubois", "10 rue des Lilas", "123456789", "sss", "0123456789")

        Mockito.`when`(mockDAO.chercherParId(1)).thenReturn(listeClients[0])

        val cobaye = ClientsService(mockDAO)

        assertFailsWith<RessourceInexistanteException> {
            cobaye.modifierClient(clientOTD, 1)
        }

    }
}