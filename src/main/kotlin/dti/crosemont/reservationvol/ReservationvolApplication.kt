package dti.crosemont.reservationvol

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
class ReservationvolApplication

fun main(args: Array<String>) {
	runApplication<ReservationvolApplication>(*args)
}
