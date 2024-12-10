
package dti.crosemont.reservationvol.AccesAuxDonnees.SourcesDeDonnees

import dti.crosemont.reservationvol.Domaine.Modele.Siège


interface SiègeDAO : DAO<Siège> {
    fun save(siege: Siège): Siège
}