package br.com.jfelipefaria.acme.api.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.jfelipefaria.acme.api.entity.FeriasEntity;

/**
 * Repository interface for FeriasEntity (vacation periods).
 * Extends JpaRepository to provide CRUD operations.
 * Includes custom queries for finding and deleting vacation periods by Employee ID,
 * and for finding vacation periods that overlap a given date range (used to
 * compute vacation days taken within a specific year).
 */
@Repository
public interface FeriasRepository extends JpaRepository<FeriasEntity, Integer> {

    /**
     * Find all FeriasEntity records for a given Employee ID.
     * @param employeeId Employee ID.
     * @return List of FeriasEntity objects.
     */
    List<FeriasEntity> findByEmployeeId(Integer employeeId);

    /**
     * Find all FeriasEntity records for a given Employee ID that overlap
     * the provided date range (e.g. the first and last day of a year).
     * A vacation period overlaps the range when it starts on/before the range end
     * AND ends on/after the range start.
     * @param employeeId Employee ID.
     * @param rangeStart Start of the range (inclusive).
     * @param rangeEnd End of the range (inclusive).
     * @return List of overlapping FeriasEntity objects.
     */
    @Query("SELECT f FROM FeriasEntity f "
         + "WHERE f.employee.id = :employeeId "
         + "AND f.dataInicio <= :rangeEnd "
         + "AND f.dataFim >= :rangeStart")
    List<FeriasEntity> findByEmployeeIdOverlappingRange(
            @Param("employeeId") Integer employeeId,
            @Param("rangeStart") LocalDate rangeStart,
            @Param("rangeEnd") LocalDate rangeEnd);

    /**
     * Delete all FeriasEntity records by Employee ID.
     * Uses a custom JPQL query.
     * @param employeeId Employee ID.
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM FeriasEntity f WHERE f.employee.id = :employeeId")
    void deleteAllByEmployeeId(@Param("employeeId") Integer employeeId);
}
