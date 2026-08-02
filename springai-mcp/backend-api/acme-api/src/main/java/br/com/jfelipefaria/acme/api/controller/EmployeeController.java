package br.com.jfelipefaria.acme.api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.jfelipefaria.acme.api.dto.EmployeeDTO;
import br.com.jfelipefaria.acme.api.dto.FeriasDiasNoAnoDTO;
import br.com.jfelipefaria.acme.api.service.EmployeeService;
import br.com.jfelipefaria.acme.api.service.FeriasService;

/**
 * REST controller for managing Employee entities.
 * Provides endpoints for CRUD operations on employees, and for
 * querying how many vacation days an employee has taken in a given year.
 * Uses Spring's @RestController and @RequestMapping annotations to define routes.
 */
@RestController
@RequestMapping("/api")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private FeriasService feriasService;

    // --- Employee Endpoints ---

    /**
     * Get all Employee records.
     * @return List of EmployeeDTO objects.
     */
    @GetMapping("/employees")
    public List<EmployeeDTO> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    /**
     * Get an Employee record by its ID.
     * @param id Employee ID.
     * @return EmployeeDTO wrapped in ResponseEntity.
     */
    @GetMapping("/employees/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable("id") Integer id) {
        Optional<EmployeeDTO> employee = employeeService.getEmployeeById(id);
        return employee.map(ResponseEntity::ok)
                        .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Create a new Employee record.
     * @param data EmployeeDTO object (nome, dept).
     * @return Created EmployeeDTO with HTTP 201 status.
     */
    @PostMapping("/employees")
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeDTO data) {
        EmployeeDTO created = employeeService.create(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Update an existing Employee record.
     * @param id Employee ID.
     * @param data EmployeeDTO object.
     * @return Updated EmployeeDTO.
     */
    @PutMapping("/employees/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable("id") Integer id, @RequestBody EmployeeDTO data) {
        if (!id.equals(data.getId())) {
            return ResponseEntity.badRequest().build();
        }
        EmployeeDTO updated = employeeService.update(data);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete an Employee record by its ID (also removes its vacation periods).
     * @param id Employee ID.
     * @return No content response.
     */
    @DeleteMapping("/employees/{id}")
    public ResponseEntity<Void> deleteEmployeeById(@PathVariable("id") Integer id) {
        employeeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Delete all Employee records.
     * @return No content response.
     */
    @DeleteMapping("/employees/all")
    public ResponseEntity<Void> deleteAllEmployees() {
        employeeService.deleteAllEmployees();
        return ResponseEntity.noContent().build();
    }

    /**
     * Get the total number of vacation days an Employee has already taken
     * during a given year. If "ano" is not provided, the current year is used.
     * Example: GET /api/employees/1/ferias/dias-no-ano?ano=2026
     * @param id Employee ID.
     * @param ano Optional year (defaults to the current year).
     * @return FeriasDiasNoAnoDTO with the total number of days.
     */
    @GetMapping("/employees/{id}/ferias/dias-no-ano")
    public ResponseEntity<FeriasDiasNoAnoDTO> getDiasDeFeriasNoAno(
            @PathVariable("id") Integer id,
            @RequestParam(value = "ano", required = false) Integer ano) {
        int anoConsulta = (ano != null) ? ano : java.time.Year.now().getValue();
        FeriasDiasNoAnoDTO resultado = feriasService.getDiasDeFeriasNoAno(id, anoConsulta);
        return ResponseEntity.ok(resultado);
    }
}
