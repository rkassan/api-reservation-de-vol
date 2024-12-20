package dti.crosemont.reservationvol.AccesAuxDonnees.BD

import dti.crosemont.reservationvol.AccesAuxDonnees.SourcesDeDonnees.VillesDAO
import org.springframework.stereotype.Repository
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.query
import dti.crosemont.reservationvol.Domaine.Modele.Ville
import java.sql.ResultSet



@Repository
class VilleDAOImpl(private val bd: JdbcTemplate): VillesDAO {

    override fun chercherTous(): List<Ville> {
      val query = "SELECT * FROM villes"
         return bd.query(query) { reponse, _ ->
            Ville(
                id = reponse.getInt("id"),
                nom = reponse.getString("nom"), 
                pays = reponse.getString("pays"),
                url_photo = reponse.getString("url_photo")            
                )
        }
    }

    override fun chercherParId(id: Int): Ville? {
       val query = "SELECT * FROM villes WHERE id = ?"
        return bd.query(query, arrayOf(id)) { reponse, _ ->
            Ville(
                id = reponse.getInt("id"),
                nom = reponse.getString("nom"), 
                pays = reponse.getString("pays"),
                url_photo = reponse.getString("url_photo")   
                )
        }.firstOrNull()
    }
   
    override fun ajouterVille(ville: Ville): Ville {
        val query = """
        INSERT INTO villes (id, nom, pays, url_photo)
        VALUES (?, ?, ?, ?)
        """
        bd.update(query, ville.id, ville.nom, ville.pays, ville.url_photo)
        return ville
    }

     override fun modifierVille(id: Int, modifieVille: Ville): Ville {
        val query = """
            UPDATE villes 
            SET nom = ?, pays = ?, url_photo = ? 
            WHERE id = ?
        """
        val modificationRows = bd.update(query, modifieVille.nom, modifieVille.pays, modifieVille.url_photo, id)
        return modifieVille 
    }


    override fun effacer(id: Int) {
        bd.update("DELETE FROM villes WHERE id = ?", id)
    }
    
    override fun chercherParNomEtPays(nom: String, pays:String): Ville? {
    val query = "SELECT * FROM villes WHERE nom = ? AND pays = ?"
        return bd.query(query,nom,pays) { reponse, _ ->
        Ville(
            id = reponse.getInt("id"),
            nom = reponse.getString("nom"),
            pays = reponse.getString("pays"),
            url_photo = reponse.getString("url_photo")
        )
        }.firstOrNull()
    }

 }
