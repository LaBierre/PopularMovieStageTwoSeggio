package com.example.standard.popularmoviestagetwoseggio.dataFromDatabase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.standard.popularmoviestagetwoseggio.R;
import com.example.standard.popularmoviestagetwoseggio.dataFromInternet.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.R.attr.id;
import static android.R.attr.onClick;

/**
 * Created by vince on 10.09.2017.
 */

public class MovieCursorAdapter extends RecyclerViewCursorAdapter<MovieCursorAdapter.MovieViewHolder>
{
    private final MovieCursorAdapterOnClickHandler mClickHandler;
    private Context context;
    private Cursor cursor;
    private MovieDbHelper mDbHelper;
    //private List<Cursor> movieItems;

    public MovieCursorAdapter(Context context, Cursor cursor, MovieCursorAdapterOnClickHandler mClickHandler) {
        super(cursor);
        this.mClickHandler = mClickHandler;
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_item, parent, false);

        return new MovieViewHolder(v);
    }

    @Override
    protected void onBindViewHolder(MovieViewHolder holder, Cursor cursor) {
        //Todo: get poster url from db

        int position = cursor.getPosition();
        Log.d("Test", "position onBindViewHolder = " + position);

        int posterColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER);
        Log.d("Test", "posterColumnIndex onBindViewHolder = " + posterColumnIndex);

        String poster = cursor.getString(posterColumnIndex);

        Picasso
                .with(context)
                .load(poster)
                .into(holder.imageView);
    }

    /**
     * The interface that receives onClick messages.
     */
    public interface MovieCursorAdapterOnClickHandler {
        void onClick (Movie data);
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView imageView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.movie_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //Todo: extract data from db and put in Movie data

            int adapterPosition = getAdapterPosition();

            mDbHelper = new MovieDbHelper(context);

            // Get readable database
            SQLiteDatabase database = mDbHelper.getReadableDatabase();

            cursor = database.query(MovieContract.MovieEntry.TABLE_NAME, null, null , null,
                    null, null, null);
            cursor.moveToPosition(adapterPosition);
            int position = cursor.getPosition();
            Log.d("Test", "position onClick = " + position);

            int _idColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry._ID);
            int posterColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER);
            int titleColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE);
            int storyColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_STORY);
            int dateColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_DATE);
            int ratingColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_RATING);
            int idColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
            int authorColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_AUTHOR);
            int reviewColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_REVIEW);
            int keyColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_KEY);
            int trailerColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_TRAILER);
            Log.d("Test", "posterColumnIndex onClick = " + posterColumnIndex);

            String _id = cursor.getString(_idColumnIndex);
            String poster = cursor.getString(posterColumnIndex);
            String title = cursor.getString(titleColumnIndex);
            String story = cursor.getString(storyColumnIndex);
            String date = cursor.getString(dateColumnIndex);
            String rating = cursor.getString(ratingColumnIndex);
            String id = cursor.getString(idColumnIndex);
            String author = cursor.getString(authorColumnIndex);
            String review = cursor.getString(reviewColumnIndex);
            String key = cursor.getString(keyColumnIndex);
            String trailer = cursor.getString(trailerColumnIndex);

            Log.d("Test", "id onClick = " + id);

            Movie data = new Movie(_id, poster, title, story, date, rating, id, author, review, key, trailer);

            mClickHandler.onClick(data);
        }
    }
}
