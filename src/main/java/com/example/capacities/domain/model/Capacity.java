package com.example.capacities.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "capacities")
public class Capacity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la capacidad no puede estar vacío")
    @Size(max = 50, message = "El tamaño máximo del nombre es 50 caracteres")
    private String name;

    @NotBlank(message = "La descripción no puede estar vacía")
    @Size(max = 250, message = "El tamaño máximo de la descripción es 250 caracteres")
    private String description;

    @NotBlank(message = "Debe proporcionar una lista de tecnologias")
    @ElementCollection
    @CollectionTable(name = "capacity_technologies", joinColumns = @JoinColumn(name = "capacity_id"))
    @Column(name = "technology_name")
    private Set<String> technologyNames = new HashSet<>(); // Almacena nombres de tecnología para evitar duplicados

    public Capacity() {
    }

    public Capacity(Long id, String name, String description, Set<String> technologyNames) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.technologyNames = technologyNames;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<String> getTechnologyNames() {
        return technologyNames;
    }

    public void setTechnologyNames(Set<String> technologyNames) {
        this.technologyNames = technologyNames;
    }
}

