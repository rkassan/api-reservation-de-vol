package dti.crosemont.brise_glace.Controleur.Exceptions

import java.time.LocalDateTime

data class MessageErreur(val code: Int, val date: LocalDateTime, val message: String?, val chemin: String)
