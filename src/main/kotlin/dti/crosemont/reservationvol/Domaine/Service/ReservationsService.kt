package dti.crosemont.reservationvol

import org.springframework.stereotype.Service
import dti.crosemont.reservationvol.Domaine.Modele.Reservation
import dti.crosemont.reservationvol.Domaine.Modele.Siège
import dti.crosemont.reservationvol.Domaine.Modele.Vol
import dti.crosemont.reservationvol.Domaine.Modele.Client 
import dti.crosemont.reservationvol.AccesAuxDonnees.BD.ReservationsDAOImpl
import dti.crosemont.reservationvol.AccesAuxDonnees.SourcesDeDonnees.SiègeDAO
import dti.crosemont.reservationvol.Domaine.Service.VolService


import dti.crosemont.reservationvol.Controleurs.Exceptions.RéservationInexistanteException

@Service
class ReservationsService(  private val reservationsDAO: ReservationsDAOImpl,
                            private val siegeDAO: SiègeDAO, 
                            private val volService: VolService) {


    fun obtenirToutesLesReservations(): List<Reservation> = reservationsDAO.chercherTous()

    // --------------------changer ici pour associe la reservation avec le vol, client  et siege--------------------------------
    fun ajouterReservation(reservation: Reservation): Reservation {
        // Vérification si le vol existe
        val vol = volService.chercherParId(reservation.idVol)
            ?: throw IllegalArgumentException("Vol avec l'ID ${reservation.idVol} introuvable.")
    
        // ici cest pour avoir  les sièges du vol
        val sièges = volService.chercherSiegeParVolId(reservation.idVol)

        // Trouver le siège sélectionné dans la liste
        val siègeSélectionné = sièges.find { it.numéroSiège == reservation.siegeSelectionne }
            ?: throw IllegalArgumentException("Le siège ${reservation.siegeSelectionne} n'est pas disponible pour ce vol.")

        // Vérification si le siège est occupé
        if (siègeSélectionné.statut == "occupé") {
            throw IllegalArgumentException("Le siège ${reservation.siegeSelectionne} est déjà réservé.")
         }

        
        val nouvelleRéservation = reservationsDAO.ajouterReservation(reservation)

        // mise a joiur statut du siège "occupé"
        siègeSélectionné.statut = "occupé"
        siegeDAO.save(siègeSélectionné)

        // Associer le siège sélectionné a la réservation dans la table réservations_sièges
        val siègeId = sièges.find { it.numéroSiège == reservation.siegeSelectionne }?.id
         ?: throw IllegalArgumentException("Le siège ${reservation.siegeSelectionne} n'existe pas.")
    

        return nouvelleRéservation
}



    fun obtenirReservationParId(id: Int): Reservation? {
        return reservationsDAO.chercherParId(id)
    }
    
    fun modifierRéservation( id: Int, réservation: Reservation ): Reservation {
        val réservationVérification = this.obtenirReservationParId(id)
        
        if ( réservationVérification != null ) {
            return reservationsDAO.modifierRéservation(id, réservation)
        } else {
            throw RéservationInexistanteException("Réservation avec le id: $id est inexistante")
        }
    }

    fun supprimerRéservation(id: Int) {
        val réservation = this.obtenirReservationParId(id)
        
        if ( réservation != null ) {
            reservationsDAO.effacer(id)
        } else {
            throw RéservationInexistanteException("Réservation avec le id: $id est inexistante")
        }
    }
}