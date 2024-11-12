package dti.crosemont.reservationvol.Controleurs

import dti.crosemont.reservationvol.Entites.Vol
import dti.crosemont.reservationvol.VolsDAOImpl
import java.time.LocalDateTime
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import dti.crosemont.reservationvol.Service.VolService
import kotlin.time.Duration
import kotlin.time.minutes




@RestController
@RequestMapping("/vols")
class VolsControleur(private val volService: VolService) {

    @GetMapping
    fun obtenirToutLesVols(): ResponseEntity<List<Vol>> =
            ResponseEntity(volService.chercherTous(), HttpStatus.OK)

    @GetMapping("/{id}")
    fun obtenirVolParId(@PathVariable id: Int): ResponseEntity<Vol> =
            ResponseEntity(volService.chercherParId(id), HttpStatus.OK)

    @GetMapping( params = ["dateDebut", "aeroportDebut", "aeroportFin"])
            fun obtenirVolParParam(
                @RequestParam dateDebut: LocalDateTime,
                @RequestParam aeroportDebut: String,
                @RequestParam aeroportFin: String
            ): ResponseEntity<List<Vol>> {
                
                
                val vols = volService.obtenirVolParParam(dateDebut, aeroportDebut, aeroportFin)
        
                return if (vols.isNotEmpty()) {
                    ResponseEntity(vols, HttpStatus.OK)
                } else {
                    ResponseEntity(HttpStatus.NOT_FOUND)
                }
            }

            @PostMapping
            fun ajoutervol(@RequestBody vol: Vol): ResponseEntity<Vol> {
                return volService.ajouterVol(vol)
            }

    @PutMapping("/{id}")
    fun modifierVol(
            @PathVariable numeroVol: String,
            @RequestBody modifieVol: Vol
    ): ResponseEntity<Vol> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    @DeleteMapping("/{id}")
    fun supprimerVolParId(@PathVariable id: Int): ResponseEntity<HttpStatus> {
        ResponseEntity(volService.effacer(id), HttpStatus.ACCEPTED)
        return ResponseEntity.noContent().build()
    }
}
