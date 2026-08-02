package br.com.joaofelipefaria.acme.mcp.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import br.com.joaofelipefaria.acme.mcp.client.EmployeeApiClient;
import br.com.joaofelipefaria.acme.mcp.dto.Employee;

@Service
public class EmployeeTools {

    private final EmployeeApiClient api;

    public EmployeeTools(EmployeeApiClient api) {
        this.api = api;
    }

    @Tool(description = "Returns employee information")
    public Employee employee(Long employeeId) {

        return api.getEmployee(employeeId);

    }

}