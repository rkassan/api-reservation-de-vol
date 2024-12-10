package dti.crosemont.reservationvol.Domaine.Service




import dti.crosemont.reservationvol.AccesAuxDonnees.SourcesDeDonnees.VolsDAO
import dti.crosemont.reservationvol.Controleurs.Exceptions.RequêteMalFormuléeException
import dti.crosemont.reservationvol.Controleurs.Exceptions.RessourceInexistanteException
import dti.crosemont.reservationvol.Domaine.Modele.*
import dti.crosemont.reservationvol.Domaine.OTD.VolOTD
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.io.Console
import java.time.Duration
import java.time.LocalDateTime



@ExtendWith(MockitoExtension::class)
class VolServiceTest {

    @Mock
    lateinit var mockDAO: VolsDAO

    @InjectMocks
    lateinit var volService: VolService

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
    val vol2 = Vol(
            id = 2,
            dateDepart = LocalDateTime.of(2024, 12, 12, 10, 30),
            dateArrivee = LocalDateTime.of(2024, 12, 12, 14, 45),
            avion = avion,
            prixParClasse = mapOf("économique" to 350.0, "affaire" to 650.0, "première" to 1200.0),
            poidsMaxBag = 25,
            trajet = trajet,
            vol_statut = listOf(VolStatut(idVol = 2, statut = "Confirmé", heure = LocalDateTime.now())),
            duree = Duration.ofHours(4).plusMinutes(15),
            sièges = emptyList()
    )
    val listeVol = listOf(vol,vol2)

    @Test
    fun `étant donné un vol valide lorsqu'on ajoute un vol, on obtient le vol sauvegardé correctement`() {
        Mockito.doReturn(true).`when`(mockDAO).trajetExiste(trajet.id)
        Mockito.doReturn(true).`when`(mockDAO).avionExiste(avion.id)
        Mockito.doReturn(vol).`when`(mockDAO).ajouterVol(vol)

        val résultat = volService.ajouterVol(vol)

        assertEquals(vol, résultat)
        Mockito.verify(mockDAO).ajouterVol(vol)
    }

    @Test
    fun `étant donné un trajet inexistant lorsqu'on ajoute un vol, on obtient une RessourceInexistanteException`() {
        Mockito.doReturn(false).`when`(mockDAO).trajetExiste(trajet.id)

        val exception = assertThrows(RessourceInexistanteException::class.java) {
            volService.ajouterVol(vol)
        }

        val message= exception.message?.substringAfter("\"")?.substringBeforeLast("\"")
        assertEquals("Le trajet avec l'ID ${trajet.id} n'existe pas.", message)
    }



    @Test
    fun `étant donné une date d'arrivée avant la date de départ lorsqu'on ajoute un vol, on obtient une RequêteMalFormuléeException`() {
        val volInvalide = vol.copy(dateArrivee = vol.dateDepart.minusHours(1))

        Mockito.doReturn(true).`when`(mockDAO).trajetExiste(trajet.id)
        Mockito.doReturn(true).`when`(mockDAO).avionExiste(avion.id)

        val exception = assertThrows(RequêteMalFormuléeException::class.java) {
            volService.ajouterVol(volInvalide)
        }

        assertEquals(
                "La date d'arrivée (${volInvalide.dateArrivee}) ne peut pas être avant la date de départ (${volInvalide.dateDepart}).",
                exception.message
        )
    }


    @Test
    fun `étant donné des paramètres valides, lorsqu'on cherche des vols par parametre, on obtient une liste de vols`() {
        val dateDebut = LocalDateTime.of(2024, 12, 10, 14, 0)
        val aeroportDebut = "CDG"
        val aeroportFin = "JFK"

        val vol1 = vol.copy(id = 1)
        val vol2 = vol.copy(id = 2)
        val vols = listOf(vol1, vol2)

        Mockito.doReturn(vols).`when`(mockDAO).obtenirVolParParam(dateDebut, aeroportDebut, aeroportFin)

        val résultat = volService.obtenirVolParParam(dateDebut, aeroportDebut, aeroportFin)

        assertEquals(vols, résultat)
        Mockito.verify(mockDAO).obtenirVolParParam(dateDebut, aeroportDebut, aeroportFin)
    }

    @Test
    fun `étant donné des paramètres valides mais aucun vol disponible, lorsqu'on cherche des vols par parametre, on obtient une liste vide`() {
        val dateDebut = LocalDateTime.of(2023, 12, 10, 14, 0)
        val aeroportDebut = "CDG"
        val aeroportFin = "JFK"

        Mockito.doReturn(emptyList<Vol>()).`when`(mockDAO).obtenirVolParParam(dateDebut, aeroportDebut, aeroportFin)

        val résultat = volService.obtenirVolParParam(dateDebut, aeroportDebut, aeroportFin)

        assertTrue(résultat.isEmpty())
        Mockito.verify(mockDAO).obtenirVolParParam(dateDebut, aeroportDebut, aeroportFin)
    }
    @Test
    fun `étant donné un VolService, lorsque la methode ChercherTous, on obtient la liste de vols`(){

        Mockito.`when`( mockDAO.chercherTous() ).thenReturn( listeVol )
        val cobay = VolService( mockDAO )

        val résultat_attendu = listeVol
        val résultat_obtenue = cobay.chercherTous()

        assertEquals( résultat_attendu,résultat_obtenue )
    }

    @Test
    fun `étant donné un VolService, lorsque la methode ChercherParId avec l'id 1, le vol 1 est obtenue`(){

        Mockito.`when`( mockDAO.chercherParId( 1 ) ).thenReturn( listeVol[0] )
        val cobaye = VolService( mockDAO )

        val résultat_attendu = listeVol[0]
        val résultat_obtenue = cobaye.chercherParId( 1 )

        assertEquals(résultat_attendu, résultat_obtenue)
    }
}