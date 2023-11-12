package com.reactivespring.mocks;

import com.reactivespring.domain.MovieInfo;

import java.time.LocalDate;
import java.util.List;

public final class MovieInfoMock {

    private MovieInfoMock() {}

    public static MovieInfo getMock(String id) {
        final MovieInfo movieInfo = new MovieInfo();

        movieInfo.setMovieInfoId(id);
        movieInfo.setName("Star Wars");
        movieInfo.setYear(2005);
        movieInfo.setReleaseDate(LocalDate.of(2005, 12, 30));
        movieInfo.setCast(List.of("Anakin", "Obi-wan", "Luke", "Han Solo"));

        return movieInfo;
    }

    public static MovieInfo createMock(String name) {
        final MovieInfo movieInfo = new MovieInfo();


        movieInfo.setName(name);
        movieInfo.setYear(2005);
        movieInfo.setReleaseDate(LocalDate.of(2005, 12, 30));
        movieInfo.setCast(List.of("Anakin", "Obi-wan", "Luke", "Han Solo"));

        return movieInfo;
    }
}