package com.reactivespring.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Random;

@RestController
public class FluxAndMonoController {

    @GetMapping("flux")
    public Flux<Integer> flux() {
        return Flux.just(new Random().nextInt(), new Random().nextInt(), new Random().nextInt()).log();
    }

    @GetMapping("mono")
    public Mono<Integer> mono() {
        return Mono.just(new Random().nextInt()).log();
    }
}