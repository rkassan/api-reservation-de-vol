package dti.crosemont.reservationvol.Controleurs


import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import dti.crosemont.reservationvol.Entites.Reservation
import dti.crosemont.reservationvol.ReservationsDAOImpl
import java.time.LocalDateTime

@RestController
@RequestMapping("/reservations")
class ReservationControleur(private val dao: ReservationsDAOImpl) {

    @GetMapping
    fun obtenirToutesLesReservations(): ResponseEntity<List<Reservation>> {
        val reservations = dao.chercherTous()
        return ResponseEntity(reservations, HttpStatus.OK)
    }


    @GetMapping("/{numéroRéservation}")
    fun obtenirReservationParNumero(@PathVariable numéroRéservation: String): ResponseEntity<Reservation> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }
    
//Creation de reservation
    @PostMapping
    fun ajouterReservation(@RequestBody reservation: Reservation): ResponseEntity<Reservation> {
       return try {
            val ajouteReservation = dao.ajouterReservation(reservation)    
            ResponseEntity(ajouteReservation, HttpStatus.CREATED)
        } catch (ex: Exception) {
            ex.printStackTrace()
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PutMapping("/{numéroRéservation}")
    fun modifierReservation(@PathVariable numéroRéservation: String, @RequestBody modifieReservation: Reservation): ResponseEntity<Reservation> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    @DeleteMapping("/{numéroRéservation}")
    fun supprimerReservation(@PathVariable numéroRéservation: String): ResponseEntity<HttpStatus> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }
}




