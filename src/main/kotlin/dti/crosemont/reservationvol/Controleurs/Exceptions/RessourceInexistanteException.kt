package dti.crosemont.reservationvol.Controleurs.Exceptions

import org.springframework.web.server.ResponseStatusException
import org.springframework.http.HttpStatus



class RessourceInexistanteException(message: String? = null, cause: Throwable? = null) : ResponseStatusException(HttpStatus.NOT_FOUND, message, cause) {
}
