package com.example.capacities.application;

import java.util.Set;

public class CapacityDTO {
    private Long id;
    private String name;
    private String description;
    private Set<String> technologyNames;

    public CapacityDTO(Long id, String name, String description, Set<String> technologyNames) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.technologyNames = technologyNames;
    }

    // Getters y setters

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTechnologyNames(Set<String> technologyNames) {
        this.technologyNames = technologyNames;
    }

    public Set<String> getTechnologyNames() {
        return technologyNames;
    }
}
