package com.skills.my.controller;

import com.skills.my.api.RatingDataRestPath;
import com.skills.my.model.Rating;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(RatingDataRestPath.BASE_PATH)
public class RatingDataController {

    @GetMapping
    @RequestMapping("/{movieId}")
    public Rating getRating(@PathVariable("movieId") String movieId) {
        return new Rating(movieId,4);
    }
}