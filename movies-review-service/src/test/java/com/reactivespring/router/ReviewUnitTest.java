package com.reactivespring.router;

import com.reactivespring.domain.Review;
import com.reactivespring.exception.GlobalErrorHandler;
import com.reactivespring.handler.ReviewHandler;
import com.reactivespring.repository.ReviewReactiveRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@WebFluxTest
@ContextConfiguration(classes = {ReviewRouter.class, ReviewHandler.class, GlobalErrorHandler.class})
@ActiveProfiles(profiles = "test")
@AutoConfigureWebTestClient
public class ReviewUnitTest {

    final static String BASE_URI = "/v1/reviews";
    @MockBean
    ReviewReactiveRepository reviewReactiveRepository;

    @Autowired
    WebTestClient webTestClient;

    @Test
    void addReview_Review_WhenSuccess()  {
        final Review review = new Review();
        review.setReviewId("1");
        review.setComment("This movie is good");
        review.setRating(10.0);
        review.setMovieInfoId(1L);

        when(reviewReactiveRepository.save(review)).thenReturn(Mono.just(review));

        webTestClient
                .post()
                .uri(URI.create(BASE_URI))
                .bodyValue(review)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Review.class)
                .consumeWith(result -> {
                    Review responseBody = result.getResponseBody();

                    assertNotNull(responseBody);
                    assertNotNull(responseBody.getReviewId());
                });
    }

    @Test
    void addReview_validation() {
        //given
        final Review review = new Review();
        review.setReviewId("1");
        review.setComment("This movie is good");
        review.setRating(-10.0);
        review.setMovieInfoId(null);

        when(reviewReactiveRepository.save(review)).thenReturn(Mono.just(review));

        //when

        webTestClient
                .post()
                .uri(BASE_URI)
                .bodyValue(review)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(String.class)
                .isEqualTo("rating.movieInfoId : must not be null,rating.negative : rating is negative and please pass a non-negative value");
    }

    @Test
    void findAll_Review_WhenSuccess() {
        final Review review = new Review();
        review.setReviewId("1");
        review.setComment("This movie is good");
        review.setRating(10.0);
        review.setMovieInfoId(1L);

        when(reviewReactiveRepository.findAll()).thenReturn(Flux.fromIterable(List.of(review)));

        webTestClient
                .get()
                .uri(URI.create(BASE_URI))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Review.class)
                .hasSize(1);
    }

    @Test
    void findAllByMovieInfoId_Review_WhenSuccess() {
        final Review review = new Review();
        review.setReviewId("1");
        review.setComment("This movie is good");
        review.setRating(10.0);
        review.setMovieInfoId(1L);

        when(reviewReactiveRepository.findAllByMovieInfoId(1L)).thenReturn(Flux.fromIterable(List.of(review)));

        webTestClient
                .get()
                .uri(URI.create(BASE_URI + "?moviesinfo=1"))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Review.class)
                .hasSize(1);
    }

    @Test
    void updateReview_Review_WhenSuccess() {
        final Review review = new Review();
        review.setComment("This movie is good");
        review.setRating(10.0);
        review.setMovieInfoId(1L);
        review.setReviewId("1");

        when(reviewReactiveRepository.findById("1")).thenReturn(Mono.just(review));
        when(reviewReactiveRepository.save(review)).thenReturn(Mono.just(review));


        webTestClient
                .put()
                .uri(URI.create(BASE_URI + "/" + "1"))
                .bodyValue(review)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Review.class)
                .consumeWith(result -> {
                    Review responseBody = result.getResponseBody();

                    assertNotNull(responseBody);
                    assertNotNull(responseBody.getReviewId());
                    assertEquals("This movie is good", responseBody.getComment());
                });
    }

    @Test
    void updateReview_Review_WhenNotFound() {
        final Review review = new Review();
        review.setComment("This movie is good");
        review.setRating(10.0);
        review.setMovieInfoId(1L);

        when(reviewReactiveRepository.findById("2")).thenReturn(Mono.empty());

        webTestClient
                .put()
                .uri(URI.create(BASE_URI + "/" + "2"))
                .bodyValue(review)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void deleteReview_Review_WhenSuccess() {
        when(reviewReactiveRepository.deleteById("1")).thenReturn(Mono.empty());


        webTestClient
                .delete()
                .uri(URI.create(BASE_URI + "/" + "1"))
                .exchange()
                .expectStatus().isNoContent();

        Flux<Review> reviews = reviewReactiveRepository.findAll();

        StepVerifier.create(reviews)
                .expectNextCount(0L);
    }

}