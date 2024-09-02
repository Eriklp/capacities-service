package com.example.capacities.infrastructure;

import com.example.capacities.application.CapacityDTO;
import com.example.capacities.application.CapacityService;
import com.example.capacities.domain.exception.CustomException;
import com.example.capacities.domain.model.Capacity;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/")
public class CapacityController {

    private final CapacityService capacityService;

    @Autowired
    public CapacityController(CapacityService capacityService) {
        this.capacityService = capacityService;
    }

    @Operation(summary = "Post create capacity", description = "Create new capacity")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Capacity> createCapacity(@RequestBody Capacity capacity) {
        return capacityService.createCapacity(capacity);
    }


    @Operation(summary = "Get capacity by id", description = "Retrieves a capacity by id")
    @GetMapping("/{id}")
    public Mono<Capacity> getCapacityById(@PathVariable Long id) {
        return capacityService.getCapacityById(id);
    }

    @Operation(summary = "Get all capacities", description = "Retrieves a paginated list of capacities")
    @GetMapping
    public Flux<CapacityDTO> listCapacities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortField,
            @RequestParam(defaultValue = "asc") String sortOrder) {
        return capacityService.listCapacities(page, size, sortField, sortOrder);
    }

    @Operation(summary = "Delete capacity by Id", description = "Delete capacity by id")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteCapacity(@PathVariable Long id) {
        return capacityService.deleteCapacity(id);
    }

    @Operation(summary = "Validate that technology list exist", description = "Return True if all tecnologie list exist in db")
    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateTechnologies(@RequestBody List<String> names) {
        boolean allExist = capacityService.validateExistence(names);
        return ResponseEntity.ok(allExist);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<String> handleCustomException(CustomException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }
}
