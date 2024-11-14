package dti.crosemont.reservationvol.AccesAuxDonnees.BD

import dti.crosemont.reservationvol.AccesAuxDonnees.SourcesDeDonnees.VillesDAO
import org.springframework.stereotype.Repository
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.query
import dti.crosemont.reservationvol.Domaine.Modele.Ville
import java.sql.ResultSet



@Repository
class VilleDAOImpl(private val bd: JdbcTemplate): VillesDAO {

    override fun chercherTous(): List<Ville> {
        TODO("function pas encore implemented")
    }

    override fun chercherParId(id: Int): Ville? {
        TODO("function pas encore implemented")
    }
   
    override fun ajouterVille(ville: Ville): Ville {
        TODO("function pas encore implemented")
    }


    override fun effacer(id: Int) {
        TODO("effacer function pas encore implemented")
    }



 }
