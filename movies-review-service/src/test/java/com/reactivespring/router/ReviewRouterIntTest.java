package com.reactivespring.router;

import com.reactivespring.domain.Review;
import com.reactivespring.repository.ReviewReactiveRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
@AutoConfigureWebTestClient
class ReviewRouterIntTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ReviewReactiveRepository reviewReactiveRepository;

    static String BASE_URI = "/v1/reviews";

    @BeforeEach
    void setup() {
        final Review review = new Review();
        review.setReviewId("1");
        review.setComment("This movie is amazing");
        review.setRating(10.0);
        review.setMovieInfoId(1L);

        List<Review> reviews = List.of(review);

        reviewReactiveRepository.saveAll(reviews).blockLast();
    }

    @AfterEach
    void tearDown() {
        reviewReactiveRepository.deleteAll().block();
    }

    @Test
    void addReview_Review_WhenSuccess() {
        final Review review = new Review();
        review.setReviewId(null);
        review.setComment("This movie is good");
        review.setRating(10.0);
        review.setMovieInfoId(1L);

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
    void findAll_Review_WhenSuccess() {
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

        webTestClient
                .put()
                .uri(URI.create(BASE_URI + "/" + "2"))
                .bodyValue(review)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void deleteReview_Review_WhenSuccess() {
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