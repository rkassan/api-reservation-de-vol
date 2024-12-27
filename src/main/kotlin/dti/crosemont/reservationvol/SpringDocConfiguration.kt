package dti.crosemont.reservationvol

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SpringDocConfiguration {
    @Bean
    fun apiInfo(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("API du service Air Idéfix")
                    .description("gestion de vol réservation, trajet, aeroports, villes, recherche de vol et création de réservation")
                    .version("1.0.0")
            )
    }
}