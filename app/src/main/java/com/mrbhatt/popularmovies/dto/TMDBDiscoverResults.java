package com.mrbhatt.popularmovies.dto;

import lombok.Data;

@Data
public class TMDBDiscoverResults {
    private Results[] results;

    private String page;

    private String total_pages;

    private String total_results;
}
