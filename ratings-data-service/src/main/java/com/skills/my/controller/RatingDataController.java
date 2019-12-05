package com.skills.my.controller;

import com.skills.my.api.RatingDataRestPath;
import model.Rating;
import model.UserRating;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(RatingDataRestPath.BASE_PATH)
public class RatingDataController {

    @GetMapping
    @RequestMapping("/{movieId}")
    public Rating getRating(@PathVariable("movieId") String movieId) {
        return new Rating(movieId,4);
    }

    @GetMapping
    @RequestMapping("/users/{userId}")
    public UserRating getRatings(@PathVariable("userId") String userId) {
        List<Rating> ratings = Arrays.asList(
                new Rating("1L", 4),
                new Rating("2L", 3),
                new Rating("3L", 2),
                new Rating("4L", 5)
        );
        UserRating userRating = new UserRating();
        userRating.setRatingList(ratings);
        return userRating;
    }
}