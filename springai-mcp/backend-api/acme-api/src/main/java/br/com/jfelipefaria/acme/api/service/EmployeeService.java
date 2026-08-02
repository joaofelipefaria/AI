package br.com.jfelipefaria.acme.api.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jfelipefaria.acme.api.dto.EmployeeDTO;
import br.com.jfelipefaria.acme.api.dto.FeriasDTO;
import br.com.jfelipefaria.acme.api.entity.EmployeeEntity;
import br.com.jfelipefaria.acme.api.entity.FeriasEntity;
import br.com.jfelipefaria.acme.api.repository.EmployeeRepository;
import br.com.jfelipefaria.acme.api.repository.FeriasRepository;

/**
 * Service class for business logic related to Employee entities.
 * Handles CRUD operations and conversion between DTOs and entities.
 * Annotated with @Service to be managed by Spring's dependency injection.
 */
@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private FeriasRepository feriasRepository;

    /**
     * Retrieve all Employee records.
     * @return List of EmployeeDTO objects.
     */
    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieve an Employee record by its ID.
     * @param id Employee ID.
     * @return Optional containing EmployeeDTO if found.
     */
    public Optional<EmployeeDTO> getEmployeeById(Integer id) {
        return employeeRepository.findById(id)
                .map(this::convertToDTO);
    }

    /**
     * Create a new Employee record.
     * @param data EmployeeDTO object.
     * @return Created EmployeeDTO.
     */
    public EmployeeDTO create(EmployeeDTO data) {
        EmployeeEntity entity = convertToEntity(data);
        entity.setId(null);
        return convertToDTO(employeeRepository.save(entity));
    }

    /**
     * Update an existing Employee record.
     * @param data EmployeeDTO object.
     * @return Updated EmployeeDTO.
     */
    public EmployeeDTO update(EmployeeDTO data) {
        EmployeeEntity entity = convertToEntity(data);
        return convertToDTO(employeeRepository.save(entity));
    }

    /**
     * Delete an Employee record and its associated vacation periods by ID.
     * @param id Employee ID.
     */
    public void deleteById(Integer id) {
        feriasRepository.deleteAllByEmployeeId(id);
        employeeRepository.deleteById(id);
    }

    /**
     * Delete all Employee and Ferias records.
     */
    public void deleteAllEmployees() {
        feriasRepository.deleteAll();
        employeeRepository.deleteAll();
    }

    // --- Conversion Methods ---

    private EmployeeDTO convertToDTO(EmployeeEntity employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getId());
        dto.setNome(employee.getNome());
        dto.setDept(employee.getDept());
        if (employee.getFerias() != null) {
            dto.setFerias(
                employee.getFerias().stream()
                    .map(this::convertFeriasToDTO)
                    .collect(Collectors.toList())
            );
        }
        return dto;
    }

    private EmployeeEntity convertToEntity(EmployeeDTO dto) {
        EmployeeEntity entity = new EmployeeEntity();
        entity.setId(dto.getId());
        entity.setNome(dto.getNome());
        entity.setDept(dto.getDept());
        return entity;
    }

    private FeriasDTO convertFeriasToDTO(FeriasEntity ferias) {
        FeriasDTO dto = new FeriasDTO();
        dto.setId(ferias.getId());
        dto.setIdEmployee(ferias.getEmployee() != null ? ferias.getEmployee().getId() : null);
        dto.setDataInicio(ferias.getDataInicio());
        dto.setDataFim(ferias.getDataFim());
        return dto;
    }
}
