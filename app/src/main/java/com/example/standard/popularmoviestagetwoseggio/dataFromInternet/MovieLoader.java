package com.example.standard.popularmoviestagetwoseggio.dataFromInternet;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

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
