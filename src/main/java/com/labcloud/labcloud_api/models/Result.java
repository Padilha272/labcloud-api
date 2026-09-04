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
import lombok.*;

@Entity
@Table(name = "results")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Builder
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    @EqualsAndHashCode.Include
    @ToString.Include
    private String id;

    @Column(nullable = false)
    @Setter(AccessLevel.PROTECTED)
    @ToString.Include
    private String tenantId;

    @Column(nullable = false, length = 100)
    @ToString.Include
    private String parameter;

    @Column(nullable = false)
    @ToString.Include
    private String value;

    @Column(length = 20)
    @ToString.Include
    private String unit;

    private LocalDateTime measurementDate;

    private String instrument;

    @Column(length = 255)
    private String method;

    @Column(columnDefinition = "TEXT")
    private String observations;

    @Builder.Default
    private Boolean isValid = true;

    @Column(columnDefinition = "TEXT")
    private String qualityControl;

    @CreationTimestamp
    @Column(updatable = false)
    @Setter(AccessLevel.NONE)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Setter(AccessLevel.NONE)
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
