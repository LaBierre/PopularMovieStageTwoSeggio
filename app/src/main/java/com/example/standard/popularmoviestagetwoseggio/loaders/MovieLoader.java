package com.example.standard.popularmoviestagetwoseggio.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.standard.popularmoviestagetwoseggio.data.Movie;
import com.example.standard.popularmoviestagetwoseggio.utils.MovieUtils;

import java.util.List;

/**
 * Created by vince on 03.09.2017.
 */

public class MovieLoader extends AsyncTaskLoader<List<Movie>> {

    private String mUrl;

    public MovieLoader(Context context, String mUrl) {
        super(context);
        this.mUrl = mUrl;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Movie> loadInBackground() {

        if (mUrl == null) {
            return null;
        }
        List<Movie> movies = MovieUtils.fetchMovieData(getContext(), mUrl);
        return movies;
    }
}
