package com.skills.my.controller;

import model.CatalogItem;
import model.MovieInfo;
import model.Rating;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.skills.my.api.MovieCatalogRestPath.BASE_PATH;

@RestController
@RequestMapping(BASE_PATH)
public class MovieCatalogController {

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping
    @RequestMapping("/{userId}")
    public List<CatalogItem> getMovieCatalog(@PathVariable("userId") String userId) {
        // get all rated movie id by this user
        List<Rating> ratings = Arrays.asList(
                new Rating("1L", 4),
                new Rating("2L", 3),
                new Rating("3L", 2),
                new Rating("4L", 5)
        );

        // for each movie, call movie info service and get details
        return ratings.stream().map(rating -> {
            MovieInfo movie = restTemplate.getForObject("http://localhost:50560/v1/movie-info/"
                    + rating.getMovieId(), MovieInfo.class);
            return new CatalogItem(userId, movie.getMovieName(),
                    "hard coded desc", rating.getRating());
        }).collect(Collectors.toList());
    }
}