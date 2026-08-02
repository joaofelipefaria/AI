package br.com.joaofelipefaria.ai.acme.app.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

class HrToolServiceTest {

    private final HrToolService service = new HrToolService(new RestTemplate(), "http://localhost:8081");

    @Test
    void shouldExtractEmployeeIdFromQuestion() {
        assertThat(service.extractEmployeeId("What is the leave balance for employee 42?")).contains(42);
        assertThat(service.extractEmployeeId("Check the vacation status for employee id 7")).contains(7);
        assertThat(service.extractEmployeeId("Tell me about the company policy")).isEmpty();
    }
}
