package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.List;

class FluxAndMonoGeneratorServiceTest {

    private final FluxAndMonoGeneratorService fluxAndMonoGeneratorService;

    FluxAndMonoGeneratorServiceTest() {
        fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();
    }

    @Nested
    @DisplayName("Tests for Flux methods")
    class testsForFlux {
        @Test
        void namesFlux_FluxString_WhenSuccess() {
            //given
            //when
            var namesFlux = fluxAndMonoGeneratorService.namesFlux();

            //then
            StepVerifier.create(namesFlux)
                    .expectNext("ALEX", "BEN", "CLOE")
                    //.expectNextCount(3) You can use it to verify if you flux have X elements
                    .verifyComplete();

        }

        @Test
        void namesFluxFlatMap_FluxString_WhenSuccess() {
            //given
            //when
            var namesFlux = fluxAndMonoGeneratorService.namesFluxFlatMap();

            //then
            StepVerifier.create(namesFlux)
                    .expectNext("A", "L", "E", "X", "B", "E", "N", "C", "L", "O", "E")
                    //.expectNextCount(3) You can use it to verify if you flux have X elements
                    .verifyComplete();

        }

        @Test
        void namesFluxFlatMapAsync_FluxString_WhenSuccess() {
            //given
            //when
            var namesFlux = fluxAndMonoGeneratorService.namesFluxFlatMapAsync();

            //then
            StepVerifier.create(namesFlux)
                    .expectNextCount(11)
                    .verifyComplete();

        }

        @Test
        void namesFluxConcatMapAsync_FluxString_WhenSuccess() {
            //given
            //when
            var namesFlux = fluxAndMonoGeneratorService.namesFluxConcatMapAsync();

            //then
            StepVerifier.create(namesFlux)
                    .expectNext("A", "L", "E", "X", "B", "E", "N", "C", "L", "O", "E")
                    .verifyComplete();
        }
    }

    @Nested
    @DisplayName("Tests for Mono methods")
    class testsForMono {
        @Test
        void nameMono_MonoString_WhenSuccess() {
            //given
            //when
            var nameMono = fluxAndMonoGeneratorService.nameMono();

            //then
            StepVerifier.create(nameMono)
                    .expectNext("Alex")
                    .verifyComplete();

        }

        @Test
        void nameMonoFlatMap_ListString_WhenSuccess() {
            //given
            //when
            var nameMono = fluxAndMonoGeneratorService.nameMonoFlatMap();

            //then
            StepVerifier.create(nameMono)
                    .expectNext(List.of("A", "l", "e", "x"))
                    .verifyComplete();

        }
    }
}