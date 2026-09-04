package com.labcloud.labcloud_api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.labcloud.labcloud_api.enums.UserRole;
import com.labcloud.labcloud_api.models.User;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findByLaboratoryId(String laboratoryId);

    List<User> findByLaboratoryIdAndActiveTrue(String laboratoryId);

    List<User> findByLaboratoryIdAndRole(String laboratoryId, UserRole role);

    @Query("SELECT u FROM User u JOIN FETCH u.laboratory WHERE u.email = :email")
    Optional<User> findByEmailWithLaboratory(@Param("email") String email);

    @Query("SELECT u FROM User u JOIN FETCH u.laboratory WHERE u.id = :id AND u.tenantId = :tenantId")
    Optional<User> findByIdAndTenantId(@Param("id") String id, @Param("tenantId") String tenantId);

    @Query("SELECT COUNT(u) FROM User u WHERE u.laboratory.id = :laboratoryId")
    long countByLaboratoryId(@Param("laboratoryId") String laboratoryId);

}
