package com.example.standard.popularmoviestagetwoseggio.activities;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.example.standard.popularmoviestagetwoseggio.dataFromDatabase.DetailCursorAdapter;
import com.example.standard.popularmoviestagetwoseggio.dataFromDatabase.MovieContract;
import com.example.standard.popularmoviestagetwoseggio.dataFromInternet.DetailAdapter;
import com.example.standard.popularmoviestagetwoseggio.dataFromInternet.Movie;
import com.example.standard.popularmoviestagetwoseggio.dataFromInternet.MovieLoader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.example.standard.popularmoviestagetwoseggio.dataFromDatabase.MovieContract.MovieEntry;

import static android.os.Build.VERSION_CODES.M;

public class MovieDetailActivity extends AppCompatActivity implements DetailAdapter.DetailAdapterOnclickHandler
{
    private static final int  LOADER_ID_MOVIE = 0;
    private static final int  LOADER_ID_CURSOR = 1;

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
    private DetailCursorAdapter mCursorAdapter;
    private String mUrl;
    private String apiKey;
    private List<Movie> movieItems;
    private String id;

    private Uri mCurrentMovieUri;

    private Movie movie;

    private LoaderManager.LoaderCallbacks<List<Movie>> detailLoader = new LoaderManager.LoaderCallbacks<List<Movie>>() {
        @Override
        public Loader<List<Movie>> onCreateLoader(int i, Bundle bundle) {
            recyclerView.setVisibility(View.VISIBLE);
            return new MovieLoader(getApplicationContext(), mUrl);
        }

        @Override
        public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {
            Log.d("Test", "Loader Id = " + loader.getId());

            releaseDate = movie.getmDate();

            String date = releaseDate.substring(0,4);

            String posterImage = movie.getmPoster();
            Picasso.with(getApplicationContext()).load(posterImage).into(poster);
            titleTextView.setText(movie.getmTitle());
            storyTextView.setText(movie.getmStory());
            dateTextView.setText(date);
            ratingTextView.setText(movie.getmRating());

            float rate = Float.parseFloat(movie.getmRating())/2;
            ratingBar.setRating(rate);

            progressBar.setVisibility(View.GONE);

            if (movies != null && !movies.isEmpty()) {
                mDetailAdapter.add(movies);
                mDetailAdapter.notifyDataSetChanged();
                Log.d("Test", "onLoadFinished DetailActivity");

            } else {
                recyclerView.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), getString(R.string.toast_message), Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onLoaderReset(Loader<List<Movie>> loader) {
            mDetailAdapter.clear();
            Log.d("Test", "onLoaderReset DetailActivity");
        }
    };

    private LoaderManager.LoaderCallbacks<Cursor> cursorLoader = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity_two);

        ButterKnife.bind(this);

        setTitle(getString(R.string.title_details));

        checkConnection();

        /*
        * Receive Data from MovieActivity and set them into Views of detail layout
        */
        movie = getIntent().getParcelableExtra("data");

        id = movie.getmId();
        apiKey = getString(R.string.api_key);
        mUrl = getString(R.string.detail_url) + id + getString(R.string.detail_trailer_url) + apiKey;

        Log.d("Test", "Url = " + mUrl);

        // http://api.themoviedb.org/3/movie/211672/videos?api_key=a495cc93d785e9175db5853bcdb8604a

        favouriteBtn.setText("Favourite");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        movieItems = new ArrayList<>();

        mDetailAdapter = new DetailAdapter(this, this, movieItems);

        recyclerView.setAdapter(mDetailAdapter);

        LoaderManager loader = getLoaderManager();
        loader.initLoader(LOADER_ID_MOVIE, null, detailLoader);

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

    //DONE: Call Loader by clicking buttons
    @OnClick(R.id.review_btn)
    public void review (){
        Toast.makeText(this, "Review clicked!", Toast.LENGTH_LONG).show();
        checkConnection();
        //recyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        mUrl = getString(R.string.detail_url) + id + getString(R.string.detail_review_url) + apiKey;

        Log.d("Test", "Url Button Review = " + mUrl);
        mDetailAdapter.clear();

        LoaderManager loaderReviews = getLoaderManager();
        loaderReviews.restartLoader(LOADER_ID_MOVIE, null, detailLoader);
    }

