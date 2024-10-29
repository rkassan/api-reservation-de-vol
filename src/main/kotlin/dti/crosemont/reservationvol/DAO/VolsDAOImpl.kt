package dti.crosemont.reservationvol

import dti.crosemont.reservationvol.Entites.Aeroport
import dti.crosemont.reservationvol.Entites.Avion
import dti.crosemont.reservationvol.Entites.Ville
import dti.crosemont.reservationvol.Entites.Vol
import dti.crosemont.reservationvol.Entites.VolStatut
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.query
import org.springframework.stereotype.Repository

import kotlin.time.toDuration
import kotlin.time.DurationUnit


@Repository
class VolsDAOImpl(private val bd: JdbcTemplate) : VolsDAO {

        override fun chercherTous(): List<Vol> =
                bd.query(
                        "SELECT * FROM vols " +
                                "JOIN aéroports AS ap_deb ON vols.aéroport_debut = ap_deb.code " +
                                "JOIN aéroports AS ap_fin ON vols.aéroport_fin = ap_fin.code " +
                                "JOIN villes AS ville_debut ON ap_deb.ville_id = ville_debut.id " +
                                "JOIN villes AS ville_fin ON ap_fin.ville_id = ville_fin.id " +
                                "JOIN avions ON vols.avion_id = avions.id " +
                                "JOIN prix_par_classe ON vols.numéro_vol = prix_par_classe.numéro_vol "
                ) { réponse, _ ->
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
                                        réponse.getString("ap_deb.code"),
                                        réponse.getString("ap_deb.nom"),
                                        ville_debut,
                                        ville_debut.pays
                                )
                        var aéroport_fin =
                                Aeroport(
                                        réponse.getString("ap_fin.code"),
                                        réponse.getString("ap_fin.nom"),
                                        ville_fin,
                                        ville_fin.pays
                                )
                        var avion = Avion(réponse.getString("avions.type"), 0, 0, 0)

                        var prix_par_classe = hashMapOf<String, Double>()
                        prix_par_classe["économique"] =
                                réponse.getDouble("prix_par_classe.prix_économique")
                        prix_par_classe["affaire"] =
                                réponse.getDouble("prix_par_classe.prix_affaire")
                        prix_par_classe["première"] =
                                réponse.getDouble("prix_par_classe.prix_première")

                        val volStatuts = bd.query(
                                "SELECT * FROM vol_statut WHERE numero_vol = ?;", 
                                        réponse.getString("numéro_vol")
                                ) { réponseStatus, _ ->
                                        VolStatut(
                                                réponseStatus.getString("vol_statut.numero_vol"),
                                                réponseStatus.getString("vol_statut.status"),
                                                réponseStatus.getTimestamp("vol_statut.heure").toLocalDateTime().toLocalTime()
                                        )
                                }
                                
                        Vol(
                                réponse.getString("numéro_vol"),
                                aéroport_debut,
                                aéroport_fin,
                                réponse.getTimestamp("date_départ").toLocalDateTime(),
                                réponse.getTimestamp("date_arrivée").toLocalDateTime(),
                                avion,
                                prix_par_classe,
                                réponse.getInt("poids_max_bag"),
                                volStatuts,
                                réponse.getInt("durée").toDuration(DurationUnit.NANOSECONDS)
                        )
                }
}