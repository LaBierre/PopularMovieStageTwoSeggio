package com.example.standard.popularmoviestagetwoseggio.activities;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.standard.popularmoviestagetwoseggio.R;
import com.example.standard.popularmoviestagetwoseggio.dataFromInternet.MovieAdapter;
import com.example.standard.popularmoviestagetwoseggio.dataFromDatabase.MovieCursorAdapter;
import com.example.standard.popularmoviestagetwoseggio.dataFromInternet.Movie;
import com.example.standard.popularmoviestagetwoseggio.dataFromInternet.MovieLoader;

import java.util.ArrayList;
import java.util.List;

import com.example.standard.popularmoviestagetwoseggio.dataFromDatabase.MovieContract.MovieEntry;

public class MovieActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler, MovieCursorAdapter.MovieCursorAdapterOnClickHandler{

    private static final int  LOADER_ID_MOVIE = 0;
    private static final int  LOADER_ID_CURSOR = 1;

    private int page;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private MovieAdapter mAdapter;
    private List<Movie> movieItems;
    private String mUrl;
    private String pageString;
    private String apiKey;
    private boolean mDetailedLayout;

    private MovieCursorAdapter mCursorAdapter;


    private LoaderManager.LoaderCallbacks<List<Movie>> movieLoader = new LoaderManager.LoaderCallbacks<List<Movie>>() {
        @Override
        public Loader<List<Movie>> onCreateLoader(int i, Bundle bundle) {
            mDetailedLayout = false;
            return new MovieLoader(getApplicationContext(), mUrl);
        }

        @Override
        public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {
            progressBar.setVisibility(View.GONE);

        /*
        * This if request checks if the home button in the detail activity is clicked and avoids
        * that the lastly loaded page with movies will be added again in the list
        */
            if (!mDetailedLayout){
                if (movies != null && !movies.isEmpty()) {
                    mAdapter.add(movies);
                    mAdapter.notifyDataSetChanged();
                    Log.d("Test", "onLoadFinished MovieActivity");

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
            Log.d("Test", "onLoadReset MovieActivity");
        }
    };

    private LoaderManager.LoaderCallbacks<Cursor> cursorLoader = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

            return new CursorLoader(getApplicationContext(), MovieEntry.CONTENT_URI, null, null, null, null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            // Update {@InventoryAdapter} with this new cursor containing updated product data
            mCursorAdapter.swapCursor(data);
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

        Log.d("Test", "MovieActivity, onCreate");

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

        progressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        int numberOfColumns = getResources().getInteger(R.integer.gallery_columns);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, numberOfColumns);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);

        movieItems = new ArrayList<>();

        mAdapter = new MovieAdapter(this, this, movieItems);

        recyclerView.setAdapter(mAdapter);

        LoaderManager loader = getLoaderManager();
        loader.initLoader(LOADER_ID_MOVIE, null, movieLoader);
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
        LoaderManager loader = getLoaderManager();
        loader.restartLoader(LOADER_ID_MOVIE, null, movieLoader);
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
                recyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                setTitle(getString(R.string.title_popular));
                mUrl = getString(R.string.url_popular) + apiKey;
                mAdapter.clear();
                page = 1;
                LoaderManager loaderPopular = getLoaderManager();
                loaderPopular.restartLoader(LOADER_ID_MOVIE, null, movieLoader);
                return true;
            case R.id.rated:
                checkConnection();
                recyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                setTitle(getString(R.string.title_rating));
                mUrl = getString(R.string.url_rated) + apiKey;
                mAdapter.clear();
                page = 1;
                LoaderManager loaderRated = getLoaderManager();
                loaderRated.restartLoader(LOADER_ID_MOVIE, null, movieLoader);
                return true;
            case R.id.favourites:
                Toast.makeText(this, "Favourites clicked", Toast.LENGTH_SHORT).show();
                mCursorAdapter = new MovieCursorAdapter(this, null, null);
                setTitle("Favourites");
                recyclerView.setAdapter(mCursorAdapter);
                getLoaderManager().initLoader(LOADER_ID_CURSOR, null, cursorLoader);
        }

        return super.onOptionsItemSelected(item);
    }

    /*
    * Starts the detail activity with detail Informations of the clicked movie image
    */
    @Override
    public void onClick(Movie data) {

        //Todo: Send the _id with the intent

        Intent intent = new Intent(this, MovieDetailActivity.class);

        intent.putExtra("data", data);
        startActivity(intent);
    }
}
