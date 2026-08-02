package br.com.jfelipefaria.acme.api.dto;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Ferias (vacation period).
 */
@Data
@NoArgsConstructor
public class FeriasDTO {
    private Integer id;
    private Integer idEmployee;
    private LocalDate dataInicio;
    private LocalDate dataFim;
}
