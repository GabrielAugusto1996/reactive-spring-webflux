package com.reactivespring.handler;

import com.reactivespring.domain.Review;
import com.reactivespring.repository.ReviewReactiveRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
public class ReviewHandler {

    private final ReviewReactiveRepository reviewReactiveRepository;

    public ReviewHandler(ReviewReactiveRepository reviewReactiveRepository) {
        this.reviewReactiveRepository = reviewReactiveRepository;
    }

    public Mono<ServerResponse> addReview(ServerRequest request) {
        return request.bodyToMono(Review.class)
                .flatMap(reviewReactiveRepository::save)
                .flatMap(savedReview -> ServerResponse.status(HttpStatus.CREATED).bodyValue(savedReview));
    }

    public Mono<ServerResponse> findAll(ServerRequest serverRequest) {
        Optional<String> movieInfoId = serverRequest.queryParam("moviesinfo");

        Flux<Review> reviews = movieInfoId.isPresent()
                ? reviewReactiveRepository.findAllByMovieInfoId(Long.parseLong(movieInfoId.get()))
                : reviewReactiveRepository.findAll();

        return ServerResponse.ok().body(reviews, Review.class);
    }

    public Mono<ServerResponse> updateReview(ServerRequest serverRequest) {
        String reviewId = serverRequest.pathVariable("id");

        var existingReview = reviewReactiveRepository.findById(reviewId);

        return existingReview.flatMap(review -> serverRequest.bodyToMono(Review.class)
                        .map(reqReview -> {
                            review.setComment(reqReview.getComment());
                            review.setRating(reqReview.getRating());

                            return review;
                        }))
                .flatMap(reviewReactiveRepository::save)
                .flatMap(review -> ServerResponse.ok().bodyValue(review))
                .switchIfEmpty(ServerResponse.status(HttpStatus.NOT_FOUND).build());
    }

    public Mono<ServerResponse> deleteReview(ServerRequest serverRequest) {
        return this.reviewReactiveRepository.deleteById(serverRequest.pathVariable("id"))
                .then(ServerResponse.noContent().build());
    }
}
