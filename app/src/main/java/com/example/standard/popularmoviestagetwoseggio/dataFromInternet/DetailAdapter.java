package com.example.standard.popularmoviestagetwoseggio.dataFromInternet;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.standard.popularmoviestagetwoseggio.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vince on 03.09.2017.
 */

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.ViewHolder>
{
    /*
     * An on-click handler that I've defined to make it easy for an Activity to interface with
     * my RecyclerView
     */
    private final DetailAdapterOnclickHandler mClickHandler;
    private Context context;
    private List<Movie> movieItems;

    public DetailAdapter(Context context, DetailAdapterOnclickHandler mClickHandler, List<Movie> movieItems) {
        this.mClickHandler = mClickHandler;
        this.context = context;
        this.movieItems = movieItems;
    }

    public void clear() {
        movieItems.clear();
        notifyDataSetChanged();
    }

    public void add(List<Movie> movieItems) {
        this.movieItems.addAll(movieItems);
        this.notifyItemRangeInserted(0, movieItems.size() - 1);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detail_list_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        Movie movieItem = movieItems.get(position);

        //DONE: Fit in author, review, image, trailer

        // Check if Review Button is clicket or the Trailer Button
        if (movieItem.getmAuthor().isEmpty() && movieItem.getmReview().isEmpty()){
            //Toast.makeText(context, "Trailer Btn is clicked", Toast.LENGTH_SHORT).show();
            holder.trailerLayout.setVisibility(View.VISIBLE);
            holder.reviewLayout.setVisibility(View.GONE);
        } else {
            //Toast.makeText(context, "Review Btn is clicked", Toast.LENGTH_SHORT).show();
            holder.trailerLayout.setVisibility(View.GONE);
            holder.reviewLayout.setVisibility(View.VISIBLE);
        }

        holder.author.setText(movieItem.getmAuthor());
        Log.d("Test", "Author = " + movieItem.getmAuthor());
        holder.review.setText(movieItem.getmReview());
        Log.d("Test", "Review = " + movieItem.getmReview());
        holder.trailer.setText(movieItem.getmTrailer());
        Log.d("Test", "Trailer = " + movieItem.getmTrailer());
    }

    @Override
    public int getItemCount() {
        return movieItems.size();
    }

    public interface DetailAdapterOnclickHandler{
        void onClick(Movie data);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
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

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();

            String title = movieItems.get(adapterPosition).getmTitle();
            String posterImage = context.getString(R.string.image_url_w342) + movieItems.get(adapterPosition).getmPoster();
            String story = movieItems.get(adapterPosition).getmStory();
            String rating = String.valueOf(movieItems.get(adapterPosition).getmRating());
            String date = movieItems.get(adapterPosition).getmDate();
            String id = movieItems.get(adapterPosition).getmId();
            String author = movieItems.get(adapterPosition).getmAuthor();
            String review = movieItems.get(adapterPosition).getmReview();
            String key = movieItems.get(adapterPosition).getmKey();
            String trailerText = movieItems.get(adapterPosition).getmTrailer();

            Movie data = new Movie(posterImage, title, story, date, rating, id, author, review, key, trailerText);

            mClickHandler.onClick(data);
        }
    }
}
