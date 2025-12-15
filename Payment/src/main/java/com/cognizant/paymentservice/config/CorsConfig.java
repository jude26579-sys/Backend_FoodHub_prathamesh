package com.cognizant.paymentservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * CORS Configuration for Payment Service
 * Enables cross-origin requests from frontend and other services
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Allow requests from all local development servers
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:8084",    // Vite Frontend
            "http://localhost:3000",    // React Dev
            "http://localhost:5173",    // Vite Default
            "http://localhost:8080",    // User/Auth Service
            "http://localhost:8081",    // Services
            "http://localhost:8082",    // Services
            "http://localhost:8083",    // Services
            "http://localhost:8282"     // Payment Service
        ));
        
        // Allow all HTTP methods
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        
        // Allow all headers (including Authorization)
        configuration.setAllowedHeaders(Arrays.asList("*"));
        
        // Allow credentials (cookies, authorization headers)
        configuration.setAllowCredentials(true);
        
        // Cache CORS preflight response for 1 hour
        configuration.setMaxAge(3600L);
        
        // Create CORS configuration source
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
}
