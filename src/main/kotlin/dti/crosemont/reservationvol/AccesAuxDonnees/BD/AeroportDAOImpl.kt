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
                        "SELECT * FROM aéroports INNER JOIN villes ON aéroports.ville_id = villes.id;"
                private const val OBTENIR_AEROPORT_PAR_CODE =
                        "SELECT * FROM aéroports INNER JOIN villes ON aéroports.ville_id = villes.id WHERE code = ?;"
                private const val OBTENIR_AEROPORT_PAR_NOM =
                        "SELECT * FROM aéroports INNER JOIN villes ON aéroports.ville_id = villes.id WHERE nom = ?;"
                private const val AJOUTER_AEROPORT: String =
                        """
                INSERT INTO aéroports ( code, nom, ville, adresse )
                VALUES ( ?, ?, ?, ?);
                """
                private const val OBTENIR_DERNIER_AEROPORT_INSÉRER =
                        """
                    SELECT * FROM aéroports 
                    WHERE id = ( SELECT MAX( id ) from aéroports   );
                """
                private const val OBTENIR_DERNIER_CLIENT_INSÉRER =
                        """
                    SELECT * FROM clients 
                    WHERE id = ( SELECT MAX( id ) from clients  );
                """
                private const val MODIFIER_AEROPORT: String =
                        """
                    UPDATE aéroports
                    SET code = ?, nom = ?, ville = ?, adresse = ?
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

                val résultat =
                        bd.update(
                                MODIFIER_AEROPORT,
                                aeroport.code,
                                aeroport.nom,
                                aeroport.ville,
                                aeroport.adresse,
                                aeroport.id
                        )

                return if (résultat != 0) aeroport else null
        }

        override fun ajouter(aeroport: Aeroport): Aeroport? {
                var insertedAeroport: Aeroport? = null

                val resultat =
                        bd.update(
                                AJOUTER_AEROPORT,
                                aeroport.code,
                                aeroport.nom,
                                aeroport.ville,
                                aeroport.adresse
                        )

                if (resultat != 0) {
                        insertedAeroport =
                                bd
                                        .query(sql = OBTENIR_DERNIER_AEROPORT_INSÉRER) { réponse, _
                                                ->
                                                convertirRésultatEnAeroport(réponse)
                                        }
                                        .singleOrNull()
                }
                return insertedAeroport
        }

        private fun convertirRésultatEnAeroport(réponse: ResultSet): Aeroport {
                return Aeroport(
                        id = réponse.getInt("id"),
                        code = réponse.getString("code"),
                        nom = réponse.getString("nom"),
                        ville =
                        Ville(
                                id = réponse.getInt("villes.id"),
                                nom = réponse.getString("villes.nom"),
                                pays = réponse.getString("villes.pays"),
                                url_photo = réponse.getString("villes.url_photo")
                        ),
                        adresse = réponse.getString("addresse")
                )
        }
}
