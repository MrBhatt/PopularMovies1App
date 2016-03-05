package com.mrbhatt.popularmovies;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        setTitle(getString(R.string.MovieDetailPageHeading));
        getActionBar().setDisplayHomeAsUpEnabled(true);

        TextView titleTextView = (TextView) findViewById(R.id.movieTitle);
        TextView releaseDateTextView = (TextView) findViewById(R.id.movieReleaseDate);
        ImageView posterImageView = (ImageView) findViewById(R.id.moviePoster);
        TextView movieRatingTextView = (TextView) findViewById(R.id.movieRating);
        TextView moviePlotTextView = (TextView) findViewById(R.id.moviePlot);

        Intent intent = getIntent();
        titleTextView.setText(intent.getStringExtra(getString(R.string.movieTitle)));
        releaseDateTextView.setText(intent.getStringExtra(getString(R.string.movieReleaseDate)));
        Picasso.with(getApplicationContext())
                .load(intent.getStringExtra(getString(R.string.moviePosterPath)))
                .into(posterImageView);
        movieRatingTextView.setText(intent.getStringExtra(getString(R.string.movieRating)));
        moviePlotTextView.setText(intent.getStringExtra(getString(R.string.moviePlot)));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.finish();
        return super.onOptionsItemSelected(item);
    }
}