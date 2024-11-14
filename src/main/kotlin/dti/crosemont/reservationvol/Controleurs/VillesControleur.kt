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
import dti.crosemont.reservationvol.VillesService
import dti.crosemont.reservationvol.Domaine.Modele.Ville

@RestController
@RequestMapping("/villes")
class VillesControleur(){

    @GetMapping
    fun obtenirToutesLesVilles(): ResponseEntity<List<Ville>> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    @PostMapping
    fun ajouterVille(@RequestBody ajouterVille: Ville): ResponseEntity<Ville> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    @PutMapping("/{id}")
    fun modifierVille(@PathVariable id: Int, @RequestBody modifieVille: Ville): ResponseEntity<Ville> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    @DeleteMapping("/{id}")
    fun supprimerVille(@PathVariable id: Int): ResponseEntity<HttpStatus> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }
}