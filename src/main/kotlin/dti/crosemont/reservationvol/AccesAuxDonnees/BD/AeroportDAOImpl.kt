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
        private const val AJOUTER_AEROPORT =
            """
            INSERT INTO aeroports (code, nom, ville_id, adresse)
            VALUES (?, ?, ?, ?);
            """
        private const val OBTENIR_DERNIER_AEROPORT_INSERE =
            "SELECT * FROM aeroports WHERE id = LAST_INSERT_ID();"
        private const val MODIFIER_AEROPORT =
            """
            UPDATE aeroports
            SET code = ?, nom = ?, ville_id = ?, adresse = ?
            WHERE id = ?;
            """
    }

    override fun chercherTous(): List<Aeroport> =
        bd.query(OBTENIR_TOUT_LES_AEROPORTS) { réponse, _ ->
            convertirRésultatEnAeroport(réponse)
        }

    override fun chercherParId(id: Int): Aeroport? =
        bd
            .query(OBTENIR_AEROPORT_PAR_CODE, id) { réponse, _ ->
                convertirRésultatEnAeroport(réponse)
            }
            .singleOrNull()

    override fun chercherParCode(code: String): Aeroport? =
        bd
            .query(OBTENIR_AEROPORT_PAR_CODE, code) { réponse, _ ->
                convertirRésultatEnAeroport(réponse)
            }
            .singleOrNull()

    override fun chercherParNom(nom: String): List<Aeroport> =
        bd.query(OBTENIR_AEROPORT_PAR_NOM, "%$nom%") { réponse, _ ->
            convertirRésultatEnAeroport(réponse)
        }

    override fun effacer(id: Int) {
        bd.update("DELETE FROM aeroports WHERE id = ?", id)
    }

    override fun modifier(aeroport: Aeroport): Aeroport? {
        val resultat =
            bd.update(
                MODIFIER_AEROPORT,
                aeroport.code,
                aeroport.nom,
                aeroport.ville.id,
                aeroport.adresse,
                aeroport.id
            )
        return if (resultat != 0) aeroport else null
    }

    override fun ajouter(aeroport: Aeroport): Aeroport? {
        val resultat =
            bd.update(
                AJOUTER_AEROPORT,
                aeroport.code,
                aeroport.nom,
                aeroport.ville.id,
                aeroport.adresse
            )
        return if (resultat != 0) {
            bd.query(OBTENIR_DERNIER_AEROPORT_INSERE) { réponse, _ ->
                convertirRésultatEnAeroport(réponse)
            }.singleOrNull()
        } else null
    }

    private fun convertirRésultatEnAeroport(réponse: ResultSet): Aeroport {
        return Aeroport(
            id = réponse.getInt("id"),
            code = réponse.getString("code"),
            nom = réponse.getString("nom"),
            ville = Ville(
                id = réponse.getInt("ville_id"),
                nom = réponse.getString("villes.nom"),
                pays = réponse.getString("villes.pays"),
                url_photo = réponse.getString("villes.url_photo")
            ),
            adresse = réponse.getString("adresse")
        )
    }
}
