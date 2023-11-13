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

import java.net.URI;
import java.util.List;

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
        review.setReviewId(null);
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

}