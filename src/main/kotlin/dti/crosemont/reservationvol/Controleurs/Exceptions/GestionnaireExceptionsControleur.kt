package dti.crosemont.reservationvol.Controleurs.Exceptions

import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.servlet.resource.NoResourceFoundException
import org.springframework.web.context.request.WebRequest
import org.springframework.http.HttpStatus
import dti.crosemont.brise_glace.Controleur.Exceptions.MessageErreur
import java.time.LocalDateTime

@RestControllerAdvice
class GestionnaireExceptionsControleur{

    @ExceptionHandler(NoResourceFoundException::class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    fun gérerRessourceInexistanteException(exception : NoResourceFoundException, requête : WebRequest) : MessageErreur = 
        MessageErreur(HttpStatus.NOT_FOUND.value(), LocalDateTime.now(), exception.message, requête.getDescription(false))

    @ExceptionHandler(RequêteMalFormuléeException::class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    fun gérerRequêteMalFormuléeException(exception: RequêteMalFormuléeException, requête: WebRequest): MessageErreur =
            MessageErreur(HttpStatus.BAD_REQUEST.value(), LocalDateTime.now(), exception.message, requête.getDescription(false))
    
}