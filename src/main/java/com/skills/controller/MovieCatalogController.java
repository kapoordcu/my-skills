package com.skills.controller;

import com.skills.model.CatalogItem;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

import static com.skills.api.MovieCatalogRestPath.BASE_PATH;

@RestController
@RequestMapping(BASE_PATH)
public class MovieCatalogController {

    @GetMapping
    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String uuid) {
        return Collections.singletonList(new CatalogItem("Hellboy",
                "Action movie for 1990", 3));
    }
}
