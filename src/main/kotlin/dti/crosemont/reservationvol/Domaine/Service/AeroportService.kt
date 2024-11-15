package dti.crosemont.reservationvol.Domaine.Service
import dti.crosemont.reservationvol.Controleurs.Exceptions.RequêteMalFormuléeException

import dti.crosemont.reservationvol.AccesAuxDonnees.SourcesDeDonnees.AeroportDAO
import dti.crosemont.reservationvol.Domaine.Modele.Aeroport
import org.springframework.stereotype.Service

@Service
class AeroportService(private val dao: AeroportDAO) {

    fun obtenirTousLesAeroports(): List<Aeroport> = dao.chercherTous()
    fun obtenirAeroportParCode(code: String): Aeroport? = dao.chercherParCode(code)
    fun obtenirAeroportsParNom(nom: String): List<Aeroport> = dao.chercherParNom(nom)
    fun obtenirAeroportParId(id: Int): Aeroport? = dao.chercherParId(id)
    fun supprimerUnAeroport( id : Int ) = dao.effacer( id )
    fun ajouterAeroport(aeroport: Aeroport): Aeroport {
        val resultat = dao.ajouter(aeroport)
        if (resultat != null) {
            return resultat
        }
        throw RequêteMalFormuléeException("L'ajout de l'aéroport a échoué")
    }

    fun modifierAeroport(aeroport: Aeroport): Aeroport {
        val resultat = dao.modifier(aeroport)
        if (resultat != null) {
            return resultat
        }
        throw RequêteMalFormuléeException("La modification de l'aéroport a échoué")
    }
  
}
