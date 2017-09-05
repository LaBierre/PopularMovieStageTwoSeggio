package com.example.standard.popularmoviestagetwoseggio.activities;

import android.app.LoaderManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
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
import com.example.standard.popularmoviestagetwoseggio.adapters.MovieAdapter;
import com.example.standard.popularmoviestagetwoseggio.data.Movie;
import com.example.standard.popularmoviestagetwoseggio.loaders.MovieLoader;

import java.util.ArrayList;
import java.util.List;

public class MovieActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List<Movie>>, MovieAdapter.MovieAdapterOnClickHandler {

    private int page;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private MovieAdapter mAdapter;
    private List<Movie> movieItems;
    private String mUrl;
    private String pageString;
    private String apiKey;
    private boolean mDetailedLayout;

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
        loader.initLoader(0, null, this);
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
        loader.restartLoader(0, null, this);
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
                loaderPopular.restartLoader(0, null, this);
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
                loaderRated.restartLoader(0, null, this);
                return true;
            case R.id.favourites:
                //Todo: Make a link to the database
                Toast.makeText(this, "Favourites clicked", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

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

            } else {
                recyclerView.setVisibility(View.GONE);
                Toast.makeText(this, getString(R.string.toast_message), Toast.LENGTH_LONG).show();
            }
        }
        mDetailedLayout = true;
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        mAdapter.clear();
    }

    /*
    * Starts the detail activity with detail Informations of the clicked movie image
    */
    @Override
    public void onClick(Movie data) {

        Intent intent = new Intent(this, MovieDetailActivity.class);

        intent.putExtra("data", data);
        startActivity(intent);
    }
}
