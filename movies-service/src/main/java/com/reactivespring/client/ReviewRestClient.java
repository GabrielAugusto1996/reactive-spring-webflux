package com.reactivespring.client;

import com.reactivespring.domain.Review;
import com.reactivespring.exception.ReviewsClientException;
import com.reactivespring.exception.ReviewsServerException;
import com.reactivespring.util.RetryUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
                .fromHttpUrl(reviewUrl + "/v1/reviews")
                .queryParam("moviesinfo", movieInfoId)
                .buildAndExpand().toUriString();

        return webClient
                .get()
                .uri(url)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> {
                    if (clientResponse.statusCode().equals(HttpStatus.NOT_FOUND)) {
                        return Mono.error(new ReviewsClientException("There is no review info available by retrieving ID:" + movieInfoId));
                    }

                    return clientResponse
                            .bodyToMono(String.class)
                            .flatMap(responseMessage -> Mono.error(new ReviewsClientException(responseMessage)));
                })
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> clientResponse
                        .bodyToMono(String.class)
                        .flatMap(responseMessage -> Mono.error(new ReviewsServerException(responseMessage))))
                .bodyToFlux(Review.class)
                .retryWhen(RetryUtil.retrySpec());
    }
}