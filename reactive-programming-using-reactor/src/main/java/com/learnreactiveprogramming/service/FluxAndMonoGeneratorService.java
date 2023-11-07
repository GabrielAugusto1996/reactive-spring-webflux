package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class FluxAndMonoGeneratorService {

    public Flux<String> namesFlux() {

        return Flux.fromIterable(List.of("Alex", "Ben", "Cloe"));
    }

    public Mono<String> nameMono() {

        return Mono.just("John");
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
