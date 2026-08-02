package br.com.joaofelipefaria.acme.mcp.client;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import br.com.joaofelipefaria.acme.mcp.dto.Employee;

@Service
public class EmployeeApiClient {

    private final RestClient restClient;

    public EmployeeApiClient(RestClient.Builder builder) {

        this.restClient = builder
                .baseUrl("http://localhost:8081")
                .build();
    }

    public Employee getEmployee(Long id) {

        return restClient
                .get()
                .uri("/employees/{id}", id)
                .retrieve()
                .body(Employee.class);
    }

}