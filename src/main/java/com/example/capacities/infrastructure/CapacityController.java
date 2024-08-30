package com.example.capacities.infrastructure;

import com.example.capacities.application.CapacityDTO;
import com.example.capacities.application.CapacityService;
import com.example.capacities.domain.model.Capacity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/capacities")
public class CapacityController {

    private final CapacityService capacityService;

    @Autowired
    public CapacityController(CapacityService capacityService) {
        this.capacityService = capacityService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Capacity> createCapacity(@RequestBody Capacity capacity) {
        return capacityService.createCapacity(capacity);
    }

    @GetMapping("/{id}")
    public Mono<Capacity> getCapacityById(@PathVariable Long id) {
        return capacityService.getCapacityById(id);
    }

    @GetMapping
    public Flux<CapacityDTO> listCapacities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortField,
            @RequestParam(defaultValue = "asc") String sortOrder) {
        return capacityService.listCapacities(page, size, sortField, sortOrder);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteCapacity(@PathVariable Long id) {
        return capacityService.deleteCapacity(id);
    }
}
