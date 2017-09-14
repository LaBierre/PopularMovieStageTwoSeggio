package com.example.standard.popularmoviestagetwoseggio.dataFromDatabase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.standard.popularmoviestagetwoseggio.R;
import com.example.standard.popularmoviestagetwoseggio.dataFromInternet.Movie;
import com.squareup.picasso.Picasso;

/**
 * Created by vince on 10.09.2017.
 */

public class MovieCursorAdapter extends RecyclerViewCursorAdapter<MovieCursorAdapter.MovieViewHolder> {
    private final MovieCursorAdapterOnClickHandler mClickHandler;
    private Context context;
    private Cursor cursor;
    private MovieDbHelper mDbHelper;

    public MovieCursorAdapter(Context context, MovieCursorAdapterOnClickHandler mClickHandler, Cursor cursor) {
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

    public boolean isCursorEmpty(Cursor cursor) {
        if (cursor.getCount() == 0)
            return true;
        return false;
    }

    public int cursorCount(Cursor cursor) {
        return cursor.getCount();
    }

    @Override
    protected void onBindViewHolder(MovieViewHolder holder, Cursor cursor) {
        try {
            int posterColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER);

            String poster = cursor.getString(posterColumnIndex);

            Picasso
                    .with(context)
                    .load(poster)
                    .into(holder.imageView);
        } catch (Exception e) {
            e.printStackTrace();
            cursor.close();
        }
    }

    /**
     * The interface that receives onClick messages.
     */
    public interface MovieCursorAdapterOnClickHandler {
        void onClick(Movie data);
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.movie_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();

            mDbHelper = new MovieDbHelper(context);

            // Get readable database
            SQLiteDatabase database = mDbHelper.getReadableDatabase();

            cursor = database.query(MovieContract.MovieEntry.TABLE_NAME, null, null, null,
                    null, null, null);
            cursor.moveToPosition(adapterPosition);

            int _idColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry._ID);
            int posterColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER);
            int titleColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE);
            int storyColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_STORY);
            int dateColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_DATE);
            int ratingColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_RATING);
            int idColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID);

            String _id = cursor.getString(_idColumnIndex);
            String poster = cursor.getString(posterColumnIndex);
            String title = cursor.getString(titleColumnIndex);
            String story = cursor.getString(storyColumnIndex);
            String date = cursor.getString(dateColumnIndex);
            String rating = cursor.getString(ratingColumnIndex);
            String id = cursor.getString(idColumnIndex);

            Movie data = new Movie(_id, poster, title, story, date, rating, id);

            mClickHandler.onClick(data);
        }
    }
}
