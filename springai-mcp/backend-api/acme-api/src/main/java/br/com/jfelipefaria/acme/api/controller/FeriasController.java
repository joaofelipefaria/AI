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
import org.springframework.web.bind.annotation.RestController;

import br.com.jfelipefaria.acme.api.dto.FeriasDTO;
import br.com.jfelipefaria.acme.api.service.FeriasService;

/**
 * REST controller for managing Ferias (vacation period) entities,
 * always scoped to a specific Employee.
 */
@RestController
@RequestMapping("/api/employees/{employeeId}/ferias")
public class FeriasController {

    @Autowired
    private FeriasService feriasService;

    /**
     * Get all Ferias records for a given Employee.
     * @param employeeId Employee ID.
     * @return List of FeriasDTO objects.
     */
    @GetMapping
    public ResponseEntity<List<FeriasDTO>> getFeriasByEmployee(@PathVariable("employeeId") Integer employeeId) {
        List<FeriasDTO> ferias = feriasService.getFeriasByEmployeeId(employeeId);
        if (ferias.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(ferias);
    }

    /**
     * Get a Ferias record by its ID.
     * @param feriasId Ferias ID.
     * @return FeriasDTO wrapped in ResponseEntity.
     */
    @GetMapping("/{feriasId}")
    public ResponseEntity<FeriasDTO> getFeriasById(@PathVariable("feriasId") Integer feriasId) {
        Optional<FeriasDTO> ferias = feriasService.getFeriasById(feriasId);
        return ferias.map(ResponseEntity::ok)
                     .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Create a new Ferias record for the given Employee.
     * @param employeeId Employee ID.
     * @param feriasDTO FeriasDTO object (dataInicio, dataFim).
     * @return Created FeriasDTO with HTTP 201 status.
     */
    @PostMapping
    public ResponseEntity<FeriasDTO> createFerias(
            @PathVariable("employeeId") Integer employeeId,
            @RequestBody FeriasDTO feriasDTO) {
        FeriasDTO created = feriasService.createFerias(employeeId, feriasDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Update an existing Ferias record.
     * @param employeeId Employee ID.
     * @param feriasId Ferias ID.
     * @param feriasDTO FeriasDTO object.
     * @return Updated FeriasDTO.
     */
    @PutMapping("/{feriasId}")
    public ResponseEntity<FeriasDTO> updateFerias(
            @PathVariable("employeeId") Integer employeeId,
            @PathVariable("feriasId") Integer feriasId,
            @RequestBody FeriasDTO feriasDTO) {
        FeriasDTO updated = feriasService.updateFerias(employeeId, feriasId, feriasDTO);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete a Ferias record by its ID.
     * @param feriasId Ferias ID.
     * @return No content response.
     */
    @DeleteMapping("/{feriasId}")
    public ResponseEntity<Void> deleteFerias(@PathVariable("feriasId") Integer feriasId) {
        feriasService.deleteFeriasById(feriasId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Delete all Ferias records for the given Employee.
     * @param employeeId Employee ID.
     * @return No content response.
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteAllFerias(@PathVariable("employeeId") Integer employeeId) {
        feriasService.deleteFeriasByEmployeeId(employeeId);
        return ResponseEntity.noContent().build();
    }
}
