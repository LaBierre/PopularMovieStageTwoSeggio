package com.example.standard.popularmoviestagetwoseggio.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.standard.popularmoviestagetwoseggio.R;
import com.example.standard.popularmoviestagetwoseggio.data.Movie;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.author;

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

        //Todo: Fit in author, review, image, trailer

        holder.author.setText(movieItem.getmAuthor());
        holder.review.setText(movieItem.getmContent());
        holder.trailer.setText(movieItem.getmTrailer());
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

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();

            //Todo: Not sure that all this things are needed

            String title = movieItems.get(adapterPosition).getmTitle();
            String posterImage = context.getString(R.string.image_url_w342) + movieItems.get(adapterPosition).getmPoster();
            String overview = movieItems.get(adapterPosition).getmOverview();
            String rating = String.valueOf(movieItems.get(adapterPosition).getmRating());
            String date = movieItems.get(adapterPosition).getmDate();
            String id = movieItems.get(adapterPosition).getmId();
            String author = movieItems.get(adapterPosition).getmAuthor();
            String content = movieItems.get(adapterPosition).getmContent();
            String key = movieItems.get(adapterPosition).getmKey();
            String trailerText = movieItems.get(adapterPosition).getmTrailer();

            Movie data = new Movie(posterImage, title, overview, date, rating, id, author, content, key, trailerText);

            mClickHandler.onClick(data);
        }
    }

    //Todo: Making the RecyclerView in the DetailActivity visible

}
