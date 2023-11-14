package com.reactivespring.client;

import com.reactivespring.domain.MovieInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
public class MovieInfoRestClient {

    private final WebClient webClient;

    private final String movieInfoBaseUrl;

    public MovieInfoRestClient(WebClient webClient, @Value("${restClient.movieInfoUrl}") String movieInfoBaseUrl) {
        this.webClient = webClient;
        this.movieInfoBaseUrl = movieInfoBaseUrl;
    }

    public Mono<MovieInfo> findMovieInfoById(String movieInfoId) {
        return webClient
                .get()
                .uri(URI.create(movieInfoBaseUrl + "/v1/movieinfos/" + movieInfoId))
                .retrieve()
                .bodyToMono(MovieInfo.class);
    }
}