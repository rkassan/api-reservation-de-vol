package dti.crosemont.reservationvol.Controleurs.Exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class RéservationInexistanteException(message: String? = null, cause: Throwable? = null) : RuntimeException(message, cause) {
}