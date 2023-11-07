package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class FluxAndMonoGeneratorService {

    public Flux<String> namesFlux() {

        return Flux.fromIterable(List.of(1L, 2L, 3L))
                .transform(fluxOperator -> fluxOperator.map(FluxAndMonoGeneratorService::getNameByCode).map(String::toUpperCase))
                .log();
    }

    public Mono<String> nameMono() {

        return Mono.just("John").log();
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
        fluxAndMonoGeneratorService.namesFlux()
                .subscribe(System.out::println);

        //Mono

        fluxAndMonoGeneratorService.nameMono().subscribe(System.out::println);
    }
}
