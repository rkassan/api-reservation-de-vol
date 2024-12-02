package dti.crosemont.reservationvol.AccesAuxDonnees.BD

import dti.crosemont.reservationvol.AccesAuxDonnees.SourcesDeDonnees.ReservationsDAO
import dti.crosemont.reservationvol.Domaine.Modele.Reservation
import dti.crosemont.reservationvol.Domaine.Modele.Client
import dti.crosemont.reservationvol.Domaine.Modele.Siège
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class ReservationsDAOImpl(private val bd: JdbcTemplate): ReservationsDAO {

    //Chercher tous les reservations!!!
        override fun chercherTous(): List<Reservation> {
            val query = "SELECT * FROM réservations"
        
         return bd.query(query) { reponse, _ ->
                Reservation(
                    id = reponse.getInt("id"),
                    numéroRéservation = reponse.getString("numéro_réservation"),
                    idVol = reponse.getInt("id_vol"),
                    client = getClientForReservation(reponse.getInt("id")), 
                    siège = getSiègeForReservation(reponse.getInt("id")),  
                    classe = reponse.getString("classe"),
                    siegeSelectionne = reponse.getString("siège_selectionné"),
                    bagages = reponse.getInt("bagages")
                )
            }
        }
    

        fun verifierSiègeDisponible(idVol: Int, idSiège: Int): Boolean {
            val query = "SELECT statut_siege FROM vols_sièges WHERE vol_id = ? AND siège_id = ?"
                return bd.queryForObject(query, arrayOf(idVol, idSiège), String::class.java) == "disponible"
        }


        fun associerClientÀRéservation(client: Client, reservationId: Int) {
            val query = "INSERT INTO réservations (id_client, id) VALUES (?, ?)"
            bd.update(query, client.id, reservationId)
        }

        fun associerSiègeÀRéservation(idRéservation: Int, idSiège: Int) {
            val query = "INSERT INTO réservations (id_siège, id) VALUES (?, ?)"
            bd.update(query, idRéservation, idSiège)
        }

        fun mettreÀJourStatutSiège(idVol: Int, idSiège: Int, statut: String) {
            val query = "UPDATE vols_sièges SET statut_siege = ? WHERE vol_id = ? AND siège_id = ?"
            bd.update(query, statut, idVol, idSiège)
        }


    private fun getClientForReservation(reservationId: Int): Client {
        val query = """
            SELECT clients.* 
            FROM clients
            WHERE clients.id = (SELECT id_client FROM réservations WHERE id = ?)
        """
        return bd.queryForObject(query, arrayOf(reservationId)) { reponse, _ ->
            Client(
                id = reponse.getInt("id"),
                nom = reponse.getString("nom"),
                prénom = reponse.getString("prénom"),
                adresse = reponse.getString("addresse"),
                numéroPasseport = reponse.getString("numéro_passeport"),
                email = reponse.getString("email"),
                numéroTéléphone = reponse.getString("numéro_téléphone")
            )
        } ?: throw IllegalArgumentException("Client not found for reservation $reservationId")
    }


        private fun getSiègeForReservation(reservationId: Int): Siège {
            val query = """
                SELECT sièges.* 
                FROM sièges
                JOIN réservations_sièges ON sièges.id = réservations_sièges.id_siège
                WHERE réservations_sièges.id_réservation = ?
            """
                return bd.queryForObject(query, arrayOf(reservationId)) { reponse, _ ->
                Siège(
                    id = reponse.getInt("id"),
                    numéroSiège = reponse.getString("numéro_siège"),
                    classe = reponse.getString("classe")
                )
            } ?: throw IllegalArgumentException("Siège not found for reservation $reservationId")
        }


        override fun ajouterReservation(reservation: Reservation): Reservation {
            // Verifier si mon siege est dispo
            if (!verifierSiègeDisponible(reservation.idVol, reservation.siège.id)) {
                throw IllegalArgumentException("Le siège sélectionné n'est pas disponible.")
            }
            // Changer le status a occupe
            mettreÀJourStatutSiège(reservation.idVol, reservation.siège.id, "occupé")
            
            val query = """
            INSERT INTO réservations (numéro_réservation, id_vol, classe, siège_selectionné, bagages)
            VALUES (?, ?, ?, ?, ?)
            """
            val numeroReservation = java.util.UUID.randomUUID().toString()  // Generate a unique reservation number
            bd.update(query, numeroReservation, reservation.idVol, reservation.classe, reservation.siegeSelectionne, reservation.bagages)

            val reservationId = bd.queryForObject("SELECT LAST_INSERT_ID()", Int::class.java)
                ?: throw IllegalArgumentException("L'ID de la réservation est nul")

            //Assocition de clients a la Reservation 
            associerClientÀRéservation(reservation.client, reservationId)

            // Assocation du siegea la reservation
            associerSiègeÀRéservation(reservationId, reservation.siège.id)

            return chercherParId(reservationId)!!
        }


    

    override fun chercherParId(id: Int): Reservation? {
        val query = "SELECT * FROM réservations WHERE id = ?"
            return bd.query(query, arrayOf(id)) { reponse, _ ->
            Reservation(
                id = reponse.getInt("id"),
                numéroRéservation = reponse.getString("numéro_réservation"),
                idVol = reponse.getInt("id_vol"),
                client = getClientForReservation(reponse.getInt("id")), // single client
                siège = getSiègeForReservation(reponse.getInt("id")), // single seat
                classe = reponse.getString("classe"),
                siegeSelectionne = reponse.getString("siège_selectionné"),
                bagages = reponse.getInt("bagages")
                )
            }.firstOrNull()
        }


     override fun modifierRéservation(id: Int, réservation: Reservation): Reservation {
        bd.update(
            """
            UPDATE réservations SET
            numéro_réservation = ?,
            id_vol = ?,
            classe = ?,
            siège_selectionné = ?,
            bagages = ?
            WHERE id = ?
            """,
            réservation.numéroRéservation,
            réservation.idVol,
            réservation.classe,
            réservation.siegeSelectionne,
            réservation.bagages,
            id
        )
        return this.chercherParId(id)!!
    }

    override fun effacer(id: Int) {
        bd.update("DELETE FROM réservations WHERE id = ?", id)
    }
}