    @OnClick(R.id.trailer_btn)
    public void trailer (){
        Toast.makeText(this, "Trailer clicked!", Toast.LENGTH_LONG).show();
        checkConnection();
        //recyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        mUrl = getString(R.string.detail_url) + id + getString(R.string.detail_trailer_url) + apiKey;

        mDetailAdapter.clear();
        Log.d("Test", "Url Button Trailer = " + mUrl);

        LoaderManager loaderTrailer = getLoaderManager();
        loaderTrailer.restartLoader(LOADER_ID_MOVIE, null, detailLoader);

    }

    @OnClick(R.id.favourite_btn)
    public void favouriteMovie(){

        if (favouriteBtn.getText() == "Favourite"){
            Toast.makeText(this, "Favourite Button clicked!", Toast.LENGTH_SHORT).show();
            //Todo: save the movie data in db
            favouriteBtn.setText("Unfavourite");
            insertData();

        } else {
            Toast.makeText(this, "UnFavourite Button clicked!", Toast.LENGTH_SHORT).show();
            //Todo: delete the movie data from db
            favouriteBtn.setText("Favourite");
        }
    }

    private void deleteData(){

       // int rowsDeleted = getContentResolver().delete(mCurrentMovieUri, null, null);
        // Todo: rausfinden wie man die mCurrentMovieUri kriegt

        mCurrentMovieUri = Uri.withAppendedPath(MovieEntry.CONTENT_URI, "/" + MovieContract.PATH_MOVIE + movie.getM_Id());
        Log.d("Test", "mCurrentMovieUri deleteData = " + mCurrentMovieUri);

//        if (rowsDeleted == 0) {
//            // If the new content URI is null, then there was an error with insertion.
//            Toast.makeText(this, getString(R.string.delete_failed_edit_activity), Toast.LENGTH_SHORT).show();
//        } else {
//            // Otherwise, the insertion was successful and we can display a toast.
//            Toast.makeText(this, getString(R.string.delete_succed_edit_activity), Toast.LENGTH_SHORT).show();
//        }
    }

    private void insertData(){

        ContentValues values = new ContentValues();

        String poster = movie.getmPoster();
        String title = movie.getmTitle();
        String story = movie.getmStory();
        String date = movie.getmDate();
        String rating = movie.getmRating();
        String id = movie.getmId();
        String author = movie.getmAuthor();
        String review = movie.getmReview();
        String key = movie.getmKey();
        String trailer = movie.getmTrailer();

        values.put(MovieEntry.COLUMN_MOVIE_POSTER, poster);
        values.put(MovieEntry.COLUMN_MOVIE_TITLE, title);
        values.put(MovieEntry.COLUMN_MOVIE_STORY, story);
        values.put(MovieEntry.COLUMN_MOVIE_DATE, date);
        values.put(MovieEntry.COLUMN_MOVIE_RATING, rating);
        values.put(MovieEntry.COLUMN_MOVIE_ID, id);
        values.put(MovieEntry.COLUMN_MOVIE_AUTHOR, author);
        values.put(MovieEntry.COLUMN_MOVIE_REVIEW, review);
        values.put(MovieEntry.COLUMN_MOVIE_KEY, key);
        values.put(MovieEntry.COLUMN_MOVIE_TRAILER, trailer);

        Uri movieUri = getContentResolver().insert(MovieEntry.CONTENT_URI, values);

        Log.d("Test", "Uri = " + movieUri);
        Log.d("Test", "Values = " + values);

        if (movieUri == null){
            Toast.makeText(this, "Movie not saved in Database", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Movie saved in Database", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(Movie data)
    {
        //Implicit Intent for calling youtube in a browser
        Intent intent=new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://www.youtube.com/watch?v=" + data.getmKey()));
        startActivity(intent);
    }


}
