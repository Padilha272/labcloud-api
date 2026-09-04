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
import lombok.*;

@Entity
@Table(name = "experiments")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class Experiment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    @ToString.Include
    @EqualsAndHashCode.Include
    private String id;

    @Column(nullable = false)
    @Setter(AccessLevel.PROTECTED)
    @ToString.Include
    public String tenantId;

    @Column(nullable = false, length = 100)
    @ToString.Include
    public String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ToString.Include
    private ExperimentStatus status;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @Column(name = "objective", columnDefinition = "TEXT")
    private String objective;

    @Column(name = "methodology", columnDefinition = "TEXT")
    private String methodology;

    @CreationTimestamp
    @Column(updatable = false)
    @Setter(AccessLevel.NONE)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Setter(AccessLevel.NONE)
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
    @Builder.Default
    private List<Sample> samples = new ArrayList<>();

    public void complete() {
        this.status = ExperimentStatus.COMPLETED;
        this.endDate = LocalDateTime.now();
    }

    public void cancel() {
        this.status = ExperimentStatus.CANCELLED;
        this.endDate = LocalDateTime.now();
    }

    public void activate() {
        this.status = ExperimentStatus.ACTIVE;
        this.startDate = LocalDateTime.now();
    }

}
