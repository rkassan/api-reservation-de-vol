package dti.crosemont.reservationvol
import org.springframework.stereotype.Repository
import org.springframework.jdbc.core.JdbcTemplate
import dti.crosemont.reservationvol.Entites.Client

@Repository
class ClientDAOImpl(private val bd : JdbcTemplate) : ClientDAO{
    const OBTENIR_TOUT_LES_CLIENTS : String = ""
    const OBTENIR_CLIENT_PAR_ID : String = "" 

    override fun chercherTous(): List<Client> = listOf()

    override fun chercherParId(id: Int): Client? = null

    override fun effacer(id: Int) { }
}