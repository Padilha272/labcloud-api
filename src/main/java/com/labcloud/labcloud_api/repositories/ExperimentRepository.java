package com.labcloud.labcloud_api.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.labcloud.labcloud_api.enums.ExperimentStatus;
import com.labcloud.labcloud_api.models.Experiment;

public interface ExperimentRepository extends JpaRepository<Experiment, String> {

    Optional<Experiment> findByTenantId(String tenantId);

    Page<Experiment> findByTenantId(String tenantId, Pageable pageable);

    List<Experiment> findByLaboratoryId(String laboratoryId);

    List<Experiment> findByTenantIdAndStatus(String tenantId, ExperimentStatus status);

    List<Experiment> findByTenantIdAndNameContainingIgnoreCase(String tenantId, String name);

    @Query("SELECT e FROM Experiment e WHERE e.tenantId = :tenantId AND e.status IN ('PLANNED', 'ACTIVE')")
    List<Experiment> findActiveExperiments(@Param("tenantId") String tenantId);

    @Query("SELECT e FROM Experiment e WHERE e.tenantId = :tenantId " +
            "AND e.status = 'COMPLETED' AND e.endDate BETWEEN :startDate AND :endDate")
    List<Experiment> findCompletedExperimentsBetweenDates(
            @Param("tenantId") String tenantId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT e FROM Experiment e LEFT JOIN FETCH e.samples WHERE e.id = :id")
    Optional<Experiment> findByIdWithSamples(@Param("id") String id);

    @Query("SELECT e FROM Experiment e JOIN FETCH e.laboratory JOIN FETCH e.createdBy WHERE e.id = :id")
    Optional<Experiment> findByIdWithDetails(@Param("id") String id);

    @Query("SELECT e.status, COUNT(e) FROM Experiment e WHERE e.tenantId = :tenantId GROUP BY e.status")
    List<Object[]> countExperimentsByStatus(@Param("tenantId") String tenantId);

    @Query("SELECT e FROM Experiment e WHERE e.tenantId = :tenantId ORDER BY e.createdAt DESC")
    List<Experiment> findRecentExperiments(@Param("tenantId") String tenantId, Pageable pageable);

}
