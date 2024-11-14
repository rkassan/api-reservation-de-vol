package dti.crosemont.reservationvol.Domaine.Service

import dti.crosemont.reservationvol.AccesAuxDonnees.SourcesDeDonnees.VillesDAO
import org.springframework.stereotype.Service
import dti.crosemont.reservationvol.Domaine.Modele.Ville


@Service
class VillesService(private val villesDAO : VillesDAO) {

    fun obtenirToutesLesVilles(): List<Ville> {
        TODO("function pas encore implémentée")
    }

    fun obtenirVilleParId(id: Int): Ville? {
        TODO("function pas encore implémentée")
    }

    fun modifierVille(id: Int, modifieVille: Ville): ResponseEntity<Ville> {
        TODO("function pas encore implémentée")
    }

    fun ajouterVille(ville: Ville): Ville {
        TODO("function pas encore implémentée")
    }

}