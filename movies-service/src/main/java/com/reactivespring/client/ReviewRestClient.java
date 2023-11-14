package com.reactivespring.client;

import com.reactivespring.domain.Review;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;

import java.net.URI;

@Component
public class ReviewRestClient {

    private final WebClient webClient;

    private final String reviewUrl;

    public ReviewRestClient(WebClient webClient, @Value("${restClient.reviewUrl}") String reviewUrl) {
        this.webClient = webClient;
        this.reviewUrl = reviewUrl;
    }

    public Flux<Review> findAllReviewByMovieInfoId(String movieInfoId) {
        var url = UriComponentsBuilder
                .fromHttpUrl(reviewUrl)
                .queryParam("moviesinfo", movieInfoId)
                .buildAndExpand().toUriString();

        return webClient
                .get()
                .uri(url)
                .retrieve()
                .bodyToFlux(Review.class);
    }
}