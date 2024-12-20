package dti.crosemont.reservationvol.Controleurs.Exceptions

import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.servlet.resource.NoResourceFoundException
import org.springframework.web.context.request.WebRequest
import org.springframework.http.HttpStatus
import java.time.LocalDateTime

@RestControllerAdvice
class GestionnaireExceptionsControleur{

    @ExceptionHandler(RessourceInexistanteException::class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    fun gérerRessourceInexistanteException(exception : RessourceInexistanteException, requête : WebRequest) : MessageErreur =
        MessageErreur(HttpStatus.NOT_FOUND.value(), LocalDateTime.now(), exception.message, requête.getDescription(false))

    @ExceptionHandler(RequêteMalFormuléeException::class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    fun gérerRequêteMalFormuléeException(exception: RequêteMalFormuléeException, requête: WebRequest): MessageErreur =
            MessageErreur(HttpStatus.BAD_REQUEST.value(), LocalDateTime.now(), exception.message, requête.getDescription(false))

    @ExceptionHandler(ModificationException::class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    fun ModificationException(exception: ModificationException, requête: WebRequest): MessageErreur =
            MessageErreur(HttpStatus.BAD_REQUEST.value(), LocalDateTime.now(), exception.message, requête.getDescription(false))
        
    @ExceptionHandler(RéservationInexistanteException::class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    fun RéservationInexistanteException(exception : RéservationInexistanteException, requête : WebRequest) : MessageErreur =
            MessageErreur(HttpStatus.NOT_FOUND.value(), LocalDateTime.now(), exception.message, requête.getDescription(false))

    @ExceptionHandler(AccèsNonAutoriséException::class)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    fun gérerAccèsNonAutoriséException(exception: AccèsNonAutoriséException, requête: WebRequest): MessageErreur =
        MessageErreur(HttpStatus.UNAUTHORIZED.value(), LocalDateTime.now(), exception.message, requête.getDescription(false))

   @ExceptionHandler(AccèsRefuséException::class)
     @ResponseStatus(code = HttpStatus.FORBIDDEN)
    fun gérerAccèsRefuséException(exception: AccèsRefuséException, requête: WebRequest): MessageErreur =
        MessageErreur(HttpStatus.FORBIDDEN.value(), LocalDateTime.now(), exception.message, requête.getDescription(false))      

    @ExceptionHandler(NombreDeBagageInvalide::class) 
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    fun gérerNombreDeBagageInvalide(exception: NombreDeBagageInvalide, requête: WebRequest): MessageErreur =
        MessageErreur(HttpStatus.BAD_REQUEST.value(), LocalDateTime.now(), exception.message, requête.getDescription(false))

    @ExceptionHandler(RessourceEexistanteException::class) 
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    fun gérerRessourceEexistanteException(exception: RessourceEexistanteException, requête: WebRequest): MessageErreur =
        MessageErreur(HttpStatus.BAD_REQUEST.value(), LocalDateTime.now(), exception.message, requête.getDescription(false))
}