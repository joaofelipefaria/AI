package br.com.jfelipefaria.acme.api.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity class representing the Ferias (vacation) table.
 * Uses JPA annotations for ORM mapping.
 * Includes a many-to-one relationship with EmployeeEntity.
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "ferias")
public class FeriasEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ferias_id_seq")
    @SequenceGenerator(name = "ferias_id_seq", sequenceName = "ferias_id_seq", allocationSize = 1)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_employee", nullable = false)
    private EmployeeEntity employee;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_fim", nullable = false)
    private LocalDate dataFim;
}
