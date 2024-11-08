package dti.crosemont.reservationvol

import dti.crosemont.reservationvol.Entites.Aeroport
import dti.crosemont.reservationvol.Entites.Avion
import dti.crosemont.reservationvol.Entites.Trajet
import dti.crosemont.reservationvol.Entites.Ville
import dti.crosemont.reservationvol.Entites.Vol
import dti.crosemont.reservationvol.Entites.VolStatut
import java.sql.ResultSet
import kotlin.time.DurationUnit
import kotlin.time.toDuration
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.query
import org.springframework.stereotype.Repository

@Repository
class VolsDAOImpl(private val bd: JdbcTemplate) : VolsDAO {

        companion object {
                private const val QUERY_TOUT_LES_VOLS =
                        """
                        SELECT * FROM vols 
                        JOIN trajets ON vols.trajet_id = trajets.id 
                        JOIN aéroports AS ap_deb ON trajets.id_aéroport_debut = ap_deb.id 
                        JOIN aéroports AS ap_fin ON trajets.id_aéroport_fin = ap_fin.id 
                        JOIN villes AS ville_debut ON ap_deb.ville_id = ville_debut.id 
                        JOIN villes AS ville_fin ON ap_fin.ville_id = ville_fin.id 
                        JOIN prix_par_classe ON vols.id = prix_par_classe.id_vol 
                        JOIN avions ON vols.avion_id = avions.id 
                        ORDER BY vols.id;
                        """

                private const val QUERY_VOL_PAR_ID =
                        """
                        SELECT * FROM vols 
                        JOIN trajets ON vols.trajet_id = trajets.id 
                        JOIN aéroports AS ap_deb ON trajets.id_aéroport_debut = ap_deb.id 
                        JOIN aéroports AS ap_fin ON trajets.id_aéroport_fin = ap_fin.id 
                        JOIN villes AS ville_debut ON ap_deb.ville_id = ville_debut.id 
                        JOIN villes AS ville_fin ON ap_fin.ville_id = ville_fin.id 
                        JOIN prix_par_classe ON vols.id = prix_par_classe.id_vol 
                        JOIN avions ON vols.avion_id = avions.id 
                        WHERE vols.id = ? 
                        ORDER BY vols.id;
                        """
        }
        private fun mapVol(réponse: ResultSet): Vol {
                var ville_debut =
                        Ville(
                                réponse.getInt("ville_debut.id"),
                                réponse.getString("ville_debut.nom"),
                                réponse.getString("ville_debut.pays"),
                                réponse.getString("ville_debut.url_photo")
                        )
                var ville_fin =
                        Ville(
                                réponse.getInt("ville_fin.id"),
                                réponse.getString("ville_fin.nom"),
                                réponse.getString("ville_fin.pays"),
                                réponse.getString("ville_fin.url_photo")
                        )
                var aéroport_debut =
                        Aeroport(
                                réponse.getInt("ap_deb.id"),
                                réponse.getString("ap_deb.code"),
                                réponse.getString("ap_deb.nom"),
                                ville_debut,
                                réponse.getString("ap_deb.addresse")
                        )
                var aéroport_fin =
                        Aeroport(
                                réponse.getInt("ap_fin.id"),
                                réponse.getString("ap_fin.code"),
                                réponse.getString("ap_fin.nom"),
                                ville_fin,
                                réponse.getString("ap_fin.addresse")
                        )

                var avion = Avion(réponse.getInt("avions.id"), réponse.getString("avions.type"))

                var trajet =
                        Trajet(
                                réponse.getInt("trajets.id"),
                                réponse.getString("trajets.numéro_trajet"),
                                aéroport_debut,
                                aéroport_fin
                        )

                var prix_par_classe = hashMapOf<String, Double>()
                prix_par_classe["économique"] = réponse.getDouble("prix_par_classe.prix_économique")
                prix_par_classe["affaire"] = réponse.getDouble("prix_par_classe.prix_affaire")
                prix_par_classe["première"] = réponse.getDouble("prix_par_classe.prix_première")

                val volStatuts =
                        bd.query(
                                "SELECT * FROM vol_statut WHERE id_vol = ?;",
                                réponse.getInt("id_vol")
                        ) { réponseStatut, _ ->
                                VolStatut(
                                        réponseStatut.getInt("vol_statut.id_vol"),
                                        réponseStatut.getString("vol_statut.statut"),
                                        réponseStatut
                                                .getTimestamp("vol_statut.heure")
                                                .toLocalDateTime()
                                                .toLocalTime()
                                )
                        }
                return Vol(
                        réponse.getInt("id"),
                        réponse.getTimestamp("date_départ").toLocalDateTime(),
                        réponse.getTimestamp("date_arrivée").toLocalDateTime(),
                        avion,
                        prix_par_classe,
                        réponse.getInt("poids_max_bag"),
                        trajet,
                        volStatuts,
                        réponse.getInt("durée").toDuration(DurationUnit.NANOSECONDS)
                )
        }

        override fun chercherTous(): List<Vol> =
                bd.query(QUERY_TOUT_LES_VOLS) { réponse, _ -> mapVol(réponse) }

        override fun chercherParId(id: Int): Vol? =
                bd.query(QUERY_VOL_PAR_ID, id) { réponse, _ -> mapVol(réponse) }.singleOrNull()

        override fun effacer(id: Int) {
                //bd.update("DELETE FROM vols WHERE id = ?", id)
        }
}
