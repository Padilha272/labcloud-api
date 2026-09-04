package com.labcloud.labcloud_api.models;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "results")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false, length = 100)
    private String parameter;

    @Column(nullable = false)
    private String value;

    @Column(length = 20)
    private String unit;

    private LocalDateTime measurementDate;

    private String instrument;

    @Column(length = 255)
    private String method;

    @Column(columnDefinition = "TEXT")
    private String observations;

    private Boolean isValid = true;

    @Column(columnDefinition = "TEXT")
    private String qualityControl;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Uma amostra tem vários resultados -> Many to one
    // Vários resultados são registrados por um único usuário -> Many to one

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sample_id", nullable = false)
    private Sample sample;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

}
