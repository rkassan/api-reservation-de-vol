package dti.crosemont.reservationvol.Controleurs

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ApiControleur {
    @GetMapping("/")
    fun index() = "Service web de l'API Air Idefix"
}