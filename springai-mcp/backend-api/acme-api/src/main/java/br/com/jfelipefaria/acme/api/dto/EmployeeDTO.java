package br.com.jfelipefaria.acme.api.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Employee.
 * Used to transfer data between layers without exposing entity details.
 */
@Data
@NoArgsConstructor
public class EmployeeDTO {
    private Integer id;
    private String nome;
    private String dept;
    private List<FeriasDTO> ferias;
}
