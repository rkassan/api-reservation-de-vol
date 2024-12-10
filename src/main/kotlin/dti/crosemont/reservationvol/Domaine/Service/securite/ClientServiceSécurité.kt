package dti.crosemont.reservationvol.Domaine.Service.securite

import dti.crosemont.reservationvol.AccesAuxDonnees.SourcesDeDonnees.ClientDAO
import org.springframework.stereotype.Service

@Service
class ClientServiceSécurité(private val dao : ClientDAO) {
    fun naPasDeCompte(email : String ) : Boolean {
        if ( dao.obtenirParEmail( email ) == null ) {
            return true
        }
        return false
    }
}