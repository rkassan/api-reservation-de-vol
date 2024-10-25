package dti.crosemont.reservationvol.Controleurs

import dti.crosemont.reservationvol.Entites.Client
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.DeleteMapping

@RestController
@RequestMapping("/clients")
class ClientsControleur {

    @GetMapping
    fun obtenirToutLesClients(@RequestParam("keyword") keyword : String?): ResponseEntity<List<Client>> =
            ResponseEntity(HttpStatus.NOT_IMPLEMENTED)`
    
    @GetMapping("/{id}")
    fun obtenirUnClientParId(@PathVariable id : Int): ResponseEntity<Client> =
            ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    
    @DeleteMapping("/{id}")
    fun supprimerUnClientParId(@PathVariable id : Int): ResponseEntity<HttpStatus> = 
            ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    
}
