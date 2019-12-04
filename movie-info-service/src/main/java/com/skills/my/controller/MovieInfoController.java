package com.skills.my.controller;

import com.skills.my.model.MovieInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

import static com.skills.my.api.MovieInfoRestPath.BASE_PATH;

@RestController
@RequestMapping(BASE_PATH)
public class MovieInfoController {

    @GetMapping
    @RequestMapping("/{movieId}")
    public List<MovieInfo> getCatalog(@PathVariable("movieId") String movieId) {
        return Collections.singletonList(new MovieInfo(movieId,
                "Hellboy"));
    }
}