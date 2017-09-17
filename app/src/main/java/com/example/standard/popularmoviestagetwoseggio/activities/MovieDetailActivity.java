package com.example.standard.popularmoviestagetwoseggio.activities;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.standard.popularmoviestagetwoseggio.R;
import com.example.standard.popularmoviestagetwoseggio.SimpleDividerItemDecoration;
import com.example.standard.popularmoviestagetwoseggio.dataFromDatabase.MovieContract.MovieEntry;
import com.example.standard.popularmoviestagetwoseggio.dataFromInternet.DetailAdapter;
import com.example.standard.popularmoviestagetwoseggio.dataFromInternet.Movie;
import com.example.standard.popularmoviestagetwoseggio.dataFromInternet.MovieLoader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MovieDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>>, DetailAdapter.DetailAdapterOnclickHandler {
    private static final int LOADER_ID_MOVIE = 0;

    @BindView(R.id.poster_image)
    ImageView poster;

    @BindView(R.id.title_tv)
    TextView titleTextView;
    @BindView(R.id.story_tv)
    TextView storyTextView;
    @BindView(R.id.rating_tv)
    TextView ratingTextView;
    @BindView(R.id.date_tv)
    TextView dateTextView;

    @BindView(R.id.recyclerViewDetail)
    RecyclerView recyclerView;

    @BindView(R.id.ratingBar)
    RatingBar ratingBar;

    @BindView(R.id.favourite_btn)
    Button favouriteBtn;

    @BindView(R.id.pb_loading_indicator_detail)
    ProgressBar progressBar;

    private String releaseDate;
    private DetailAdapter mDetailAdapter;
    private String mUrl;
    private String apiKey;
    private List<Movie> movieItems;
    private String id;
    private String _id;
    private int rowsDeleted;

    private Uri mCurrentMovieUri, mMovieUri;

    private Movie movie;

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
        Intent intent = getIntent();
        Parcelable extras = intent.getParcelableExtra(getString(R.string.intent_key));
        if (extras != null){
            movie = getIntent().getParcelableExtra(getString(R.string.intent_key));
        } else{
            Toast.makeText(this, getString(R.string.toast_message), Toast.LENGTH_LONG).show();
        }

        _id = movie.getM_Id();
        id = movie.getmId();
        apiKey = getString(R.string.api_key);
        mUrl = getString(R.string.detail_url) + id + getString(R.string.detail_trailer_url) + apiKey;

        releaseDate = movie.getmDate();
        // i wanted to show only the year number. Therefore
        String date = releaseDate.substring(0, 4);

        String posterImage = movie.getmPoster();
        Picasso.with(getApplicationContext()).load(posterImage).into(poster);
        titleTextView.setText(movie.getmTitle());
        storyTextView.setText(movie.getmStory());
        dateTextView.setText(date);
        ratingTextView.setText(movie.getmRating());

        /*
        * rating is for the rating text view. It shows sth like 6.2/10
        * in voteRate the last 3 signs will be cutted for the ratingbar, because float rate
        * doesn't accept 6.2/10
        */
        String rating = movie.getmRating();
        String voteRate = rating.substring(0, rating.length() - 3);

        float rate = Float.parseFloat(voteRate) / 2;
        ratingBar.setRating(rate);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(
                getApplicationContext()
        ));

        movieItems = new ArrayList<>();

        /*
        * If the clicked Movie is not a favourite, the _id must be null
        */
        if (_id == null) {
            favouriteBtn.setText(getString(R.string.favourites));
        } else {
            favouriteBtn.setText(getString(R.string.delete));
        }
        mDetailAdapter = new DetailAdapter(this, this, movieItems);
        recyclerView.setAdapter(mDetailAdapter);
        LoaderManager loader = getSupportLoaderManager();
        loader.initLoader(LOADER_ID_MOVIE, null, this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                progressBar.setVisibility(View.VISIBLE);
                //mDetailAdapter.notifyDataSetChanged();
                NavUtils.navigateUpFromSameTask(MovieDetailActivity.this);
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
    public void review() {
        checkConnection();

        progressBar.setVisibility(View.VISIBLE);
        mUrl = getString(R.string.detail_url) + id + getString(R.string.detail_review_url) + apiKey;

        mDetailAdapter.clear();

        getSupportLoaderManager().restartLoader(LOADER_ID_MOVIE, null, this);
    }

    @OnClick(R.id.trailer_btn)
    public void trailer() {
        checkConnection();
        //recyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        mUrl = getString(R.string.detail_url) + id + getString(R.string.detail_trailer_url) + apiKey;
        mDetailAdapter.clear();

        getSupportLoaderManager().restartLoader(LOADER_ID_MOVIE, null, this);
    }

    @OnClick(R.id.favourite_btn)
    public void favouriteMovie() {
        if (favouriteBtn.getText() == getString(R.string.favourites)) {
            favouriteBtn.setText(getString(R.string.delete));
            insertData();
        } else {
            deleteData();
            favouriteBtn.setText(getString(R.string.favourites));
        }
    }

    private void deleteData() {
        if (mCurrentMovieUri == null) {
            if (_id == null) {
                mCurrentMovieUri = mMovieUri;
            } else {
                mCurrentMovieUri = Uri.withAppendedPath(MovieEntry.CONTENT_URI, _id);
            }
        }
        rowsDeleted = getContentResolver().delete(mCurrentMovieUri, null, null);

        if (rowsDeleted == 0) {
            Toast.makeText(this, getString(R.string.delete_failed_edit_activity), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.delete_succed_edit_activity), Toast.LENGTH_SHORT).show();
        }
    }

    private void insertData() {
        ContentValues values = new ContentValues();

        String poster = movie.getmPoster();
        String title = movie.getmTitle();
        String story = movie.getmStory();
        String date = movie.getmDate();
        String rating = movie.getmRating();
        String id = movie.getmId();

        values.put(MovieEntry.COLUMN_MOVIE_POSTER, poster);
        values.put(MovieEntry.COLUMN_MOVIE_TITLE, title);
        values.put(MovieEntry.COLUMN_MOVIE_STORY, story);
        values.put(MovieEntry.COLUMN_MOVIE_DATE, date);
        values.put(MovieEntry.COLUMN_MOVIE_RATING, rating);
        values.put(MovieEntry.COLUMN_MOVIE_ID, id);

        mMovieUri = getContentResolver().insert(MovieEntry.CONTENT_URI, values);

        if (mMovieUri == null) {
            Toast.makeText(this, R.string.movie_not_saved, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.movie_saved, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(Movie data) {
        /*
        * If statement checks if the Trailer List is Visible.
        * The Review List should not be clickable
        */
        if (TextUtils.isEmpty(data.getmReview())) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(getString(R.string.youtube_url) + data.getmKey()));
            // Verify that the intent will resolve to an activity
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        recyclerView.setVisibility(View.VISIBLE);
        return new MovieLoader(this, mUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {
        progressBar.setVisibility(View.GONE);

        if (movies != null && !movies.isEmpty()) {
            mDetailAdapter.add(movies);
            mDetailAdapter.notifyDataSetChanged();
        } else {
            recyclerView.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), getString(R.string.toast_message), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        mDetailAdapter.notifyDataSetChanged();
        mDetailAdapter.clear();
    }
}
