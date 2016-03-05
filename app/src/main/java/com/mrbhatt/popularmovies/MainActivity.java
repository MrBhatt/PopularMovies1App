package com.mrbhatt.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.mrbhatt.popularmovies.async.PullImageTask;
import com.mrbhatt.popularmovies.dto.Results;
import com.mrbhatt.popularmovies.dto.TMDBDiscoverResults;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends Activity {

    private SharedPreferences prefs = null;
    private ImageAdapter imageAdapter = null;
    private String currentSortPreference = null;
    private Results[] results = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView gridview = (GridView) findViewById(R.id.gridview);
        currentSortPreference = getSortPreference();
        imageAdapter = new ImageAdapter(this, getPosters(currentSortPreference));
        gridview.setAdapter(imageAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(parent.getContext(), MovieDetailActivity.class);
                Results result = results[position];

                intent.putExtra(getString(R.string.movieTitle), result.getTitle());
                intent.putExtra(getString(R.string.movieReleaseDate), result.getRelease_date());
                intent.putExtra(getString(R.string.moviePosterPath),
                        String.format(getString(R.string.posterImageUrl), result.getPoster_path()));
                intent.putExtra(getString(R.string.movieRating), result.getVote_average());
                intent.putExtra(getString(R.string.moviePlot), result.getOverview());
                startActivity(intent);
            }
        });
    }

    private String getSortPreference() {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getString(getString(R.string.sortingPreference),
                getString(R.string.DefaultPreference));
    }

    private List<String> getPosters(String sortPref) {
        try {
            TMDBDiscoverResults tmdbDiscoverResults = new PullImageTask().execute(getResources().getString(R.string.discoverMovieUrl),
                    sortPref).get();
            results = tmdbDiscoverResults.getResults();
        } catch (InterruptedException e) {
            Log.e("==EXCEPTION==", e.getMessage());
            e.printStackTrace();
        } catch (ExecutionException e) {
            Log.e("==EXCEPTION==", e.getMessage());
            e.printStackTrace();
        }

        List<String> urls = new ArrayList<>();

        for (Results result : results) {
            String posterPath = result.getPoster_path();
            if (posterPath != null)
                urls.add(String.format(getString(R.string.posterImageUrl), posterPath));
        }

        return urls;
    }

    @Override
    public void onResume() {
        super.onResume();
        String updatedSortPreference = getSortPreference();
        if (!currentSortPreference.equalsIgnoreCase(updatedSortPreference)) {
            imageAdapter.setUrls(getPosters(updatedSortPreference));
            currentSortPreference = updatedSortPreference;
            imageAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.preferences) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * ImageAdapter to be associated with the ImageView (which in turn is associated with a GridView)
     */
    private class ImageAdapter extends BaseAdapter {
        private List<String> urls = new ArrayList<>();
        private Context mContext;

        public ImageAdapter(Context c, List<String> urls) {
            mContext = c;
            this.urls = urls;
        }

        public void setUrls(List<String> urls) {
            this.urls = urls;
        }

        @Override
        public int getCount() {
            return 100;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
            } else {
                imageView = (ImageView) convertView;
            }
            if (position < urls.size())
                Picasso.with(mContext).load(urls.get(position)).into(imageView);
            return imageView;
        }
    }
}
