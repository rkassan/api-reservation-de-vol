package dti.crosemont.reservationvol.Controleurs


import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import dti.crosemont.reservationvol.ReservationsService
import dti.crosemont.reservationvol.Domaine.Modele.Reservation


@RestController
@RequestMapping("/reservations")
class ReservationControleur(val reservationsService: ReservationsService) {

    @GetMapping
    fun obtenirToutesLesReservations(): ResponseEntity<List<Reservation>> =
        ResponseEntity.ok(reservationsService.obtenirToutesLesReservations())


    @GetMapping("/{id}")
    fun obtenirReservationParId(@PathVariable id: Int): ResponseEntity<Reservation> {
        val reservation = reservationsService.obtenirReservationParId(id)
            return if (reservation != null) {
                ResponseEntity.ok(reservation)
        }else {
        ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }
    
    //Creation de reservation
    @PostMapping
    fun ajouterReservation(@RequestBody reservation: Reservation): ResponseEntity<Reservation> {
        return try {
            val ajouteReservation = reservationsService.ajouterReservation(reservation)    
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




