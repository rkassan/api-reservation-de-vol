package dti.crosemont.reservationvol.AccesAuxDonnees.BD

import dti.crosemont.reservationvol.AccesAuxDonnees.SourcesDeDonnees.ReservationsDAO
import dti.crosemont.reservationvol.Domaine.Modele.Reservation
import dti.crosemont.reservationvol.Domaine.Modele.Client
import dti.crosemont.reservationvol.Domaine.Modele.Siège
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class ReservationsDAOImpl(private val bd: JdbcTemplate): ReservationsDAO {

    override fun chercherTous(): List<Reservation> {
        val query = "SELECT * FROM réservations"
        
        return bd.query(query) { reponse, _ ->
            Reservation(
                id = reponse.getInt("id"),
                numéroRéservation = reponse.getString("numéro_réservation"),
                idVol = reponse.getInt("id_vol"),
                clients = getClientsForReservation(reponse.getInt("id")), 
                sièges = getSiègesForReservation(reponse.getInt("id")),  
                classe = reponse.getString("classe"),
                siegeSelectionne = reponse.getString("siège_selectionné"),
                bagages = reponse.getInt("bagages")
            )
        }
    }

    private fun getClientsForReservation(reservationId: Int): List<Client> {
        val query = """
            SELECT clients.*
            FROM clients
            JOIN réservations_clients ON clients.id = réservations_clients.id_clients
            WHERE réservations_clients.id_réservation = ?
        """
        return bd.query(query, arrayOf(reservationId)) { reponse, _ ->
            Client(
                id = reponse.getInt("id"),
                nom = reponse.getString("nom"),
                prénom = reponse.getString("prénom"),
                adresse = reponse.getString("addresse"),
                numéroPasseport = reponse.getString("numéro_passeport"),
                email = reponse.getString("email"),
                numéroTéléphone = reponse.getString("numéro_téléphone")
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
        return bd.query(query, arrayOf(reservationId)) { reponse, _ ->
            Siège(
                id = reponse.getInt("id"),
                numéroSiège = reponse.getString("numéro_siège"),
                classe = reponse.getString("classe")
            )
        }
    }

    override fun ajouterReservation(reservation: Reservation): Reservation {
    val query = """
        INSERT INTO réservations (numéro_réservation, id_vol, classe, siège_selectionné, bagages)
        VALUES (?, ?, ?, ?, ?)
    """
    bd.update(query, reservation.numéroRéservation, reservation.idVol, reservation.classe, reservation.siegeSelectionne, reservation.bagages)

    return reservation
}

//Ajoute de chercherParID
override fun chercherParId(id: Int): Reservation? {
    val query = "SELECT * FROM réservations WHERE id = ?"

    return bd.query(query, arrayOf(id)) { reponse, _ ->
        Reservation(
            id = reponse.getInt("id"),
            numéroRéservation = reponse.getString("numéro_réservation"),
            idVol = reponse.getInt("id_vol"),
            clients = getClientsForReservation(reponse.getInt("id")), 
            sièges = getSiègesForReservation(reponse.getInt("id")),  
            classe = reponse.getString("classe"),
            siegeSelectionne = reponse.getString("siège_selectionné"),
            bagages = reponse.getInt("bagages")
        )
    }.firstOrNull()
}

    override fun effacer(id: Int) {
        bd.update("DELETE FROM réservations WHERE id = ?", id)
    }
}