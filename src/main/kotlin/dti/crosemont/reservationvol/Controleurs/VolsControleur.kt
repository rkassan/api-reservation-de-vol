package dti.crosemont.reservationvol

import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import dti.crosemont.reservationvol.Entites.Vol

@RestController
@RequestMapping("/vols")
class VolsControleur{
    @GetMapping
    fun obtenirToutLesVols(): ResponseEntity<List<Vol>> = ResponseEntity(HttpStatus.NOT_IMPLEMENTED)

    @GetMapping("/{numéro_vol}")
    fun obtenirVolParNumero(@PathVariable numeroVol: String) = volsService.obtenirVolParNumero(numeroVol)
    return if vol!=null else ResponseEntity(HttpStatus.NOT_FOUND)  //retourne description vol 
}
