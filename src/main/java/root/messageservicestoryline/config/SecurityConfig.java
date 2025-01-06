package root.messageservicestoryline.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain mySpringSecurityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(auth -> {
            auth.requestMatchers("/").permitAll();
            auth.anyRequest().authenticated();
        }).oauth2Login(withDefaults())
                .httpBasic(withDefaults())
                .formLogin(withDefaults())
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\":\"Unauthorized\"}");
                        })
                ).build();
    }
}
