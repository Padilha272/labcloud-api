package com.labcloud.labcloud_api.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;

@Entity
@Table(name = "samples")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Builder

public class Sample {

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
    private String name;

    @Column(nullable = false, length = 50)
    @ToString.Include
    private String type;

    private LocalDateTime collectionDate;

    @Column(length = 100)
    private String collectionMethod;

    private Double quantity;

    @Column(length = 20)
    private String unit;

    @Column(length = 255)
    private String storageConditions;

    @Column(length = 100)
    private String location;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(updatable = false)
    @Setter(AccessLevel.NONE)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Setter(AccessLevel.NONE)
    private LocalDateTime updatedAt;

    // Muitas amostras estão relacionadas a um experimento ->Many to one
    // Muitoas amostras são cadastradas por um usuário -> Many to one
    // Uma amostra pode ter vários resultados -> One to many

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "experiment_id", nullable = false)
    private Experiment experiment;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @OneToMany(mappedBy = "sample", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Result> results = new ArrayList<>();

    // Método para atualizar o tenentId
    public void updateTenantId(String newTenantId) {
        if (newTenantId == null || newTenantId.trim().isEmpty()) {
            throw new IllegalArgumentException("Tenant ID não pode ser vazio");
        }
        if (!newTenantId.matches("^[a-zA-Z0-9-_]+$")) {
            throw new IllegalArgumentException("Tenant ID contém caracteres inválidos");
        }
        this.tenantId = newTenantId;
    }

}
