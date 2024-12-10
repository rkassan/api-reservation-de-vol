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
import dti.crosemont.reservationvol.Domaine.OTD.ReservationOTD
import dti.crosemont.reservationvol.Domaine.OTD.PostReservationOTD
import dti.crosemont.reservationvol.Domaine.Service.ClientsService
import dti.crosemont.reservationvol.Controleurs.Exceptions.RéservationInexistanteException
import dti.crosemont.reservationvol.Controleurs.Exceptions.MessageErreur



@RestController
@RequestMapping("/reservations")
class ReservationControleur(val reservationsService: ReservationsService, val clientsService: ClientsService) {

    @GetMapping
        fun obtenirToutesLesReservations(): ResponseEntity<List<Reservation>> =
            ResponseEntity.ok(reservationsService.obtenirToutesLesReservations())


    @GetMapping("/{id}")
        fun obtenirReservationParId(@PathVariable id: Int): ResponseEntity<Any> {
            val reservation = reservationsService.obtenirReservationParId(id)
            return if (reservation != null) {
                ResponseEntity.ok(reservation)  
            } else {
                ResponseEntity.status(HttpStatus.NOT_FOUND).build()  
            }
        }

    @PostMapping
        fun ajouterReservation(@RequestBody reservationOTD: PostReservationOTD): ResponseEntity<Reservation> {
            val nouvelleReservation = reservationsService.ajouterReservation(reservationOTD)
        return ResponseEntity.ok(nouvelleReservation)
    }

    @PutMapping("/{id}")
    fun modifierReservation(@PathVariable id: Int, @RequestBody réservationOTD: ReservationOTD): ResponseEntity<Reservation> {

        return ResponseEntity.ok( reservationsService.modifierRéservation( id, réservationOTD ) )
    }

    @DeleteMapping("/{id}")
    fun supprimerReservation(@PathVariable id: Int): ResponseEntity<HttpStatus> {
            reservationsService.supprimerRéservation(id)    
            return ResponseEntity(HttpStatus.OK)
    }
}




