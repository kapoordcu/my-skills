package com.skills.my.controller;

import com.skills.my.api.MovieInfoRestPath;
import model.MovieInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(MovieInfoRestPath.BASE_PATH)
public class MovieInfoController {

    @GetMapping
    @RequestMapping("/{movieId}")
    public List<MovieInfo> getMovieInfo(@PathVariable("movieId") String movieId) {
        return Collections.singletonList(new MovieInfo(movieId,
                "Hellboy"));
    }
}
