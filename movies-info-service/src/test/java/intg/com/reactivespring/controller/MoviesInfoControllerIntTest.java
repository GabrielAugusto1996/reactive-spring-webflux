package com.reactivespring.controller;

import com.reactivespring.domain.MovieInfo;
import com.reactivespring.repository.MovieInfoRepository;
import com.reactivespring.repository.mocks.MovieInfoMock;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
@AutoConfigureWebTestClient
class MoviesInfoControllerIntTest {

    final static String BASE_URI = "/v1/movieinfos";

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    MovieInfoRepository movieInfoRepository;

    @BeforeEach
    void setup() {
        List<MovieInfo> movies = List.of(MovieInfoMock.getMock("1"));

        movieInfoRepository.saveAll(movies).blockLast();
    }

    @AfterEach
    void tearDown() {
        movieInfoRepository.deleteAll().block();
    }

    @Test
    void addMovieInfo_MovieInfo_WhenSuccess()  {
        MovieInfo newMovie = MovieInfoMock.createMock("New Movie");

        webTestClient
                .post()
                .uri(URI.create(BASE_URI))
                .bodyValue(newMovie)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(MovieInfo.class)
                .consumeWith(result -> {
                    MovieInfo responseBody = result.getResponseBody();

                    assertNotNull(responseBody);
                    assertNotNull(responseBody.getMovieInfoId());
                });
    }

    @Test
    void findById_MovieInfo_WhenSuccess()  {
        String id = "1";

        webTestClient
                .get()
                .uri(URI.create(BASE_URI + "/" + id))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Star Wars")
                .jsonPath("$.year").isEqualTo(2005);
    }

    @Test
    void findById_MovieInfo_WhenNotFound()  {
        String id = "2";

        webTestClient
                .get()
                .uri(URI.create(BASE_URI + "/" + id))
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void findAll_MovieInfo_WhenSuccess()  {
        webTestClient
                .get()
                .uri(URI.create(BASE_URI))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(MovieInfo.class)
                .hasSize(1);
    }

    @Test
    void update_MovieInfo_WhenSuccess()  {
        String id = "1";

        MovieInfo movieInfo = MovieInfoMock.getMock(id);
        movieInfo.setName("Star Wars2");

        webTestClient
                .put()
                .uri(URI.create(BASE_URI + "/" + id))
                .bodyValue(movieInfo)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Star Wars2")
                .jsonPath("$.year").isEqualTo(2005);
    }

    @Test
    void update_MovieInfo_WhenNotFound()  {
        String id = "2";

        MovieInfo movieInfo = MovieInfoMock.getMock(id);
        movieInfo.setName("Star Wars2");

        webTestClient
                .put()
                .uri(URI.create(BASE_URI + "/" + id))
                .bodyValue(movieInfo)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void delete_MovieInfo_WhenSuccess()  {
        String id = "1";

        webTestClient
                .delete()
                .uri(URI.create(BASE_URI + "/" + id))
                .exchange()
                .expectStatus().isNoContent();

        Flux<MovieInfo> movieInfoFlux = movieInfoRepository.findAll().log();

        StepVerifier.create(movieInfoFlux)
                .expectNextCount(0L)
                .verifyComplete();
    }
}