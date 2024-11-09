package dti.crosemont.reservationvol

import dti.crosemont.reservationvol.Entites.Reservation
import dti.crosemont.reservationvol.Entites.Client
import dti.crosemont.reservationvol.Entites.Siège
import dti.crosemont.reservationvol.Entites.Vol
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.query
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository

@Repository
class ReservationsDAOImpl(private val bd: JdbcTemplate): ReservationsDAO {

     override fun chercherTous(): List<Reservation> {
        val query = "SELECT * FROM réservations"
        
        return bd.query(query) { rs, _ ->
            Reservation(
                id = rs.getInt("id"),
                numéroRéservation = rs.getString("numéro_réservation"),
                idVol = rs.getInt("id_vol"),
                clients = getClientsForReservation(rs.getInt("id")), 
                sièges = getSiègesForReservation(rs.getInt("id")),   
                classe = rs.getString("classe"),
                siegeSelectionne = rs.getString("siège_selectionné"), 
                bagages = rs.getInt("bagages") 
            )
        }
    }

    private fun getClientsForReservation(reservationId: Int): List<Client> {
        val query = "SELECT * FROM clients WHERE reservation_id = ?"
        return bd.query(query, arrayOf(reservationId)) { rs, _ ->
            Client(
                id = rs.getInt("id"),
                nom = rs.getString("nom"),
                prénom = rs.getString("prénom"),
                adresse = rs.getString("adresse"),
                numéroPasseport = rs.getString("numéro_passeport"),
                email = rs.getString("email"),
                numéroTéléphone = rs.getString("numéro_téléphone")
            )
        }
    }

    private fun getSiègesForReservation(reservationId: Int): List<Siège> {
        val query = """
            SELECT sièges.* 
            FROM sièges
            JOIN réservations_sièges ON sièges.id = réservations_sièges.id_siège
            WHERE réservations_sièges.id_réservation = ?
        """
        return bd.query(query, arrayOf(reservationId)) { rs, _ ->
            Siège(
                id = rs.getInt("id"),
                numéroSiège = rs.getString("numéro_siège"),
                classe = rs.getString("classe")
            )
        }
    }
}