package dti.crosemont.reservationvol.Controleurs.Exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

class AccèsRefuséException(message: String? = null, cause: Throwable? = null) : RuntimeException(message)
