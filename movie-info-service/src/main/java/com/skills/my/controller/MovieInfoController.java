package com.skills.my.controller;

import com.skills.my.api.MovieInfoRestPath;
import model.MovieInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(MovieInfoRestPath.BASE_PATH)
public class MovieInfoController {

    @GetMapping(value = "/{movieId}")
    public MovieInfo getMovieInfo(@PathVariable("movieId") String movieId) {
        return new MovieInfo(movieId, "Hellboy");
    }
}
