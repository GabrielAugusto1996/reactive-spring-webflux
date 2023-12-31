package com.reactivespring.repository;

import com.reactivespring.domain.MovieInfo;
import com.reactivespring.repository.mocks.MovieInfoMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataMongoTest
@ActiveProfiles(profiles = "test")
class MovieInfoRepositoryIntTest {

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
    void findAll_MovieInfo_WhenSuccess() {
        //given
        //when
        Flux<MovieInfo> movies = movieInfoRepository.findAll().log();

        //then
        StepVerifier.create(movies)
                .expectNextCount(1L)
                .verifyComplete();
    }

    @Test
    void findById_MovieInfo_WhenSuccess() {
        //given
        var movieId = "1";

        //when
        Mono<MovieInfo> movieInfo = movieInfoRepository.findById(movieId).log();

        //then
        StepVerifier.create(movieInfo)
                .consumeNextWith(result -> {
                    assertEquals("Star Wars", result.getName());
                    assertEquals(2005, result.getYear());
                })
                .verifyComplete();
    }

    @Test
    void findOneByName_MovieInfo_WhenSuccess() {
        //given
        var name = "Star Wars";

        //when
        Mono<MovieInfo> movieInfo = movieInfoRepository.findOneByName(name).log();

        //then
        StepVerifier.create(movieInfo)
                .consumeNextWith(result -> {
                    assertEquals("Star Wars", result.getName());
                    assertEquals(2005, result.getYear());
                })
                .verifyComplete();
    }

    @Test
    void save_MovieInfo_WhenSuccess() {
        //given
        var name = "Star Wars: New Hope";

        MovieInfo movieInfoToSave = MovieInfoMock.createMock(name);

        //when
        Mono<MovieInfo> movieInfo = movieInfoRepository.save(movieInfoToSave).log();

        //then
        StepVerifier.create(movieInfo)
                .consumeNextWith(result -> {
                    assertNotNull(result.getMovieInfoId());
                    assertEquals("Star Wars: New Hope", result.getName());
                    assertEquals(2005, result.getYear());
                })
                .verifyComplete();
    }

    @Test
    void update_MovieInfo_WhenSuccess() {
        //given
        var name = "Star Wars2";

        MovieInfo movieInfoToSave = MovieInfoMock.getMock("1");
        movieInfoToSave.setName(name);

        //when
        Mono<MovieInfo> movieInfo = movieInfoRepository.save(movieInfoToSave).log();

        //then
        StepVerifier.create(movieInfo)
                .consumeNextWith(result -> {
                    assertNotNull(result.getMovieInfoId());
                    assertEquals("Star Wars2", result.getName());
                    assertEquals(2005, result.getYear());
                })
                .verifyComplete();
    }

    @Test
    void delete_MovieInfo_WhenSuccess() {
        //given
        var movieId = "1";

        //when
        movieInfoRepository.deleteById(movieId).block();
        Flux<MovieInfo> movieInfoFlux = movieInfoRepository.findAll().log();

        //then
        StepVerifier.create(movieInfoFlux)
                .expectNextCount(0L)
                .verifyComplete();
    }

}