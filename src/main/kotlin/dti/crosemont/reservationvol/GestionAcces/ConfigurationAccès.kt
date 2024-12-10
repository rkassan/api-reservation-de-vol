package dti.crosemont.reservationvol.GestionAcces

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.config.Customizer.withDefaults


@Configuration
@EnableMethodSecurity(prePostEnabled = true)
class ConfigurationAccès {

    @Bean
    @Throws(Exception::class)
    fun configurerChaineAccès(http: HttpSecurity): SecurityFilterChain {
        return http
            .authorizeHttpRequests {
                it.requestMatchers("/").permitAll()
                    .requestMatchers( HttpMethod.GET, "/aeroports" ).permitAll()
                    .requestMatchers( HttpMethod.GET, "/vols/**" ).permitAll()
                    .anyRequest().authenticated()
            }
            .cors(withDefaults())
            .oauth2ResourceServer { oauth2 ->
                oauth2.jwt(withDefaults())
            }
            .build()
    }
}