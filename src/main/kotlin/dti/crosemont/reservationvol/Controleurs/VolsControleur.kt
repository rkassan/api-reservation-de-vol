package dti.crosemont.reservationvol

import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import dti.crosemont.reservationvol.Entites.Vol
import java.time.LocalDateTime

@RestController
@RequestMapping("/vols")
class VolsControleur{
  
    @GetMapping
    fun obtenirToutLesVols(): ResponseEntity<List<Vol>> = ResponseEntity(HttpStatus.NOT_IMPLEMENTED)

    @GetMapping("/{numéro_vol}")
    fun obtenirVolParNumero(@PathVariable numeroVol: String) : ResponseEntity<Vol>{
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    @GetMapping("/")
    fun obtenirVolParParam(@RequestParam dateDebut: LocalDateTime, @RequestParam aeroportDebut: String, @RequestParam aeroportFin: String) : ResponseEntity<List<Vol>>{
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    @PostMapping
    fun ajoutervol(@RequestBody vol: Vol): ResponseEntity<Vol> {
        vols.add(vol)
        return ResponseEntity.status(HttpStatus.CREATED).body(vol)
    }

    @PutMapping("/{numeroVol}")
    fun modifierVol(@PathVariable numeroVol: String, @RequestBody modifieVol: Vol): ResponseEntity<Vol> {
    val volamodifier = vols.find { it.numeroVol == numeroVol }
    return if (volamodifier != null) {
        val index = vols.indexOf(volamodifier)
        vols[index] = modifieVol
        ResponseEntity.ok(modifieVol)
    } else {
        ResponseEntity(HttpStatus.NOT_FOUND)
    }
}

    @DeleteMapping("/{numeroVol}")
    fun supprimeVol(@PathVariable numeroVol: String): ResponseEntity<Void> {
        val volsupprime = vols.find { it.numeroVol == numeroVol }
        return if (volsupprime  != null) {
            vols.remove(volsupprime )
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }


}


