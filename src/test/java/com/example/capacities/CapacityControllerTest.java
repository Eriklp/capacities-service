package com.example.capacities;

import com.example.capacities.application.CapacityService;
import com.example.capacities.domain.model.Capacity;
import com.example.capacities.application.CapacityDTO;
import com.example.capacities.infrastructure.CapacityController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class CapacityControllerTest {

    @Mock
    private CapacityService capacityService;

    @InjectMocks
    private CapacityController capacityController;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        webTestClient = WebTestClient.bindToController(capacityController).build();
    }

    @Test
    void createCapacity() {
        Capacity capacity = new Capacity(1L, "DevOps", "DevOps description", Set.of("Docker", "Kubernetes"));
        when(capacityService.createCapacity(any(Capacity.class))).thenReturn(Mono.just(capacity));

        webTestClient.post()
                .uri("/")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(capacity)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo(capacity.getId())
                .jsonPath("$.name").isEqualTo(capacity.getName())
                .jsonPath("$.description").isEqualTo(capacity.getDescription())
                .jsonPath("$.technologyNames").isArray()
                .jsonPath("$.technologyNames[?(@ == 'Docker')]").exists()
                .jsonPath("$.technologyNames[?(@ == 'Kubernetes')]").exists();
    }



    @Test
    void getCapacityById() {
        Long id = 1L;
        Capacity capacity = new Capacity(id, "DevOps", "DevOps description", Set.of("Docker", "Kubernetes"));
        when(capacityService.getCapacityById(id)).thenReturn(Mono.just(capacity));

        webTestClient.get().uri("/" + id)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(capacity.getId())
                .jsonPath("$.name").isEqualTo(capacity.getName())
                .jsonPath("$.description").isEqualTo(capacity.getDescription());
    }


    @Test
    void listCapacities() {
        CapacityDTO dto = new CapacityDTO(1L, "DevOps", "Description", Set.of("Docker", "Kubernetes"));
        when(capacityService.listCapacities(anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(Flux.just(dto));

        webTestClient.get().uri("/?page=0&size=10&sort=name&order=asc")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CapacityDTO.class)
                .consumeWith(response -> {
                    Assertions.assertThat(response.getResponseBody()).isNotNull();
                    Assertions.assertThat(response.getResponseBody()).hasSize(1);
                    Assertions.assertThat(response.getResponseBody().get(0).getTechnologyNames()).containsExactlyInAnyOrder("Docker", "Kubernetes");
                });
    }



    @Test
    void deleteCapacity() {
        when(capacityService.deleteCapacity(1L)).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/{id}", 1)
                .exchange()
                .expectStatus().isNoContent();

        verify(capacityService).deleteCapacity(1L);
    }
}