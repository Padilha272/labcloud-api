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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "samples")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sample {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 50)
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
    private LocalDateTime createdAt;

    @UpdateTimestamp
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
    private List<Result> results = new ArrayList<>();

}
