package com.reactivespring.controller;

import com.reactivespring.domain.MovieInfo;
import com.reactivespring.service.MovieInfoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/movieinfos")
public class MoviesInfoController {

    private final MovieInfoService movieInfoService;

    public MoviesInfoController(MovieInfoService movieInfoService) {
        this.movieInfoService = movieInfoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<MovieInfo> addMovieInfo(@RequestBody MovieInfo movieInfo) {

        return this.movieInfoService.addMovie(movieInfo);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<MovieInfo> findById(@PathVariable("id") String id) {

        return this.movieInfoService.findById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<MovieInfo> findAll() {

        return this.movieInfoService.findAll();
    }
}