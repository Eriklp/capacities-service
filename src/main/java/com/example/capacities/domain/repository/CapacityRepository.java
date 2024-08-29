package com.example.capacities.domain.repository;

import com.example.capacities.domain.model.Capacity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CapacityRepository extends JpaRepository<Capacity, Long> {
    // Aquí se pueden agregar consultas específicas si es necesario
}
