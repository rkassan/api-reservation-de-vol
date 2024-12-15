package dti.crosemont.reservationvol.Controleurs

import dti.crosemont.reservationvol.Domaine.Modele.`Siège`
import dti.crosemont.reservationvol.Domaine.Modele.Vol
import dti.crosemont.reservationvol.Domaine.OTD.VolOTD
import dti.crosemont.reservationvol.Domaine.Service.VolService
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
import org.springframework.security.core.context.SecurityContextHolder

@RestController
@RequestMapping("/vols")
class VolsControleur(private val volService: VolService) {

    @GetMapping
    fun obtenirToutLesVols(): ResponseEntity<List<Vol>> =
            ResponseEntity(volService.chercherTous(), HttpStatus.OK)

    @GetMapping("/{id}")
    fun obtenirVolParId(@PathVariable id: Int): ResponseEntity<Vol> =
            ResponseEntity(volService.chercherParId(id), HttpStatus.OK)

    @GetMapping(params = ["dateDebut", "aeroportDebut", "aeroportFin"])
    fun obtenirVolParParam(
            @RequestParam dateDebut: LocalDateTime,
            @RequestParam aeroportDebut: String,
            @RequestParam aeroportFin: String
    ): ResponseEntity<List<Vol>> {

        val vols = volService.obtenirVolParParam(dateDebut, aeroportDebut, aeroportFin)

        return ResponseEntity(vols, HttpStatus.OK)

    }

    @GetMapping("/{id}/sièges")
    fun obtenirSiegeParVolId(@PathVariable id: Int): ResponseEntity<List<Siège>> =
            ResponseEntity(volService.chercherSiegeParVolId(id), HttpStatus.OK)

    @PostMapping
    fun ajouterVol(@RequestBody vol: Vol): ResponseEntity<Vol> {
        val nouveauVol = volService.ajouterVol(vol)
        return ResponseEntity(nouveauVol, HttpStatus.CREATED)
    }


    @PutMapping("/{id}")
fun modifierVol(@PathVariable id: Int, @RequestBody modifieVol: VolOTD): ResponseEntity<Vol> {
    return ResponseEntity.ok(volService.modifierVol(id,modifieVol))
}



    @DeleteMapping("/{id}")
    fun supprimerVolParId(@PathVariable id: Int): ResponseEntity<HttpStatus> {
        ResponseEntity(volService.effacer(id), HttpStatus.ACCEPTED)
        return ResponseEntity.noContent().build()
    }
}
