package dti.crosemont.reservationvol.Service

import org.springframework.stereotype.Service
import dti.crosemont.reservationvol.ClientDAO
import dti.crosemont.reservationvol.Entites.Client

@Service
class ClientsService( private val dao : ClientDAO ) {

    fun obtenirToutLesClient() : List<Client> = dao.chercherTous()
    fun obtenirClientsParMotCle( motClé : String ) : List<Client> = dao.chercherParMotCle( motClé )
    fun obtenirParId( id : Int ) : Client? = dao.chercherParId( id )

}