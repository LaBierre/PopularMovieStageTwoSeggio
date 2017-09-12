package com.example.standard.popularmoviestagetwoseggio.dataFromInternet;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.standard.popularmoviestagetwoseggio.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by vince on 02.09.2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    /*
     * An on-click handler that I've defined to make it easy for an Activity to interface with
     * my RecyclerView
     */
    private final MovieAdapterOnClickHandler mClickHandler;
    private Context context;
    private List<Movie> movieItems;

    /*
    * Constructor for the MovieAdapter
    */
    public MovieAdapter(Context context, MovieAdapterOnClickHandler mClickHandler, List<Movie> movieItems) {
        this.context = context;
        this.movieItems = movieItems;
        this.mClickHandler = mClickHandler;
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
                .inflate(R.layout.movie_list_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie movieItem = movieItems.get(position);

        String poster = context.getString(R.string.image_url_w185) + movieItem.getmPoster();

        Picasso
                .with(context)
                .load(poster)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return movieItems.size();
    }

    /**
     * The interface that receives onClick messages.
     */
    public interface MovieAdapterOnClickHandler {
        void onClick(Movie data);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.movie_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();

            //String _id = "";
            String title = movieItems.get(adapterPosition).getmTitle();
            String posterImage = context.getString(R.string.image_url_w342) + movieItems.get(adapterPosition).getmPoster();
            String overview = movieItems.get(adapterPosition).getmStory();
            String rating = String.valueOf(movieItems.get(adapterPosition).getmRating());
            String date = movieItems.get(adapterPosition).getmDate();
            String id = movieItems.get(adapterPosition).getmId();
            String author = movieItems.get(adapterPosition).getmAuthor();
            String content = movieItems.get(adapterPosition).getmReview();
            String key = movieItems.get(adapterPosition).getmKey();
            String trailerText = movieItems.get(adapterPosition).getmTrailer();

            Movie data = new Movie(posterImage, title, overview, date, rating, id, author, content, key, trailerText);

            mClickHandler.onClick(data);
        }
    }
}
