package dti.crosemont.reservationvol

interface DAO<T>{
    fun chercherTous(): List<T>

}