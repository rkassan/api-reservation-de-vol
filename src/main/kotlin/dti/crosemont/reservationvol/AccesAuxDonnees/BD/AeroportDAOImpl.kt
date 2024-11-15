package dti.crosemont.reservationvol.AccesAuxDonnees.BD

import dti.crosemont.reservationvol.AccesAuxDonnees.SourcesDeDonnees.AeroportDAO
import dti.crosemont.reservationvol.Domaine.Modele.Aeroport
import dti.crosemont.reservationvol.Domaine.Modele.Ville
import java.sql.ResultSet
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.query
import org.springframework.stereotype.Repository

@Repository
class AeroportDAOImpl(private val bd: JdbcTemplate) : AeroportDAO {

    companion object {
        private const val OBTENIR_TOUT_LES_AEROPORTS =
                "SELECT * FROM aeroports INNER JOIN villes ON aeroports.ville_id = villes.id;"
        private const val OBTENIR_AEROPORT_PAR_CODE =
                "SELECT * FROM aeroports INNER JOIN villes ON aeroports.ville_id = villes.id WHERE code = ?;"
        private const val OBTENIR_AEROPORT_PAR_NOM =
                "SELECT * FROM aeroports INNER JOIN villes ON aeroports.ville_id = villes.id WHERE nom LIKE ?;"
    }

    override fun chercherTous(): List<Aeroport> =
            bd.query(OBTENIR_TOUT_LES_AEROPORTS) { rs, _ -> convertirRésultatEnAeroport(rs) }

    override fun chercherParId(id: Int): Aeroport? =
            bd
                    .query(OBTENIR_AEROPORT_PAR_CODE, id) { rs, _ ->
                        convertirRésultatEnAeroport(rs)
                    }
                    .singleOrNull()

    override fun chercherParCode(code: String): Aeroport? =
            bd
                    .query(OBTENIR_AEROPORT_PAR_CODE, code) { rs, _ ->
                        convertirRésultatEnAeroport(rs)
                    }
                    .singleOrNull()

    override fun chercherParNom(nom: String): List<Aeroport> =
            bd.query(OBTENIR_AEROPORT_PAR_NOM, "%$nom%") { rs, _ ->
                convertirRésultatEnAeroport(rs)
            }

    override fun effacer(id: Int) {
        bd.update("DELETE FROM aeroports WHERE id = ?", id)
    }

    private fun convertirRésultatEnAeroport(rs: ResultSet): Aeroport {
        return Aeroport(
                id = rs.getInt("id"),
                code = rs.getString("code"),
                nom = rs.getString("nom"),
                ville =
                        Ville(
                                id = rs.getInt("ville_id"),
                                nom = rs.getString("ville_nom"),
                                pays = rs.getString("pays"),
                                url_photo = rs.getString("url_photo")
                        ),
                adresse = rs.getString("adresse")
        )
    }
}
