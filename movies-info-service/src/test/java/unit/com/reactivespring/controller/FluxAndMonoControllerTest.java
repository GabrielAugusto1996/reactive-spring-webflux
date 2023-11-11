package com.reactivespring.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.net.URI;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@WebFluxTest(controllers = FluxAndMonoController.class)
@AutoConfigureWebTestClient
class FluxAndMonoControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void flux() {
        webTestClient
                .get()
                .uri(URI.create("/flux"))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(Integer.class)
                .hasSize(3);
    }

    @Test
    void fluxApproachTwo() {
        var fluxResultBody = webTestClient
                .get()
                .uri(URI.create("/flux"))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .returnResult(Integer.class)
                .getResponseBody();

        StepVerifier
                .create(fluxResultBody)
                .expectNextCount(3);
    }

    @Test
    void fluxApproachThree() {
        webTestClient
                .get()
                .uri(URI.create("/flux"))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(Integer.class)
                .consumeWith(list -> Objects.requireNonNull(list.getResponseBody()).forEach(Assertions::assertNotNull));
    }

    @Test
    void mono() {
        webTestClient
                .get()
                .uri(URI.create("/mono"))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(Integer.class)
                .hasSize(1);
    }

    @Test
    void stream() {
        var fluxResultBody = webTestClient
                .get()
                .uri(URI.create("/stream"))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .returnResult(Integer.class)
                .getResponseBody();

        StepVerifier
                .create(fluxResultBody)
                .expectNextCount(3)
                .thenCancel()
                .verify();
    }

}