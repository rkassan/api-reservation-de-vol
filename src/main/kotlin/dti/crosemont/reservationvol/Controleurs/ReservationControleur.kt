package dti.crosemont.reservationvol.Controleurs

import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.context.request.WebRequest
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
import dti.crosemont.reservationvol.Controleurs.Exceptions.RéservationInexistanteException
import dti.crosemont.reservationvol.Controleurs.Exceptions.MessageErreur



@RestController
@RequestMapping("/reservations")
class ReservationControleur(val reservationsService: ReservationsService) {

    @GetMapping
    fun obtenirToutesLesReservations(): ResponseEntity<List<Reservation>> =
        ResponseEntity.ok(reservationsService.obtenirToutesLesReservations())


    //Get d'un reservation mais avec un Exception de MessageErreur
    @GetMapping("/{id}")
    fun obtenirReservationParId(@PathVariable id: Int): ResponseEntity<Any> {
        val reservation = reservationsService.obtenirReservationParId(id)
            return if (reservation != null) {
                ResponseEntity.ok(reservation)
        }else {
        val errorMessage = MessageErreur(
            code = HttpStatus.NOT_FOUND.value(),
            date = LocalDateTime.now(),
            message = "La reservation avec Id $id introuvable.",
            chemin = "/reservations/$id"
        )
        ResponseEntity(errorMessage, HttpStatus.NOT_FOUND)
            }
        }
    
    
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
    fun supprimerReservation(@PathVariable numéroRéservation: Int): ResponseEntity<HttpStatus> {
            reservationsService.supprimerRéservation(numéroRéservation)    
            return ResponseEntity(HttpStatus.OK)
    }
}




