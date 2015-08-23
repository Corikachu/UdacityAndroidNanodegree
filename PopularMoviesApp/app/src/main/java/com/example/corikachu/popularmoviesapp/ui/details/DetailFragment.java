package com.example.corikachu.popularmoviesapp.ui.details;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.corikachu.popularmoviesapp.MovieData;
import com.example.corikachu.popularmoviesapp.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Default File Header.
 * Need a comments!
 */
public class DetailFragment extends Fragment {

    @InjectView(R.id.detailBackdropImage)
    ImageView backdropImage;
    @InjectView(R.id.detailTitleTextView)
    TextView title;
    @InjectView(R.id.detailRatingBar)
    RatingBar ratingBar;
    @InjectView(R.id.detailReleaseDate)
    TextView releaseDate;
    @InjectView(R.id.detailOverviewTextView)
    TextView overview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        ButterKnife.inject(this, rootView);

        //Get Data from intent
        Intent intent = getActivity().getIntent();
        MovieData data = intent.getParcelableExtra(getString(R.string.movie_list_key));

        //Set contents.
        Glide.with(this).load(data.getBackdropPath()).into(backdropImage);
        title.setText(data.getTitle());
        ratingBar.setRating((float) data.getVoteAverage() / 2);
        overview.setText(data.getOverview());
        releaseDate.setText(data.getReleaseDate());

        //Set actionbar title.
        ActionBar actionBar = getActivity().getActionBar();
        if(actionBar != null) {
            actionBar.setTitle(data.getTitle());
        }

        return rootView;
    }
}
