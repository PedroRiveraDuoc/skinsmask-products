package com.example.product_service.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("Configuring security filter chain for gestion_productos...");

        // Deshabilitar completamente la seguridad para permitir acceso libre temporalmente
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((requests) -> requests
                        .anyRequest().permitAll());

        logger.info("Security filter chain configured successfully with no authentication.");
        return http.build();
    }
}
