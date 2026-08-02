package br.com.jfelipefaria.acme.api.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jfelipefaria.acme.api.dto.FeriasDTO;
import br.com.jfelipefaria.acme.api.dto.FeriasDiasNoAnoDTO;
import br.com.jfelipefaria.acme.api.entity.EmployeeEntity;
import br.com.jfelipefaria.acme.api.entity.FeriasEntity;
import br.com.jfelipefaria.acme.api.repository.EmployeeRepository;
import br.com.jfelipefaria.acme.api.repository.FeriasRepository;
import jakarta.persistence.EntityNotFoundException;

/**
 * Service class for business logic related to Ferias (vacation) entities.
 * Handles CRUD operations, conversion between DTOs and entities, and the
 * calculation of how many vacation days an Employee has taken in a given year.
 */
@Service
public class FeriasService {

    @Autowired
    private FeriasRepository feriasRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * Retrieve a Ferias record by its ID.
     * @param id Ferias ID.
     * @return Optional containing FeriasDTO if found.
     */
    public Optional<FeriasDTO> getFeriasById(Integer id) {
        return feriasRepository.findById(id)
                .map(this::convertToDTO);
    }

    /**
     * Retrieve all Ferias records for a given Employee ID.
     * @param employeeId Employee ID.
     * @return List of FeriasDTO objects.
     */
    public List<FeriasDTO> getFeriasByEmployeeId(Integer employeeId) {
        return feriasRepository.findByEmployeeId(employeeId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Create a new Ferias record for a given Employee.
     * @param employeeId Employee ID.
     * @param feriasDTO FeriasDTO object.
     * @return Created FeriasDTO.
     */
    public FeriasDTO createFerias(Integer employeeId, FeriasDTO feriasDTO) {
        EmployeeEntity employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("EmployeeEntity not found with ID " + employeeId));

        if (feriasDTO.getDataInicio() == null || feriasDTO.getDataFim() == null) {
            throw new IllegalArgumentException("dataInicio e dataFim sao obrigatorios.");
        }
        if (feriasDTO.getDataFim().isBefore(feriasDTO.getDataInicio())) {
            throw new IllegalArgumentException("dataFim nao pode ser anterior a dataInicio.");
        }

        FeriasEntity entity = new FeriasEntity();
        entity.setEmployee(employee);
        entity.setDataInicio(feriasDTO.getDataInicio());
        entity.setDataFim(feriasDTO.getDataFim());

        return convertToDTO(feriasRepository.save(entity));
    }

    /**
     * Update an existing Ferias record.
     * @param employeeId Employee ID.
     * @param feriasId Ferias ID.
     * @param feriasDTO FeriasDTO object.
     * @return Updated FeriasDTO.
     */
    public FeriasDTO updateFerias(Integer employeeId, Integer feriasId, FeriasDTO feriasDTO) {
        FeriasEntity entity = feriasRepository.findById(feriasId)
                .orElseThrow(() -> new EntityNotFoundException("FeriasEntity not found with ID " + feriasId));

        if (!entity.getEmployee().getId().equals(employeeId)) {
            throw new IllegalArgumentException("O registro de ferias nao pertence ao employee informado.");
        }
        if (feriasDTO.getDataFim().isBefore(feriasDTO.getDataInicio())) {
            throw new IllegalArgumentException("dataFim nao pode ser anterior a dataInicio.");
        }

        entity.setDataInicio(feriasDTO.getDataInicio());
        entity.setDataFim(feriasDTO.getDataFim());

        return convertToDTO(feriasRepository.save(entity));
    }

    /**
     * Delete a Ferias record by its ID.
     * @param id Ferias ID.
     */
    public void deleteFeriasById(Integer id) {
        feriasRepository.deleteById(id);
    }

    /**
     * Delete all Ferias records for a given Employee.
     * @param employeeId Employee ID.
     */
    public void deleteFeriasByEmployeeId(Integer employeeId) {
        feriasRepository.deleteAllByEmployeeId(employeeId);
    }

    /**
     * Calculate how many vacation days a given Employee has already taken
     * during a specific year (defaults to the current year).
     * Only the portion of each vacation period that falls within the
     * requested year is counted, so periods that cross a year boundary
     * (e.g. Dec 28th to Jan 5th) are counted proportionally.
     * @param employeeId Employee ID.
     * @param ano Year to calculate, e.g. 2026.
     * @return FeriasDiasNoAnoDTO with the total number of days.
     */
    public FeriasDiasNoAnoDTO getDiasDeFeriasNoAno(Integer employeeId, int ano) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new EntityNotFoundException("EmployeeEntity not found with ID " + employeeId);
        }

        LocalDate inicioAno = LocalDate.of(ano, 1, 1);
        LocalDate fimAno = LocalDate.of(ano, 12, 31);

        List<FeriasEntity> periodos = feriasRepository.findByEmployeeIdOverlappingRange(employeeId, inicioAno, fimAno);

        long totalDias = periodos.stream()
                .mapToLong(f -> {
                    LocalDate inicioEfetivo = f.getDataInicio().isBefore(inicioAno) ? inicioAno : f.getDataInicio();
                    LocalDate fimEfetivo = f.getDataFim().isAfter(fimAno) ? fimAno : f.getDataFim();
                    // Both dates inclusive, so add 1 day.
                    return ChronoUnit.DAYS.between(inicioEfetivo, fimEfetivo) + 1;
                })
                .sum();

        FeriasDiasNoAnoDTO resultado = new FeriasDiasNoAnoDTO();
        resultado.setIdEmployee(employeeId);
        resultado.setAno(ano);
        resultado.setDiasDeFerias(totalDias);
        return resultado;
    }

    // --- Conversion Methods ---

    private FeriasDTO convertToDTO(FeriasEntity entity) {
        FeriasDTO dto = new FeriasDTO();
        dto.setId(entity.getId());
        dto.setIdEmployee(entity.getEmployee() != null ? entity.getEmployee().getId() : null);
        dto.setDataInicio(entity.getDataInicio());
        dto.setDataFim(entity.getDataFim());
        return dto;
    }
}
