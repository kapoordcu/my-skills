package com.skills.my.controller;

import model.CatalogItem;
import model.MovieInfo;
import model.Rating;
import model.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.skills.my.api.MovieCatalogRestPath.BASE_PATH;

@RestController
@RequestMapping(BASE_PATH)
public class MovieCatalogController {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    WebClient webClient;

    @GetMapping
    @RequestMapping("/{userId}")
    public List<CatalogItem> getMovieCatalog(@PathVariable("userId") String userId) {
        // get all rated movie id by this user
        UserRating ratings = restTemplate.getForObject("http://localhost:50570/v1/rating-data/users/" + userId, UserRating.class);
        List<CatalogItem> catalogItems = new ArrayList<>();
        // for each movie, call movie info service and get details
        return ratings.getRatingList().stream().map(rating -> {
            MovieInfo movie = restTemplate.getForObject("http://localhost:50560/v1/movie-info/"
                    + rating.getMovieId(), MovieInfo.class);
//            MovieInfo movieByWebClient = webClient.get()
//                    .uri("http://localhost:50560/v1/movie-info/"
//                            + rating.getMovieId())
//                    .retrieve()
//                    .bodyToMono(MovieInfo.class).block();
            return new CatalogItem("", movie.getMovieName(), "desc", rating.getRating());
        }).collect(Collectors.toList());
    }
}