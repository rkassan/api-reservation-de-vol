package dti.crosemont.reservationvol.Controleurs.Exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.UNAUTHORIZED)
class AccèsNonAutoriséException(message: String? = null, cause: Throwable? = null) : RuntimeException(message,cause){
}
