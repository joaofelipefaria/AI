package br.com.jfelipefaria.acme.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.jfelipefaria.acme.api.entity.EmployeeEntity;

/**
 * Repository interface for EmployeeEntity.
 * Extends JpaRepository to provide CRUD operations.
 * Annotated with @Repository for Spring Data JPA.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Integer> {
}
