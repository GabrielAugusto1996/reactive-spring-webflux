package com.reactivespring.router;

import com.reactivespring.domain.Review;
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
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@WebFluxTest
@ContextConfiguration(classes = {ReviewRouter.class, ReviewHandler.class})
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

}