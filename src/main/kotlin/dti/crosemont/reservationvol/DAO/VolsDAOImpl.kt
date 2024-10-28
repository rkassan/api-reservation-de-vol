package dti.crosemont.reservationvol

import org.springframework.stereotype.Repository
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.query
import dti.crosemont.reservationvol.Entites.Vol

/* 
@Repository
class VolsDAOImpl(private val bd: JdbcTemplate): VolsDAO{

    override fun chercherTous(): List<Vol> = bd.query("select * from vols") { réponse, _ ->



        Vol(réponse.getString("numéro_vol"), réponse.getString("aéroport_debut"))
    }



}*/