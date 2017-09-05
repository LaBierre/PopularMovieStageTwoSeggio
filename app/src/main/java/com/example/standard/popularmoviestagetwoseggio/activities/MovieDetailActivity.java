package com.example.standard.popularmoviestagetwoseggio.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.app.LoaderManager;
import android.content.Loader;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.standard.popularmoviestagetwoseggio.R;
import com.example.standard.popularmoviestagetwoseggio.adapters.DetailAdapter;
import com.example.standard.popularmoviestagetwoseggio.adapters.MovieAdapter;
import com.example.standard.popularmoviestagetwoseggio.data.Movie;
import com.example.standard.popularmoviestagetwoseggio.loaders.MovieLoader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MovieDetailActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List<Movie>>, DetailAdapter.DetailAdapterOnclickHandler
{
    @BindView(R.id.poster_image)
    ImageView poster;

    @BindView(R.id.title_tv)
    TextView titleTextView;
    @BindView(R.id.story_tv)
    TextView overviewTextView;
    @BindView(R.id.rating_tv)
    TextView ratingTextView;
    @BindView(R.id.date_tv)
    TextView dateTextView;

    @BindView(R.id.recyclerViewDetail)
    RecyclerView recyclerView;

    @BindView(R.id.ratingBar)
    RatingBar ratingBar;

    private String releaseDate;
    private DetailAdapter mAdapter;
    private String mUrl;
    private String apiKey;
    private List<Movie> movieItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        ButterKnife.bind(this);

        setTitle(getString(R.string.title_details));

        checkConnection();

        /*
        * Receive Data from MovieActivity and set them into Views of detail layout
        */
        Movie movie = getIntent().getParcelableExtra("data");

        String id = movie.getmId();
        apiKey = getString(R.string.api_key);
        mUrl = getString(R.string.detail_url) + id + getString(R.string.detail_trailer_url) + apiKey;

        Log.d("Test", "Url = " + mUrl);

        // http://api.themoviedb.org/3/movie/211672/videos?api_key=a495cc93d785e9175db5853bcdb8604a

        releaseDate = movie.getmDate();

        String date = releaseDate.substring(0,4);

        String posterImage = movie.getmPoster();
        Picasso.with(this).load(posterImage).into(poster);
        titleTextView.setText(movie.getmTitle());
        overviewTextView.setText(movie.getmOverview());
        dateTextView.setText(date);
        ratingTextView.setText(movie.getmRating());

        float rate = Float.parseFloat(movie.getmRating())/2;
        ratingBar.setRating(rate);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        movieItems = new ArrayList<>();

        mAdapter = new DetailAdapter(this, this, movieItems);

        recyclerView.setAdapter(mAdapter);

        LoaderManager loader = getLoaderManager();
        loader.initLoader(0, null, this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MovieActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
        return true;
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

    @OnClick(R.id.review_btn)
    public void review (){
        Toast.makeText(this, "Review clicked!", Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.trailer_btn)
    public void trailer (){
        Toast.makeText(this, "Trailer clicked!", Toast.LENGTH_LONG).show();
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int i, Bundle bundle) {
        return new MovieLoader(getApplicationContext(), mUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies)
    {
        if (movies != null && !movies.isEmpty()) {
            mAdapter.add(movies);
            mAdapter.notifyDataSetChanged();
            movies.get(0);

        } else {
            recyclerView.setVisibility(View.GONE);
            Toast.makeText(this, getString(R.string.toast_message), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader)
    {
        mAdapter.clear();
    }

    @Override
    public void onClick(Movie data)
    {
        //Todo: Implicit Intent for calling youtube in a browser
    }
}
