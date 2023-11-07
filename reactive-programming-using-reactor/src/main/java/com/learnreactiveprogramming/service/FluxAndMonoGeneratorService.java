package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Random;

public class FluxAndMonoGeneratorService {

    public Flux<String> namesFlux() {

        return Flux.fromIterable(List.of(1L, 2L, 3L))
                .transform(fluxOperator -> fluxOperator.map(FluxAndMonoGeneratorService::getNameByCode).map(String::toUpperCase))
                .log();
    }

    public Flux<String> namesFluxFlatMap() {

        return Flux.fromIterable(List.of(1L, 2L, 3L))
                .transform(fluxOperator -> fluxOperator.map(FluxAndMonoGeneratorService::getNameByCode)
                        .map(String::toUpperCase)
                        .flatMap(FluxAndMonoGeneratorService::splitString))
                .log();
    }

    public Flux<String> namesFluxFlatMapAsync() {

        return Flux.fromIterable(List.of(1L, 2L, 3L))
                .transform(fluxOperator -> fluxOperator.map(FluxAndMonoGeneratorService::getNameByCode)
                        .map(String::toUpperCase)
                        .flatMap(FluxAndMonoGeneratorService::splitStringWithDelay))
                .log();
    }

    public Flux<String> namesFluxConcatMapAsync() {

        return Flux.fromIterable(List.of(1L, 2L, 3L))
                .transform(fluxOperator -> fluxOperator.map(FluxAndMonoGeneratorService::getNameByCode)
                        .map(String::toUpperCase)
                        .concatMap(FluxAndMonoGeneratorService::splitStringWithDelay))
                .log();
    }

    public Mono<String> nameMono() {

        return Mono.just(1L).map(FluxAndMonoGeneratorService::getNameByCode).log();
    }

    public Mono<List<String>> nameMonoFlatMap() {

        return Mono.just(1L).map(FluxAndMonoGeneratorService::getNameByCode).flatMap(FluxAndMonoGeneratorService::splitStringMonoWithDelay).log();
    }

    private static Flux<String> splitString(String text) {
        return Flux.fromArray(text.split(""));
    }

    private static Flux<String> splitStringWithDelay(String text) {
        return Flux.fromArray(text.split("")).delayElements(Duration.ofMillis(new Random().nextInt(100)));
    }

    private static Mono<List<String>> splitStringMonoWithDelay(String text) {
        return Mono.just(List.of(text.split("")));
    }

    private static String getNameByCode(Long code) {
        if (code == 1) {
            return "Alex";
        } else if (code == 2) {
            return "Ben";
        } else if (code == 3) {
            return "Cloe";
        }
        return null;
    }

    public static void main(String[] args) {

        FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();


        //Flux
        fluxAndMonoGeneratorService.namesFluxFlatMapAsync()
                .subscribe(System.out::println);

        //Mono

        fluxAndMonoGeneratorService.nameMonoFlatMap().subscribe(System.out::println);
    }
}
