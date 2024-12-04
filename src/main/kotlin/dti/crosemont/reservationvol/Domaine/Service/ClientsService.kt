package dti.crosemont.reservationvol.Domaine.Service

import dti.crosemont.reservationvol.AccesAuxDonnees.SourcesDeDonnees.ClientDAO
import dti.crosemont.reservationvol.Controleurs.Exceptions.ModificationException
import dti.crosemont.reservationvol.Controleurs.Exceptions.RequêteMalFormuléeException
import dti.crosemont.reservationvol.Controleurs.Exceptions.RessourceInexistanteException
import org.springframework.stereotype.Service
import dti.crosemont.reservationvol.Domaine.Modele.Client
import dti.crosemont.reservationvol.Domaine.OTD.ClientOTD

@Service
class ClientsService( private val dao : ClientDAO) {

    fun obtenirToutLesClient() : List<Client> = dao.chercherTous()
    fun obtenirClientsParMotCle( motClé : String ) : List<Client> = dao.chercherParMotCle( motClé )
    fun obtenirClientParEmail( email : String ) : Client =
        dao.obtenirParEmail( email ) ?: throw RessourceInexistanteException( "L'email $email n'est pas associé à aucun client" )
    fun obtenirParId( id : Int ) : Client =
            dao.chercherParId( id ) ?: throw RessourceInexistanteException( "Le client n'existe pas" )

    fun ajouterClient( client : Client ) : Client =
            dao.ajouter( client ) ?: throw RequêteMalFormuléeException( "L'ajout du client à échouer" )

    fun modifierClient( clientOTD : ClientOTD, id: Int ) : Client {
        val clientExistant = dao.chercherParId( id ) ?: throw RessourceInexistanteException( "Le client n'existe pas" )
        clientOTD.apply {
            nom?.let { clientExistant.nom = it }
            prénom?.let { clientExistant.prénom = it }
            adresse?.let { clientExistant.adresse = it }
            numéroPasseport?.let { clientExistant.numéroPasseport = it }
            email?.let { clientExistant.email = it }
            numéroTéléphone?.let { clientExistant.numéroTéléphone = it }
        }
        return dao.modifier( clientExistant ) ?: throw ModificationException( "La modification du client à échouer" )
    }

    fun supprimerUnClient( id : Int ) = dao.effacer( id )
}