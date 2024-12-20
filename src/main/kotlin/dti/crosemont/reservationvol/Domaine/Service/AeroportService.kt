package dti.crosemont.reservationvol.Domaine.Service

import dti.crosemont.reservationvol.AccesAuxDonnees.SourcesDeDonnees.AeroportDAO
import dti.crosemont.reservationvol.Controleurs.Exceptions.RequêteMalFormuléeException
import dti.crosemont.reservationvol.Controleurs.Exceptions.RessourceInexistanteException
import dti.crosemont.reservationvol.Domaine.Modele.Aeroport
import dti.crosemont.reservationvol.Domaine.OTD.AeroportOTD
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service

@Service
class AeroportService(private val dao: AeroportDAO) {

    fun obtenirTousLesAeroports(): List<Aeroport> {
        val aeroports = dao.chercherTous()
        return if (aeroports.isNotEmpty()) {
            aeroports
        } else {
            emptyList()
        }
    }

    fun obtenirAeroportParCode(code: String): Aeroport {
        return dao.chercherParCode(code)
                ?: throw RessourceInexistanteException("Aéroport avec le code $code introuvable.")
    }

    fun obtenirAeroportsParNom(nom: String): List<Aeroport> {
        val aeroports = dao.chercherParNom(nom)
        return if (aeroports.isNotEmpty()) {
            aeroports
        } else {
            emptyList()
        }
    }

    fun obtenirAeroportParId(id: Int): Aeroport {
        return dao.chercherParId(id)
                ?: throw RessourceInexistanteException("Aéroport avec l'id $id introuvable.")
    }
    @PreAuthorize("hasAuthority('supprimer:aéroport')")
    fun supprimerUnAeroport(id: Int) {
        val aeroportExistant = dao.chercherParId(id)
        if (aeroportExistant == null) {
            throw RessourceInexistanteException(
                    "Impossible de supprimer, l'aéroport avec l'id $id n'existe pas."
            )
        }
        dao.effacer(id)
    }
    @PreAuthorize("hasAuthority('créer:aéroport')")
    fun ajouterAeroport(aeroport: Aeroport): Aeroport {
        val aeroportExistant = dao.chercherParCode(aeroport.code)
        if (aeroportExistant != null) {
            throw RequêteMalFormuléeException(
                    "L'aéroport avec le code ${aeroport.code} existe déjà."
            )
        }

        val aeroportParNom = dao.chercherParNom(aeroport.nom)
        if (aeroportParNom.isNotEmpty()) {
            throw RequêteMalFormuléeException(
                    "Un aéroport avec le nom ${aeroport.nom} existe déjà."
            )
        }

        val resultat = dao.ajouter(aeroport)
        if (resultat != null) {
            return resultat
        }
        throw RequêteMalFormuléeException("L'ajout de l'aéroport a échoué")
    }
    @PreAuthorize("hasAuthority('modifier:aéroport')")
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
