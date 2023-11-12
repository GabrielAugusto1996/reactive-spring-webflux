package com.reactivespring.controller;

import com.reactivespring.domain.MovieInfo;
import com.reactivespring.repository.mocks.MovieInfoMock;
import com.reactivespring.service.MovieInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = MoviesInfoController.class)
@ActiveProfiles(profiles = "test")
@AutoConfigureWebTestClient
class MoviesInfoControllerTest {

    final static String BASE_URI = "/v1/movieinfos";
    @MockBean
    MovieInfoService movieInfoService;

    @Autowired
    WebTestClient webTestClient;

    @Test
    void addMovieInfo_MovieInfo_WhenSuccess()  {
        MovieInfo newMovie = MovieInfoMock.getMock("1");
        when(movieInfoService.addMovie(newMovie)).thenReturn(Mono.just(newMovie));

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
        when(movieInfoService.findById("1")).thenReturn(Mono.just(MovieInfoMock.getMock("1")));

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
    void findAll_MovieInfo_WhenSuccess()  {
        List<MovieInfo> movies = List.of(MovieInfoMock.getMock("1"));

        when(this.movieInfoService.findAll())
                .thenReturn(Flux.fromIterable(movies));

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

        when(movieInfoService.update(id, movieInfo)).thenReturn(Mono.just(movieInfo));

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
    void delete_MovieInfo_WhenSuccess()  {
        String id = "1";

        when(movieInfoService.delete(id)).thenReturn(Mono.empty());

        webTestClient
                .delete()
                .uri(URI.create(BASE_URI + "/" + id))
                .exchange()
                .expectStatus().isNoContent();

    }
}