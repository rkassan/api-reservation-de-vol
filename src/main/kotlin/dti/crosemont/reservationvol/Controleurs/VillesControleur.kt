package dti.crosemont.reservationvol.Controleurs


import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import dti.crosemont.reservationvol.Domaine.Service.VillesService
import dti.crosemont.reservationvol.Domaine.Modele.Ville

@RestController
@RequestMapping("/villes")
class VillesControleur(val villesService: VillesService){

    @GetMapping
    fun obtenirToutesLesVilles(): ResponseEntity<List<Ville>> {
        return ResponseEntity(villesService.obtenirToutesLesVilles(),HttpStatus.OK)
    }

     @GetMapping("/{id}")
        fun obtenirVilleParId(@PathVariable id: Int) : ResponseEntity<Ville> =
                ResponseEntity.ok(villesService.obtenirVilleParId(id))


    @PostMapping
    fun ajouterVille(@RequestBody ville: Ville): ResponseEntity<Ville>  =
                ResponseEntity.ok(villesService.ajouterVille(ville))


    @PutMapping("/{id}")
    fun modifierVille(@PathVariable id: Int, @RequestBody modifieVille: Ville): ResponseEntity<Ville> {
        return villesService.modifierVille(id, modifieVille)
    }

    @DeleteMapping("/{id}")
    fun supprimerVilleParId(@PathVariable id: Int): ResponseEntity<HttpStatus> {
        ResponseEntity(villesService.effacerVille(id), HttpStatus.ACCEPTED)
        return ResponseEntity.noContent().build()

    }
}