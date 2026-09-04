package com.labcloud.labcloud_api.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.labcloud.labcloud_api.models.Result;

public interface ResultRepository extends JpaRepository<Result, String> {

    Optional<Result> findByTenantId(String tenantId);

    Page<Result> findByTenantId(String tenantId, Pageable pageable);

    List<Result> findBySampleId(String sampleId);

    Page<Result> findBySampleId(String SampleId, Pageable pageable);

    List<Result> findByTenantIdAndParameter(String tenantId, String paramether);

    List<Result> findByTenantIdAndIsValidTrue(String tenantId);

    @Query("SELECT r FROM Result r WHERE r.tenantId = :tenantId " +
            "AND r.measurementDate BETWEEN :startDate AND :endDate")
    List<Result> findResultsByMeasurementDateBetween(
            @Param("tenantId") String tenantId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT r FROM Result r JOIN FETCH r.sample JOIN FETCH r.createdBy WHERE r.id = :id")
    Optional<Result> findByIdWithDetails(@Param("id") String id);

    @Query("SELECT r FROM Result r JOIN r.sample s WHERE s.experiment.id = :experimentId")
    List<Result> findByExperimentId(@Param("experimentId") String experimentId);

    @Query("SELECT r FROM Result r JOIN r.sample s WHERE s.experiment.id = :experimentId")
    Page<Result> findByExperimentIdPaged(
            @Param("experimentId") String experimentId,
            Pageable pageable);

    @Query("SELECT r FROM Result r WHERE r.tenantId = :tenantId AND r.measurementDate = " +
            "(SELECT MAX(r2.measurementDate) FROM Result r2 WHERE r2.parameter = r.parameter AND r2.tenantId = :tenantId)")
    List<Result> findLatestResultsByParameter(@Param("tenantId") String tenantId);

    @Query("SELECT r.parameter, COUNT(r) FROM Result r WHERE r.tenantId = :tenantId GROUP BY r.parameter")
    List<Object[]> countResultsByParameter(@Param("tenantId") String tenantId);

}
