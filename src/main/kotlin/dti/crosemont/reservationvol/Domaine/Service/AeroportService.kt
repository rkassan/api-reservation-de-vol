package dti.crosemont.reservationvol.Domaine.Service

import dti.crosemont.reservationvol.AccesAuxDonnees.SourcesDeDonnees.AeroportDAO
import dti.crosemont.reservationvol.Domaine.Modele.Aeroport
import org.springframework.stereotype.Service

@Service
class AeroportService(private val dao: AeroportDAO) {

    fun obtenirTousLesAeroports(): List<Aeroport> = dao.chercherTous()
    fun obtenirAeroportParCode(code: String): Aeroport? = dao.chercherParCode(code)
    fun obtenirAeroportsParNom(nom: String): List<Aeroport> = dao.chercherParNom(nom)
    fun obtenirAeroportParId(id: Int): Aeroport? = dao.chercherParId(id)
}
