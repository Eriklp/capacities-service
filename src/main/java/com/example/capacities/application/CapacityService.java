package com.example.capacities.application;

import com.example.capacities.domain.model.Capacity;
import com.example.capacities.domain.repository.CapacityRepository;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;

@Service
public class CapacityService {

    private final CapacityRepository capacityRepository;

    @Autowired
    public CapacityService(CapacityRepository capacityRepository) {
        this.capacityRepository = capacityRepository;
    }

    public Mono<Capacity> createCapacity(Capacity capacity) {
        return Mono.just(capacityRepository.save(capacity));
    }

    public Mono<Capacity> getCapacityById(Long id) {
        return Mono.justOrEmpty(capacityRepository.findById(id));
    }

    @Transactional()
    public Flux<CapacityDTO> listCapacities(int page, int size, String sortField, String sortOrder) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortField);
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        Page<CapacityDTO> capacityPage = capacityRepository.findAll(pageRequest).map(this::initializeAndConvertToDTO);
        System.out.println(capacityPage);
        return Flux.fromIterable(capacityPage.getContent());
    }

    private CapacityDTO initializeAndConvertToDTO(Capacity capacity) {
        Hibernate.initialize(capacity.getTechnologyNames());
        return convertToDTO(capacity);
    }

    private CapacityDTO convertToDTO(Capacity capacity) {
        // Convierte a DTO
        return new CapacityDTO(capacity.getId(), capacity.getName(), capacity.getDescription(), new HashSet<>(capacity.getTechnologyNames()));
    }
    
    public Mono<Void> deleteCapacity(Long id) {
        Mono.fromRunnable(() -> capacityRepository.deleteById(id));
        return null;
    }
}
