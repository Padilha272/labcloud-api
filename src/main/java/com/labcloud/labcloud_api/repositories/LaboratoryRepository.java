package com.labcloud.labcloud_api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.labcloud.labcloud_api.models.Laboratory;

public interface LaboratoryRepository extends JpaRepository<Laboratory, String> {

    Optional<Laboratory> findByTenantId(String tenantId);

    List<Laboratory> findByActiveTrue();

    List<Laboratory> findByNameContainingIgnoreCase(String name);

    @Query("SELECT l FROM Laboratory l LEFT JOIN FETCH l.users WHERE l.id = :id")
    Optional<Laboratory> findByIdWithUsers(@Param("id") String id);

    @Query("SELECT l FROM Laboratory l LEFT JOIN FETCH l.experiments WHERE l.id = :id")
    Optional<Laboratory> findByIdWithExperiments(@Param("id") String id);

    boolean existsByTenantId(String tenantId);

}
