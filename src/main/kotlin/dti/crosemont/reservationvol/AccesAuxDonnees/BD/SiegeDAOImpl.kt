package dti.crosemont.reservationvol.AccesAuxDonnees.BD

import dti.crosemont.reservationvol.AccesAuxDonnees.SourcesDeDonnees.SiègeDAO
import org.springframework.stereotype.Repository
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.query
import dti.crosemont.reservationvol.Domaine.Modele.Siège
import org.springframework.beans.factory.annotation.Autowired
import java.sql.ResultSet


@Repository
class SiègeDAOImpl(private val bd: JdbcTemplate): SiègeDAO {

        override fun chercherTous(): List<Siège> {
            val query = """
                SELECT s.id, s.numéro_siège, s.classe, vs.statut_siege
                FROM sièges s
                LEFT JOIN vols_sièges vs ON s.id = vs.s siège_id
                """
                return bd.query(query) { reponse, _ ->
                    Siège(
                    id = reponse.getInt("id"),
                    numéroSiège = reponse.getString("numéro_siège"),
                    classe = reponse.getString("classe"),
                    statut = reponse.getString("statut_siege") ?: "disponible" 
            )
        }
        }

    override fun chercherParId(id: Int): Siège? {
        val query = """
                SELECT s.id, s.numéro_siège, s.classe, vs.statut_siege
                FROM sièges s
                LEFT JOIN vols_sièges vs ON s.id = vs.s siège_id
                WHERE s.id = ?
            """
            return bd.query(query, arrayOf(id)) { reponse, _ ->
                Siège(
                id = reponse.getInt("id"),
                numéroSiège = reponse.getString("numéro_siège"),
                classe = reponse.getString("classe"),
                statut = reponse.getString("statut_siege") ?: "disponible" 
                )
            }.firstOrNull()
        }

        override fun sauvegarder(siege: Siège): Siège {
            return if (siege.id == 0) {
                val query = """
                    INSERT INTO sièges (numéro_siège, classe) 
                    VALUES (?, ?)
                """
                bd.update(query, siege.numéroSiège, siege.classe)
            siege
            } else {
            val query = """
                UPDATE sièges
                SET numéro_siège = ?, classe = ?
                WHERE id = ?
            """
            bd.update(query, siege.numéroSiège, siege.classe, siege.id)
            siege
        }
    }


    override fun effacer(id: Int) {
        val query = "DELETE FROM sièges WHERE id = ?"
        bd.update(query, id)
    }
}