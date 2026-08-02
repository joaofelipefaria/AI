package br.com.jfelipefaria.acme.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object with the total number of vacation days
 * an Employee has already taken during a given year.
 */
@Data
@NoArgsConstructor
public class FeriasDiasNoAnoDTO {
    private Integer idEmployee;
    private Integer ano;
    private long diasDeFerias;
}
