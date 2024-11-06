package dti.crosemont.reservationvol

import dti.crosemont.reservationvol.Entites.Reservation
import dti.crosemont.reservationvol.Entites.Client
import dti.crosemont.reservationvol.Entites.Siege
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.query
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository

@Repository
class ReservationDAOImpl(private val bd: JdbcTemplate): ReservationsDAO {


  override fun chercherTous(): List<Reservation> =
        //A faire
        
}