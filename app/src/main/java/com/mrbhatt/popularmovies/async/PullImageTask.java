package com.mrbhatt.popularmovies.async;

import android.os.AsyncTask;
import android.util.Log;

import com.mrbhatt.popularmovies.dto.TMDBDiscoverResults;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class PullImageTask extends AsyncTask<String, Void, TMDBDiscoverResults> {
    @Override
    protected TMDBDiscoverResults doInBackground(String... params) {
        try {
            final String url = String.format(params[0], params[1]);
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            return restTemplate.getForObject(url, TMDBDiscoverResults.class);
        } catch (Exception e) {
            Log.e("MainActivity", e.getMessage(), e);
        }
        return null;
    }
}