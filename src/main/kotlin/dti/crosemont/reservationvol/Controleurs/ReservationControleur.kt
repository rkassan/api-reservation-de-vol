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
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.core.annotation.AuthenticationPrincipal
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
    fun obtenirToutesLesReservations(@AuthenticationPrincipal principal : Jwt): ResponseEntity<List<Reservation>> {
        val listePermissions = principal.claims["permissions"] as? List<String>
        val courrielAuthentification = principal.claims["courriel"] as String? ?: ""

        return ResponseEntity.ok(reservationsService.obtenirToutesLesReservations(listePermissions, courrielAuthentification))
    }


    @GetMapping("/{id}")
    fun obtenirReservationParId(@PathVariable id: Int): ResponseEntity<Any> {
        return ResponseEntity.ok(reservationsService.obtenirReservationParId(id))  
    }

    @PostMapping
    fun ajouterReservation(@RequestBody reservationOTD: PostReservationOTD): ResponseEntity<Reservation> {
        return ResponseEntity.ok(reservationsService.ajouterReservation(reservationOTD))
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




