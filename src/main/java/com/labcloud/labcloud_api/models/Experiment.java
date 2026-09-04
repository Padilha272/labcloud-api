package com.labcloud.labcloud_api.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.labcloud.labcloud_api.enums.ExperimentStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "experiments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Experiment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    public String tenantId;

    @Column(nullable = false, length = 100)
    public String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExperimentStatus status;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @Column(name = "objective", columnDefinition = "TEXT")
    private String objective;

    @Column(name = "methodology", columnDefinition = "TEXT")
    private String methodology;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Muitos experimentos podem ocorrer em um laboratório -> Many to one
    // Muitos experimentos podem ser realizados por um usuário -> Many to one
    // Um experimento pode ter várias amostras -> One to many

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "laboratory_id", nullable = false)
    private Laboratory laboratory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @OneToMany(mappedBy = "experiment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Sample> samples = new ArrayList<>();

}
