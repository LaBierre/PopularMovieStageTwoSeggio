package com.example.standard.popularmoviestagetwoseggio.dataFromDatabase;

import android.content.Context;
import android.database.Cursor;
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

/**
 * Created by vince on 10.09.2017.
 */

public class MovieCursorAdapter extends RecyclerViewCursorAdapter<MovieCursorAdapter.MovieViewHolder>
{
    private final MovieCursorAdapterOnClickHandler mClickHandler;
    private Context context;
    private static Cursor cursor;
    private List<Movie> movieItems;

    public MovieCursorAdapter(Context context, Cursor cursor, MovieCursorAdapterOnClickHandler mClickHandler) {
        super(cursor);
        this.mClickHandler = mClickHandler;
        this.context = context;
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

        this.cursor = cursor;

        int posterColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER);


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
        void onClick (Cursor cursor);
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView imageView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.movie_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //Todo: extract data from db and put in Movie data


//            int adapterPosition = getAdapterPosition() + 1;
//            int otherPositionId = view.getId();
//            Cursor cursor;
//
//            int idColumnIndex = cursor.getColumnIndex(String.valueOf(adapterPosition));
//            int titleColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE);
//            String _id = cursor.getString(idColumnIndex);
//            Log.d("Test", "adapterPosition = " + adapterPosition);
//            Log.d("Test", "otherPositionId = " + otherPositionId);
//            Log.d("Test", "_id = " + _id);
        }
    }
}
