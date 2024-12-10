package dti.crosemont.reservationvol.Domaine.Service

import dti.crosemont.reservationvol.AccesAuxDonnees.SourcesDeDonnees.AeroportDAO
import dti.crosemont.reservationvol.Controleurs.Exceptions.RequêteMalFormuléeException
import dti.crosemont.reservationvol.Controleurs.Exceptions.RessourceInexistanteException
import dti.crosemont.reservationvol.Domaine.Modele.Aeroport
import dti.crosemont.reservationvol.Domaine.OTD.AeroportOTD
import org.springframework.stereotype.Service

@Service
class AeroportService(private val dao: AeroportDAO) {

    fun obtenirTousLesAeroports(): List<Aeroport> {
        val aeroports = dao.chercherTous()
        if (aeroports.isEmpty()) {
            throw RessourceInexistanteException("Aucun aéroport trouvé.")
        }
        return aeroports
    }

    fun obtenirAeroportParCode(code: String): Aeroport {
        return dao.chercherParCode(code)
                ?: throw RessourceInexistanteException("Aéroport avec le code $code introuvable.")
    }

    fun obtenirAeroportsParNom(nom: String): List<Aeroport> {
        val aeroports = dao.chercherParNom(nom)
        if (aeroports.isEmpty()) {
            throw RessourceInexistanteException("Aucun aéroport trouvé avec le nom $nom.")
        }
        return aeroports
    }

    fun obtenirAeroportParId(id: Int): Aeroport {
        return dao.chercherParId(id)
                ?: throw RessourceInexistanteException("Aéroport avec l'id $id introuvable.")
    }

    fun supprimerUnAeroport(id: Int) {
        val aeroportExistant = dao.chercherParId(id)
        if (aeroportExistant == null) {
            throw RessourceInexistanteException(
                    "Impossible de supprimer, l'aéroport avec l'id $id n'existe pas."
            )
        }
        dao.effacer(id)
    }

    fun ajouterAeroport(aeroport: Aeroport): Aeroport {
        val resultat = dao.ajouter(aeroport)
        if (resultat != null) {
            return resultat
        }
        throw RequêteMalFormuléeException("L'ajout de l'aéroport a échoué")
    }

    fun modifierAeroport(aeroportOTD: AeroportOTD, id: Int, aeroportExistant: Aeroport): Aeroport {
        aeroportOTD.apply {
            code?.let { aeroportExistant.code = it }
            nom?.let { aeroportExistant.nom = it }
            ville?.let { aeroportExistant.ville = it }
            adresse?.let { aeroportExistant.adresse = it }
        }
        return dao.modifier(aeroportExistant)
                ?: throw RequêteMalFormuléeException("La modification de l'aéroport a échoué")
    }
}
