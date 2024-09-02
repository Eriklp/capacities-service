package com.example.capacities.domain.repository;

import com.example.capacities.domain.model.Capacity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CapacityRepository extends JpaRepository<Capacity, Long> {
    List<Capacity> findByNameIn(List<String> names);
    // Aquí se pueden agregar consultas específicas si es necesario
}
