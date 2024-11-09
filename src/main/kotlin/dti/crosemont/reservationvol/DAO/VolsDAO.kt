package dti.crosemont.reservationvol

import dti.crosemont.reservationvol.Entites.Vol

interface VolsDAO: DAO<Vol>{
    override fun chercherTous() : List<Vol>
}