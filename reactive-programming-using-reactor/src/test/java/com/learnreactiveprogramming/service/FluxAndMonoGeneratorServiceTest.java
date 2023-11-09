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

        @Test
        void exploreConcat_FluxString_WhenSuccess() {
            //given
            //when
            var exploreConcat = fluxAndMonoGeneratorService.exploreConcat();

            //then
            StepVerifier.create(exploreConcat)
                    .expectNext("A", "B", "C", "D", "E", "F")
                    .verifyComplete();
        }

        @Test
        void exploreConcatWith_FluxString_WhenSuccess() {
            //given
            //when
            var exploreConcat = fluxAndMonoGeneratorService.exploreConcatWith();

            //then
            StepVerifier.create(exploreConcat)
                    .expectNext("A", "B", "C", "D", "E", "F")
                    .verifyComplete();
        }

        @Test
        void exploreMerge_FluxString_WhenSuccess() {
            //given
            //when
            var exploreConcat = fluxAndMonoGeneratorService.exploreMerge();

            //then
            StepVerifier.create(exploreConcat)
                    .expectNext("A", "D", "B", "E", "C", "F")
                    .verifyComplete();
        }

        @Test
        void exploreMergeWith_FluxString_WhenSuccess() {
            //given
            //when
            var exploreConcat = fluxAndMonoGeneratorService.exploreMergeWith();

            //then
            StepVerifier.create(exploreConcat)
                    .expectNext("A", "D", "B", "E", "C", "F")
                    .verifyComplete();
        }

        @Test
        void exploreMergeSequential_FluxString_WhenSuccess() {
            //given
            //when
            var exploreConcat = fluxAndMonoGeneratorService.exploreMergeSequential();

            //then
            StepVerifier.create(exploreConcat)
                    .expectNext("A", "B", "C", "D", "E", "F")
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

        @Test
        void nameMonoFlatMapMany_ListString_WhenSuccess() {
            //given
            //when
            var nameMono = fluxAndMonoGeneratorService.nameMonoFlatMapMany();

            //then
            StepVerifier.create(nameMono)
                    .expectNext("A", "l", "e", "x")
                    .verifyComplete();

        }

        @Test
        void exploreConcatWithMono_FluxString_WhenSuccess() {
            //given
            //when
            var exploreConcat = fluxAndMonoGeneratorService.exploreConcatWithMono();

            //then
            StepVerifier.create(exploreConcat)
                    .expectNext("A", "B")
                    .verifyComplete();
        }

        @Test
        void exploreMergeWithMono_FluxString_WhenSuccess() {
            //given
            //when
            var exploreConcat = fluxAndMonoGeneratorService.exploreMergeWithMono();

            //then
            StepVerifier.create(exploreConcat)
                    .expectNext("B", "A")
                    .verifyComplete();
        }
    }
}