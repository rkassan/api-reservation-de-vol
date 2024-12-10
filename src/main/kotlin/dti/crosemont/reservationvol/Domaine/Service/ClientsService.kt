package dti.crosemont.reservationvol.Domaine.Service

import dti.crosemont.reservationvol.AccesAuxDonnees.SourcesDeDonnees.ClientDAO
import dti.crosemont.reservationvol.Controleurs.Exceptions.ModificationException
import dti.crosemont.reservationvol.Controleurs.Exceptions.RequêteMalFormuléeException
import dti.crosemont.reservationvol.Controleurs.Exceptions.RessourceInexistanteException
import org.springframework.stereotype.Service
import dti.crosemont.reservationvol.Domaine.Modele.Client
import dti.crosemont.reservationvol.Domaine.OTD.ClientOTD
import org.springframework.security.access.prepost.PostAuthorize
import org.springframework.security.access.prepost.PreAuthorize

@Service
class ClientsService( private val dao: ClientDAO ) {
    @PreAuthorize("hasAnyAuthority('consulter:clients')")
    fun obtenirToutLesClient() : List<Client> = dao.chercherTous()

    @PreAuthorize("hasAnyAuthority('consulter:clients')")
    fun obtenirClientsParMotCle( motClé : String ) : List<Client> = dao.chercherParMotCle( motClé )

    @PostAuthorize("hasAnyAuthority('consulter:clients') || authentication.principal.claims['courriel'] == returnObject.email")
    fun obtenirClientParEmail( email : String ) : Client =
        dao.obtenirParEmail( email ) ?: throw RessourceInexistanteException( "L'email $email n'est pas associé à aucun client" )

    @PostAuthorize("hasAnyAuthority('consulter:clients') || authentication.principal.claims['courriel'] == returnObject.email")
    fun obtenirParId( id : Int ) : Client =
            dao.chercherParId( id ) ?: throw RessourceInexistanteException( "Le client n'existe pas" )

    @PreAuthorize("hasAnyAuthority('créer:clients') || clientServiceSécurité.naPasDeCompte(#client.email)")
    fun ajouterClient( client : Client ) : Client =
            dao.ajouter( client ) ?: throw RequêteMalFormuléeException( "L'ajout du client à échouer" )

    @PreAuthorize("hasAnyAuthority('modifier:clients') || authentication.principal.claims['courriel'] == #clientExistant.email")
    fun modifierClient( clientOTD : ClientOTD, id: Int, clientExistant : Client ) : Client {
        clientOTD.apply {
            nom?.let { clientExistant.nom = it }
            prénom?.let { clientExistant.prénom = it }
            adresse?.let { clientExistant.adresse = it }
            numéroPasseport?.let { clientExistant.numéroPasseport = it }
            numéroTéléphone?.let { clientExistant.numéroTéléphone = it }
        }
        return dao.modifier( clientExistant ) ?: throw ModificationException( "La modification du client à échouer" )
    }

    @PreAuthorize("hasAnyAuthority('supprimer:clients')")
    fun supprimerUnClient( id : Int ) = dao.effacer( id )
}