package dti.crosemont.reservationvol.AccesAuxDonnees.SourcesDeDonnees

import dti.crosemont.reservationvol.Domaine.Modele.Ville


interface VillesDAO: DAO<Ville>{
        fun ajouterVille(ville: Ville): Ville
        fun modifierVille(id: Int, modifieVille: Ville): Ville
}
