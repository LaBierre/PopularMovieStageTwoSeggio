package com.example.standard.popularmoviestagetwoseggio.activities;


import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.standard.popularmoviestagetwoseggio.R;
import com.example.standard.popularmoviestagetwoseggio.dataFromDatabase.MovieContract;
import com.example.standard.popularmoviestagetwoseggio.dataFromDatabase.MovieCursorAdapter;
import com.example.standard.popularmoviestagetwoseggio.dataFromInternet.Movie;
import com.example.standard.popularmoviestagetwoseggio.dataFromInternet.MovieAdapter;
import com.example.standard.popularmoviestagetwoseggio.dataFromInternet.MovieLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler, MovieCursorAdapter.MovieCursorAdapterOnClickHandler {

    private static final int LOADER_ID_MOVIE = 0;
    private static final int LOADER_ID_CURSOR = 1;

    private static final String FAVOURITES_VISIBILITY_STATE = "placeholder";
    private static final String CURSOR_COUNT_STATE = "cursorCount";
    @BindView(R.id.pb_loading_indicator)
    ProgressBar progressBar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.load_more_btn)
    Button loadMoreBtn;

    int mCursorCount;
    private int page;
    private MovieAdapter mAdapter;
    private MovieCursorAdapter mCursorAdapter;
    private List<Movie> movieItems;
    private String mUrl;
    private String pageString;
    private String apiKey;
    private boolean mDetailedLayout;
    private boolean mFavouritesVisible;
    private LoaderManager.LoaderCallbacks<List<Movie>> movieLoader = new android.support.v4.app.LoaderManager.LoaderCallbacks<List<Movie>>() {
        @Override
        public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
            mDetailedLayout = false;
            return new MovieLoader(getApplicationContext(), mUrl);
        }

        @Override
        public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            /*
            * This if request checks if the home button in the detail activity is clicked and avoids
            * that the lastly loaded page with movies will be added again in the list
            */
            if (!mDetailedLayout) {
                if (movies != null && !movies.isEmpty()) {
                    mAdapter.add(movies);
                    mAdapter.notifyDataSetChanged();
                } else {
                    recyclerView.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), getString(R.string.toast_message), Toast.LENGTH_LONG).show();
                }
            }
            mDetailedLayout = true;
        }

        @Override
        public void onLoaderReset(Loader<List<Movie>> loader) {
            mAdapter.clear();
        }
    };

    private LoaderManager.LoaderCallbacks<Cursor> cursorLoader = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new CursorLoader(getApplicationContext(), MovieContract.MovieEntry.CONTENT_URI, null, null, null, null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            progressBar.setVisibility(View.GONE);
            loadMoreBtn.setVisibility(View.GONE);

            mCursorCount = mCursorAdapter.cursorCount(data);

            if (mCursorCount == 0) {
                recyclerView.setVisibility(View.GONE);
                mFavouritesVisible = true;
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                mFavouritesVisible = true;
                // Update {@InventoryAdapter} with this new cursor containing updated product data
                mCursorAdapter.swapCursor(data);
            }

        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            mCursorAdapter.swapCursor(null);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        ButterKnife.bind(this);

        /*
        * mDetailedLayout = false means that the home button in MovieDetailActivity is not clicked
        * mDetailedLayout = true means that the home button in MovieDetailActivity is clicked
        */
        mDetailedLayout = false;

        checkConnection();

        setTitle(getString(R.string.title_popular));

        page = 1;
        pageString = getString(R.string.page_appendix);

        apiKey = getString(R.string.api_key);
        mUrl = getString(R.string.url_popular) + apiKey;

        int numberOfColumns = getResources().getInteger(R.integer.gallery_columns);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, numberOfColumns);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);

        movieItems = new ArrayList<>();

        mAdapter = new MovieAdapter(this, this, movieItems);
        recyclerView.setAdapter(mAdapter);

        /*
        * These lines keep the state of the cursorAdapter when screen rotates-----------------------
        */
        if (savedInstanceState != null) {
            mFavouritesVisible = savedInstanceState.getBoolean(FAVOURITES_VISIBILITY_STATE);
            mCursorCount = savedInstanceState.getInt(CURSOR_COUNT_STATE);
        }

        if (!mFavouritesVisible) {
            LoaderManager loader = getSupportLoaderManager();
            loader.initLoader(LOADER_ID_MOVIE, null, movieLoader);
        } else {
            if (mCursorCount > 0) {
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                mCursorAdapter = new MovieCursorAdapter(this, this, null);
                recyclerView.setAdapter(mCursorAdapter);
                getSupportLoaderManager().initLoader(LOADER_ID_CURSOR, null, cursorLoader);
            } else {
                recyclerView.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                loadMoreBtn.setVisibility(View.GONE);
            }
        }
        //------------------------------------------------------------------------------------------
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(FAVOURITES_VISIBILITY_STATE, mFavouritesVisible);
        outState.putInt(CURSOR_COUNT_STATE, mCursorCount);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCursorAdapter != null && mCursorCount > 0) {
            getSupportLoaderManager().restartLoader(LOADER_ID_CURSOR, null, cursorLoader);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mCursorAdapter != null && mCursorCount > 0) {
            getSupportLoaderManager().restartLoader(LOADER_ID_CURSOR, null, cursorLoader);
        }
    }

    /*
     * This method checks the Internet Connectivity and produce an alert if there is no connection
    */
    public void checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        Boolean connectivity = networkInfo != null && networkInfo.isConnectedOrConnecting();

        if (!connectivity) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.alert_title))
                    .setMessage(getString(R.string.alert_message))
                    .setPositiveButton(getString(R.string.dialog_positive_button), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            Intent i = getBaseContext().getPackageManager().
                                    getLaunchIntentForPackage(getBaseContext().getPackageName());
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                        }
                    })
                    .setIcon(R.drawable.internet_connection)
                    .show();
        }
    }

    /*
    * If the user clicks this Button, the next 20 Movies will be loaded
    */
    public void loadMoreBtn(View v) {
        progressBar.setVisibility(View.VISIBLE);
        checkConnection();
        page++;
        mUrl = mUrl + pageString + page;
        getSupportLoaderManager().restartLoader(LOADER_ID_MOVIE, null, movieLoader);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.popular:
                checkConnection();
                popularRatedVisibility();
                setTitle(getString(R.string.title_popular));
                mUrl = getString(R.string.url_popular) + apiKey;

                if (mCursorAdapter != null) {
                    mAdapter.clear();
                    recyclerView.setAdapter(mAdapter);
                    getSupportLoaderManager().restartLoader(LOADER_ID_MOVIE, null, movieLoader);
                } else {
                    mAdapter.clear();
                    getSupportLoaderManager().restartLoader(LOADER_ID_MOVIE, null, movieLoader);
                }
                page = 1;
                return true;
            case R.id.rated:
                checkConnection();
                popularRatedVisibility();
                setTitle(getString(R.string.title_rating));
                mUrl = getString(R.string.url_rated) + apiKey;

                if (mCursorAdapter != null) {
                    mAdapter.clear();
                    recyclerView.setAdapter(mAdapter);
                    getSupportLoaderManager().restartLoader(LOADER_ID_MOVIE, null, movieLoader);
                } else {
                    mAdapter.clear();
                    getSupportLoaderManager().restartLoader(LOADER_ID_MOVIE, null, movieLoader);
                }
                page = 1;

                return true;
            case R.id.favourites:
                mFavouritesVisible = true;
                progressBar.setVisibility(View.VISIBLE);
                setTitle(getString(R.string.favourites));
                if (mCursorAdapter != null) {
                    recyclerView.setAdapter(mCursorAdapter);
                    getSupportLoaderManager().restartLoader(LOADER_ID_CURSOR, null, cursorLoader);
                    getSupportLoaderManager().destroyLoader(LOADER_ID_MOVIE);
                } else {
                    mCursorAdapter = new MovieCursorAdapter(this, this, null);
                    recyclerView.setAdapter(mCursorAdapter);
                    getSupportLoaderManager().initLoader(LOADER_ID_CURSOR, null, cursorLoader);
                    getSupportLoaderManager().destroyLoader(LOADER_ID_MOVIE);
                }
        }
        return super.onOptionsItemSelected(item);
    }

    public void popularRatedVisibility() {
        recyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        loadMoreBtn.setVisibility(View.VISIBLE);
        mFavouritesVisible = false;
    }

    /*
    * Starts the detail activity with detail Informations of the clicked movie image
    */
    @Override
    public void onClick(Movie data) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(getString(R.string.intent_key), data);
        startActivity(intent);
    }
}
