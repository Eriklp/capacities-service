package com.example.capacities.application;

import com.example.capacities.domain.model.Capacity;
import com.example.capacities.domain.repository.CapacityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    public Flux<Capacity> listCapacities() {
        return Flux.fromIterable(capacityRepository.findAll());
    }

    public Mono<Void> deleteCapacity(Long id) {
        Mono.fromRunnable(() -> capacityRepository.deleteById(id));
        return null;
    }
}
