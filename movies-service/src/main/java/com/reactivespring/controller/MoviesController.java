package com.reactivespring.controller;

import com.reactivespring.client.MovieInfoRestClient;
import com.reactivespring.client.ReviewRestClient;
import com.reactivespring.domain.Movie;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/movies")
public class MoviesController {

    private final MovieInfoRestClient movieInfoRestClient;
    private final ReviewRestClient reviewRestClient;

    public MoviesController(MovieInfoRestClient movieInfoRestClient, ReviewRestClient reviewRestClient) {
        this.movieInfoRestClient = movieInfoRestClient;
        this.reviewRestClient = reviewRestClient;
    }

    @GetMapping("/{id}")
    public Mono<Movie> retrieveMovieById(@PathVariable("id") String movieId) {

        return movieInfoRestClient
                .findMovieInfoById(movieId)
                .flatMap(movieInfo -> {
                    var reviewMonoList = reviewRestClient.findAllReviewByMovieInfoId(movieId).collectList();

                    return reviewMonoList.map(reviews -> new Movie(movieInfo, reviews));
                });
    }
}