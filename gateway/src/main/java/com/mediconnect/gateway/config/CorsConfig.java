package com.mediconnect.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * CORS Configuration for API Gateway
 * 
 * This configuration is applied at the gateway level to provide centralized
 * CORS management for all downstream microservices.
 * 
 * Production-ready configuration with proper security settings.
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        
        // Allow credentials (required for HTTP-only cookies)
        corsConfig.setAllowCredentials(true);
        
        // Allowed origins - UPDATE THIS FOR PRODUCTION
        corsConfig.setAllowedOrigins(List.of(
            "http://localhost:5173",  // Vite dev server
            "http://localhost:5174",   // Alternative React dev server
            "http://localhost:8084",   // Alternative React dev server
            "http://localhost:3000"   // Alternative React dev server
        ));
        
        // Allowed HTTP methods
        corsConfig.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"
        ));
        
        // Allowed headers
        corsConfig.setAllowedHeaders(Arrays.asList(
            "Authorization",
            "Content-Type",
            "Accept",
            "Origin",
            "X-Requested-With"
        ));
        
        // Exposed headers (headers that frontend can access)
        corsConfig.setExposedHeaders(Arrays.asList(
            "Authorization",
            "Content-Type"
        ));
        
        // Preflight cache duration (1 hour)
        corsConfig.setMaxAge(3600L);
        
        // Apply CORS configuration to all routes
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        
        return new CorsWebFilter(source);
    }
}
