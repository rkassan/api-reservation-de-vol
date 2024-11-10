package dti.crosemont.brise_glace.Controleur.Exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class RequêteMalFormuléeException(message: String? = null, cause: Throwable? = null) : RuntimeException(message, cause) {
}
