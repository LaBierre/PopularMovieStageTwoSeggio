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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.standard.popularmoviestagetwoseggio.R;
import com.example.standard.popularmoviestagetwoseggio.dataFromInternet.Movie;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vince on 11.09.2017.
 */

public class DetailCursorAdapter extends RecyclerViewCursorAdapter<DetailCursorAdapter.DetailViewHolder>
{
    private final DetailCursorAdapterOnClickHandler mClickHandler;
    private Context context;
    private Cursor cursor;
    private MovieDbHelper mDbHelper;

    public DetailCursorAdapter(Context context, DetailCursorAdapterOnClickHandler mClickHandler, Cursor cursor) {
        super(cursor);
        this.mClickHandler = mClickHandler;
        this.context = context;
        this.cursor = cursor;
    }


    @Override
    public DetailCursorAdapter.DetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detail_list_item, parent, false);
        Log.d("Test", "DetailCursorAdapter.DetailViewHolder onCreateViewHolder");

        return new DetailViewHolder(v);
    }

    @Override
    protected void onBindViewHolder(DetailCursorAdapter.DetailViewHolder holder, Cursor cursor) {
        Log.d("Test", "DetailCursorAdapter.DetailViewHolder onBindViewHolder");
    }

    /**
     * The interface that receives onClick messages.
     */
    public interface DetailCursorAdapterOnClickHandler {
        void onClick (Movie data);
    }

    public class DetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.author_item)
        TextView author;
        @BindView(R.id.review_item)
        TextView review;

        @BindView(R.id.trailer_item)
        TextView trailer;

        @BindView(R.id.trailer_layout)
        LinearLayout trailerLayout;
        @BindView(R.id.review_layout)
        LinearLayout reviewLayout;

        public DetailViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            Log.d("Test", "DetailCursorAdapter.DetailViewHolder onClick");

            int adapterPosition = getAdapterPosition();

            mDbHelper = new MovieDbHelper(context);

            // Get readable database
            SQLiteDatabase database = mDbHelper.getReadableDatabase();

            cursor = database.query(MovieContract.MovieEntry.TABLE_NAME, null, null , null,
                    null, null, null);
            cursor.moveToPosition(adapterPosition);
            int position = cursor.getPosition();
            //Log.d("Test", "position onClick = " + position);

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
            //Log.d("Test", "posterColumnIndex onClick = " + posterColumnIndex);

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

            //Log.d("Test", "id onClick = " + id);

            Movie data = new Movie(_id, poster, title, story, date, rating, id, author, review, key, trailer);

            mClickHandler.onClick(data);
        }
    }
}
