package com.labcloud.labcloud_api.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.labcloud.labcloud_api.models.Sample;

public interface SampleRepository extends JpaRepository<Sample, String> {

        Optional<Sample> findByTenantId(String tenantId);

        Page<Sample> findByTenantId(String tenantId, Pageable pageable);

        List<Sample> findByTenantIdAndType(String tenantId, String Type);

        List<Sample> findByTenantIdAndNameContainingIgnoreCase(String tenantId, String name);

        @Query("SELECT s FROM Sample s WHERE s.tenantId = :tenantId " +
                        "AND s.collectionDate BETWEEN :startDate AND :endDate")
        List<Sample> findSamplesByCollectionDateBetween(
                        @Param("tenantId") String tenantId,
                        @Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate);

        @Query("SELECT s FROM Sample s LEFT JOIN FETCH s.results WHERE s.id = :id")
        Optional<Sample> findByIdWithResults(@Param("id") String id);

        @Query("SELECT s FROM Sample s JOIN FETCH s.experiment JOIN FETCH s.createdBy WHERE s.id = :id")
        Optional<Sample> findByIdWithDetails(@Param("id") String id);

        @Query("SELECT s.type, COUNT(s) FROM Sample s WHERE s.tenantId = :tenantId GROUP BY s.type")
        List<Object[]> countSamplesByType(@Param("tenantId") String tenantId);

        @Query("SELECT s FROM Sample s WHERE s.experiment.id = :experimentId")
        Page<Sample> findByExperimentIdPaged(
                        @Param("experimentId") String experimentId,
                        Pageable pageable);

        @Query("SELECT s FROM Sample s WHERE s.experiment.id = :experimentId AND s.results IS EMPTY")
        List<Sample> findSamplesWithoutResults(@Param("experimentId") String experimentId);

}
