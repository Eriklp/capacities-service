package com.example.capacities.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Component
public class TechnologyServiceClient {

    private final WebClient webClient;

    public TechnologyServiceClient(WebClient.Builder webClientBuilder) {
        // Use the Docker service name and the internal port
        this.webClient = webClientBuilder.baseUrl("http://technology-service:8080").build();
    }

    public Mono<Boolean> validateExistence(List<String> technologyNames) {
        return webClient
                .post()
                .uri("/validate")
                .bodyValue(new ArrayList<>(technologyNames))
                .retrieve()
                .bodyToMono(Boolean.class) // Espera un Boolean directamente desde el cuerpo de la respuesta
                .flatMap(allExist -> {
                    if (allExist) {
                        return Mono.just(true); // O proceder con la operación deseada si todas las tecnologías existen
                    } else {
                        return Mono.just(false);
                    }
                });
    }
}
