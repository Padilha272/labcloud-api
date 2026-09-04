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
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "laboratories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Laboratory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String Id;

    @Column(nullable = false, length = 100) // Nome precisa ser preenchido, tamanho máximo = 100 caracteres
    private String name;

    @Column(unique = true, nullable = false, length = 50)
    private String tenantId;

    @Column(length = 255)
    private String description;

    @Column(length = 200)
    private String address;

    @Column(length = 20)
    private String phone;

    @Column(length = 100)
    private String email;

    @Column(nullable = false)
    private Boolean active = true;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // O laboratório pode ter vários usuários e realizar experimentos

    // Um laboratório para vários usuários
    @OneToMany(mappedBy = "laboratory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "laboratory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Experiment> experiments = new ArrayList<>();

}
