package br.com.jfelipefaria.acme.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Acme API.
 * Uses Spring Boot to bootstrap and launch the REST API service.
 * The @SpringBootApplication annotation enables component scanning,
 * auto-configuration, and configuration properties support.
 */
@SpringBootApplication
public class AcmeApp {
    /**
     * Entry point for the Spring Boot application.
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        SpringApplication.run(AcmeApp.class, args);
    }
}
