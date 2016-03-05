package com.mrbhatt.popularmovies.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Results {
    private String vote_average;

    private String title;

    private String overview;

    private String release_date;

    private String poster_path;
}
