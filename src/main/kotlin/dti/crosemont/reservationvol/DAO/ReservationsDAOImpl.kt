package dti.crosemont.reservationvol

import dti.crosemont.reservationvol.Entites.Reservation
import dti.crosemont.reservationvol.Entites.Client
import dti.crosemont.reservationvol.Entites.Siege
import dti.crosemont.reservationvol.Entites.Vol
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.query
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository

@Repository
class ReservationsDAOImpl(private val bd: JdbcTemplate): ReservationsDAO {


  override fun chercherTous(): List<Reservation> =
       
        
}