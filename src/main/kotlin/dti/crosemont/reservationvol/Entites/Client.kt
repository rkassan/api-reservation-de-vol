package dti.crosemont.reservationvol.Entites

import jakarta.persistence.*

@Entity
class Client(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Int
)