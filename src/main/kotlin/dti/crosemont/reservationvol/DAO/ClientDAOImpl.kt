package dti.crosemont.reservationvol
import org.springframework.stereotype.Repository
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.query
import dti.crosemont.reservationvol.Entites.Client
import java.sql.ResultSet

@Repository
class ClientDAOImpl( private val bd : JdbcTemplate ) : ClientDAO{
    companion object {
        private const val OBTENIR_TOUT_LES_CLIENTS : String = "SELECT * FROM clients;"
        private const val OBTENIR_CLIENT_PAR_ID : String = "SELECT * FROM clients WHERE id = ?;"
        private const val OBTENIR_CLIENT_PAR_MOT_CLÉ : String = 
            """
            SELECT * FROM clients WHERE prénom LIKE ? OR nom LIKE ?;
            """
    }

    override fun chercherTous(): List<Client> = 
        bd.query( OBTENIR_TOUT_LES_CLIENTS ) {réponse, _ -> convertirRésultatEnClient(réponse)}

    override fun chercherParId(id: Int): Client? = 
        bd.query( OBTENIR_CLIENT_PAR_ID, id ) 
            { réponse, _ -> convertirRésultatEnClient(réponse) }.singleOrNull()
    
    override fun chercherParMotCle(motClé: String): List<Client> =
        bd.query( OBTENIR_CLIENT_PAR_MOT_CLÉ, "$motClé%", "$motClé%" ) {réponse, _ -> convertirRésultatEnClient(réponse)}

    override fun effacer( id: Int ) { }

    private fun convertirRésultatEnClient( réponse : ResultSet ) : Client {
        return Client(
            id = réponse.getInt( "id" ) , 
            nom = réponse.getString( "nom" ), 
            prénom = réponse.getString( "prénom" ), 
            adresse = réponse.getString( "numéro_passeport" ), 
            numéroPasseport = réponse.getNString( "addresse" ), 
            email = réponse.getString( "email" ),
            numéroTéléphone = réponse.getString( "numéro_téléphone" )
        )
    }
}