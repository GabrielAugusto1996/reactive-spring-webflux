package com.reactivespring.service;

import com.reactivespring.domain.MovieInfo;
import com.reactivespring.repository.MovieInfoRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MovieInfoService {

    private final MovieInfoRepository movieInfoRepository;

    public MovieInfoService(MovieInfoRepository movieInfoRepository) {
        this.movieInfoRepository = movieInfoRepository;
    }

    public Mono<MovieInfo> addMovie(MovieInfo movieInfo) {
        return this.movieInfoRepository.save(movieInfo);
    }

    public Mono<MovieInfo> findById(String id) {
        return this.movieInfoRepository.findById(id);
    }

    public Flux<MovieInfo> findAll() {
        return this.movieInfoRepository.findAll();
    }

    public Mono<MovieInfo> update(String id, MovieInfo movieInfo) {
        return this.movieInfoRepository.findById(id).flatMap(movieInfoToUpdate -> {
            movieInfoToUpdate.setName(movieInfo.getName());
            movieInfoToUpdate.setYear(movieInfo.getYear());
            movieInfoToUpdate.setReleaseDate(movieInfo.getReleaseDate());
            movieInfoToUpdate.setCast(movieInfo.getCast());

            return this.movieInfoRepository.save(movieInfoToUpdate);
        });
    }

    public Mono<Void> delete(String id) {
        return this.movieInfoRepository.deleteById(id);
    }
}