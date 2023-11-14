package com.reactivespring.controller;

import com.reactivespring.domain.MovieInfo;
import com.reactivespring.service.MovieInfoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/movieinfos")
public class MoviesInfoController {

    Sinks.Many<MovieInfo> moviesInfoSynk = Sinks.many().replay().all();

    private final MovieInfoService movieInfoService;

    public MoviesInfoController(MovieInfoService movieInfoService) {
        this.movieInfoService = movieInfoService;
    }

    @PostMapping
    public Mono<ResponseEntity<MovieInfo>> addMovieInfo(@Valid @RequestBody MovieInfo movieInfo) {

        return this.movieInfoService.addMovie(movieInfo)
                .doOnNext(savedMovieInfo -> moviesInfoSynk.tryEmitNext(savedMovieInfo))
                .map(movie -> ResponseEntity.status(HttpStatus.CREATED).body(movie));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<MovieInfo>> findById(@PathVariable("id") String id) {

        return this.movieInfoService.findById(id)
                .map(movie -> ResponseEntity.status(HttpStatus.OK).body(movie))
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()));
    }


    @GetMapping(value = "/stream", produces = MediaType.APPLICATION_NDJSON_VALUE) // Stream as a JSON
    public Flux<MovieInfo> movieInfoStream() {

        return moviesInfoSynk.asFlux();
    }

    @GetMapping
    public Flux<MovieInfo> findAll() {

        return this.movieInfoService.findAll();
    }

    @GetMapping("/year/{year}")
    public Flux<MovieInfo> findAllByYear(@PathVariable("year") Integer year) {

        return this.movieInfoService.findAllByYear(year);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<MovieInfo>> update(@PathVariable("id") String id, @Valid @RequestBody MovieInfo movieInfo) {

        return this.movieInfoService.update(id, movieInfo)
                .map(movie -> ResponseEntity.status(HttpStatus.OK).body(movie))
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable("id") String id) {

        return this.movieInfoService.delete(id);
    }
}