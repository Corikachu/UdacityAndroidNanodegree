package com.example.corikachu.popularmoviesapp.ui.movies;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.corikachu.popularmoviesapp.MovieData;
import com.example.corikachu.popularmoviesapp.R;
import com.example.corikachu.popularmoviesapp.ui.details.DetailActivity;
import com.example.corikachu.popularmoviesapp.ui.details.DetailFragment;

import java.util.ArrayList;
import static com.example.corikachu.popularmoviesapp.utils.MovieAPIConstants.API_IMAGE_BASE_URL;

/**
 * Default File Header.
 * Need a comments!
 */
public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.ViewHolder> {

    private ArrayList<MovieData> data;
    private Activity activity;
    private Context context;
    private boolean twoPane;

    public CardViewAdapter(ArrayList<MovieData> data, Activity activity){
        this.data = data;
        this.activity = activity;
        this.context = activity.getApplicationContext();
    }

    @Override
    public CardViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Inflate Layout
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_movie_list, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CardViewAdapter.ViewHolder holder, int position) {

        // Get specific movie data
        final MovieData movieData = data.get(position);

        // Set Image with Gilde
        Glide.with(context)
                .load(movieData.getBackdropPath())
                .centerCrop()
                .into(holder.movieBackdrop);

        // Set movie title
        holder.movieTitle.setText(movieData.getTitle());

        // Set click listener
        holder.itemView.setOnClickListener( v -> {
            if(twoPane){
                Bundle arguments = new Bundle();
                arguments.putParcelable(context.getString(R.string.movie_list_key), movieData);
                DetailFragment fragment = new DetailFragment();
                fragment.setArguments(arguments);
                activity.getFragmentManager().beginTransaction()
                        .replace(R.id.detailFragment, fragment)
                        .commit();

            } else {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra(context.getString(R.string.movie_list_key), movieData);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    /*
     * ViewHolder class
     * RecyclerAdapter is needed ViewHolder
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        protected TextView movieTitle;
        protected ImageView movieBackdrop;
        protected View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            movieTitle = (TextView) itemView.findViewById(R.id.movieTitleAtList);
            movieBackdrop = (ImageView) itemView.findViewById(R.id.movieBackdropImage);
        }
    }

    public void setTwoPane(boolean twoPane) {
        this.twoPane = twoPane;
    }
}
