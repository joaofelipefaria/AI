package br.com.joaofelipefaria.ai.acme.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public String backendBaseUrl(@Value("${app.backend.base-url:http://localhost:8081}") String backendBaseUrl) {
        return backendBaseUrl;
    }
}
