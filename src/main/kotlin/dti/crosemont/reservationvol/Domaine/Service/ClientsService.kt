package dti.crosemont.reservationvol.Domaine.Service

import dti.crosemont.reservationvol.AccesAuxDonnees.SourcesDeDonnees.ClientDAO
import org.springframework.stereotype.Service
import dti.crosemont.reservationvol.Domaine.Modele.Client

@Service
class ClientsService( private val dao : ClientDAO) {

    fun obtenirToutLesClient() : List<Client> = dao.chercherTous()
    fun obtenirClientsParMotCle( motClé : String ) : List<Client> = dao.chercherParMotCle( motClé )
    fun obtenirParId( id : Int ) : Client? = dao.chercherParId( id )

}