package dti.crosemont.reservationvol.AccesAuxDonnees.SourcesDeDonnees

interface DAO<T>{
    fun chercherTous(): List<T>
    fun chercherParId(id: Int): T?
    fun effacer(id: Int)
}    