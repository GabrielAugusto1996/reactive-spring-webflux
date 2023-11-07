package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class FluxAndMonoGeneratorServiceTest {

    private final FluxAndMonoGeneratorService fluxAndMonoGeneratorService;

    FluxAndMonoGeneratorServiceTest() {
        fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();
    }

    @Test
    void namesFlux_FluxString_WhenSuccess() {
        //given
        //when
        var namesFlux = fluxAndMonoGeneratorService.namesFlux();

        //then
        StepVerifier.create(namesFlux)
                .expectNext("Alex", "Ben", "Cloe")
                //.expectNextCount(3) You can use it to verify if you flux have X elements
                .verifyComplete();

    }

    @Test
    void nameMono_MonoString_WhenSuccess() {
        //given
        //when
        var nameMono = fluxAndMonoGeneratorService.nameMono();

        //then
        StepVerifier.create(nameMono)
                .expectNext("John")
                .verifyComplete();

    }
}