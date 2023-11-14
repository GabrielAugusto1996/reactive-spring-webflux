package com.reactivespring.client;

import com.reactivespring.domain.MovieInfo;
import com.reactivespring.exception.MoviesInfoClientException;
import com.reactivespring.exception.MoviesInfoServerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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
        System.out.println("Making a call to find a movie by ID:" + movieInfoId);
        return webClient
                .get()
                .uri(URI.create(movieInfoBaseUrl + "/v1/movieinfos/" + movieInfoId))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> {
                    if (clientResponse.statusCode().equals(HttpStatus.NOT_FOUND)) {
                        return Mono.error(new MoviesInfoClientException("There is no movie info available by retriving ID:" + movieInfoId, clientResponse.statusCode().value()));
                    }

                    return clientResponse
                            .bodyToMono(String.class)
                            .flatMap(responseMessage -> Mono.error(new MoviesInfoClientException(responseMessage, clientResponse.statusCode().value())));
                })
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> clientResponse
                        .bodyToMono(String.class)
                        .flatMap(responseMessage -> Mono.error(new MoviesInfoServerException(responseMessage))))
                .bodyToMono(MovieInfo.class)
                .retry(3L);
    }
}