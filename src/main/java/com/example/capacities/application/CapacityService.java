package com.example.capacities.application;

import com.example.capacities.client.TechnologyServiceClient;
import com.example.capacities.domain.exception.CustomException;
import com.example.capacities.domain.model.Capacity;
import com.example.capacities.domain.repository.CapacityRepository;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CapacityService {

    private final CapacityRepository capacityRepository;

    private final TechnologyServiceClient technologyServiceClient;


    @Autowired
    public CapacityService(CapacityRepository capacityRepository, TechnologyServiceClient technologyServiceClient) {
        this.capacityRepository = capacityRepository;
        this.technologyServiceClient = technologyServiceClient;
    }

    public Mono<Capacity> createCapacity(Capacity capacity) {
        return validateTechnologies(capacity.getTechnologyNames())
                .flatMap(valid -> {
                    if (valid) {
                        return Mono.just(capacityRepository.save(capacity));
                    } else {
                        return Mono.error(new CustomException("Una o más tecnologías no existen."));
                    }
                });
        //return Mono.just(capacityRepository.save(capacity));
    }

    public Mono<Capacity> getCapacityById(Long id) {
        return Mono.justOrEmpty(capacityRepository.findById(id));
    }

    public Mono<Boolean> validateTechnologies(Set<String> technologyNames) {
        return technologyServiceClient.validateExistence(new ArrayList<>(technologyNames));
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

    public boolean validateExistence(List<String> names) {
        List<Capacity> foundCapacities = capacityRepository.findByNameIn(names);
        Set<String> foundNames = foundCapacities.stream()
                .map(Capacity::getName)
                .collect(Collectors.toSet());
        System.out.println(names);
        return names.stream().allMatch(foundNames::contains);
    }
}
