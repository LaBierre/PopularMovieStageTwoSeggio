package com.example.standard.popularmoviestagetwoseggio.dataFromDatabase;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.standard.popularmoviestagetwoseggio.R;

/**
 * Created by vince on 11.09.2017.
 */

public class DetailCursorAdapter extends RecyclerViewCursorAdapter<DetailCursorAdapter.DetailViewHolder>
{
    private final DetailCursorAdapterOnClickHandler mClickHandler;
    private Context context;

    public DetailCursorAdapter(Context context, Cursor cursor, DetailCursorAdapterOnClickHandler mClickHandler) {
        super(cursor);
        this.mClickHandler = mClickHandler;
        this.context = context;
    }


    @Override
    public DetailCursorAdapter.DetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected void onBindViewHolder(DetailCursorAdapter.DetailViewHolder holder, Cursor cursor) {

    }

    /**
     * The interface that receives onClick messages.
     */
    public interface DetailCursorAdapterOnClickHandler {
        void onClick (Cursor cursor);
    }

    public static class DetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView imageView;

        public DetailViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.movie_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
