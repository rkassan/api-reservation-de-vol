package dti.crosemont.reservationvol.Domaine.Service

import dti.crosemont.reservationvol.AccesAuxDonnees.BD.VilleDAOImpl
import org.springframework.stereotype.Service
import dti.crosemont.reservationvol.Domaine.Modele.Ville
import org.springframework.http.ResponseEntity
import dti.crosemont.reservationvol.Controleurs.Exceptions.RequêteMalFormuléeException
import dti.crosemont.reservationvol.Controleurs.Exceptions.RessourceInexistanteException
import dti.crosemont.reservationvol.Controleurs.Exceptions.RessourceEexistanteException
import org.springframework.http.HttpStatus 
import org.springframework.security.access.prepost.PostAuthorize
import org.springframework.security.access.prepost.PreAuthorize



@Service
class VillesService(private val villesDAO : VilleDAOImpl) {

    fun obtenirToutesLesVilles(): List<Ville> = villesDAO.chercherTous()

    fun obtenirVilleParId(id: Int): Ville? {
       return villesDAO.chercherParId(id)?: throw RessourceInexistanteException( "La ville n'existe pas" )
    }

    @PreAuthorize("hasAnyAuthority('créer:villes')")
     fun ajouterVille(ville: Ville ): Ville {
         if (existeVille(ville.nom, ville.pays)) {
            throw RessourceEexistanteException("La ville avec le nom ${ville.nom} existe déjà.")
        }
       return villesDAO.ajouterVille(ville)
    }

    @PreAuthorize("hasAnyAuthority('modifier:villes')")
    fun modifierVille(id: Int, modifieVille: Ville): ResponseEntity<Ville> {
        val updatedVille = villesDAO.modifierVille(id, modifieVille)

        return if (updatedVille != null) {
            ResponseEntity(updatedVille, HttpStatus.OK) 
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND) 
        }
    }

    @PreAuthorize("hasAnyAuthority('supprimer:villes')")
    fun effacerVille(id: Int) {
        val ville = villesDAO.chercherParId(id)

        if(ville == null){
            throw RequêteMalFormuléeException("La ville avec l'ID $id est introuvable.")
        }
        villesDAO.effacer(id)
    }


    fun existeVille(nom: String, pays:String): Boolean {
        return villesDAO.chercherParNomEtPays(nom, pays) != null
    }

}

