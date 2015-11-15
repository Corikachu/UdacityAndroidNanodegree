package com.example.corikachu.popularmoviesapp.ui.details;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.example.corikachu.popularmoviesapp.ApplicationController;
import com.example.corikachu.popularmoviesapp.MovieData;
import com.example.corikachu.popularmoviesapp.R;
import com.example.corikachu.popularmoviesapp.utils.Favorite;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.example.corikachu.popularmoviesapp.utils.MovieAPIConstants.API_MOVIE_URL;
import static com.example.corikachu.popularmoviesapp.utils.MovieAPIConstants.HIGHEST_RATED;
import static com.example.corikachu.popularmoviesapp.utils.MovieAPIConstants.POPULARITY;
import static com.example.corikachu.popularmoviesapp.utils.MovieAPIConstants.THEMOVIEDB_API_KEY;

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
    @InjectView(R.id.detailReviewAuthorTextView)
    TextView reviewAuthor;
    @InjectView(R.id.detailReviewContentTextView)
    TextView reviewContent;
    @InjectView(R.id.detailVideoButton)
    Button videoButton;
    @InjectView(R.id.detailFavorite)
    Button favorite;

    final static String TAG = DetailFragment.class.getSimpleName();
    private MovieData data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        ButterKnife.inject(this, rootView);

        //Get Data from intent
        Intent intent = getActivity().getIntent();
        data = intent.getParcelableExtra(getString(R.string.movie_list_key));

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            data = bundle.getParcelable(getString(R.string.movie_list_key));
        }

        if(data == null){
            return inflater.inflate(R.layout.fragment_movie_empty, container, false);
        }

        //Request Video API
        requestVideoAPIQuery(String.valueOf(data.getId()));

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

        favorite.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Favorite favorite = new Favorite(getActivity().getApplicationContext());
                boolean save = favorite.save(String.valueOf(data.getId()));
                if(save) {
                    Toast.makeText(getActivity().getApplicationContext(), "Add Favorite", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Remove Favorite", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

    // Request movie data to api server with api key.
    private void requestVideoAPIQuery(String id) {
        String queryUrlVideo = API_MOVIE_URL + id + "/videos?api_key=" + THEMOVIEDB_API_KEY;
        JsonObjectRequest jsonObjectRequestVideo = new JsonObjectRequest(Request.Method.GET, queryUrlVideo, null,
                new ResponseListenerVideo(), error ->
                VolleyLog.d(TAG, "ERROR : " + error.getMessage())
        );

        String queryUrlReview = API_MOVIE_URL + id + "/reviews?api_key=" + THEMOVIEDB_API_KEY;
        JsonObjectRequest jsonObjectRequestReview = new JsonObjectRequest(Request.Method.GET, queryUrlReview, null,
                new ResponseListenerReview(), error ->
                VolleyLog.d(TAG, "ERROR : " + error.getMessage())
        );

        ApplicationController.getInstance().addToRequestQueue(jsonObjectRequestVideo);
        ApplicationController.getInstance().addToRequestQueue(jsonObjectRequestReview);
    }

    class ResponseListenerVideo implements Response.Listener<JSONObject> {
        @Override
        public void onResponse(JSONObject response) {
            try {
                JSONArray jsonArray = response.getJSONArray("results");
                JSONObject jsonObject = jsonArray.getJSONObject(0);

                String key = jsonObject.getString("key");

                videoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (key != null) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + key)));
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "No video", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class ResponseListenerReview implements Response.Listener<JSONObject> {
        @Override
        public void onResponse(JSONObject response) {
            try {
                JSONArray jsonArray = response.getJSONArray("results");
                JSONObject jsonObject = jsonArray.getJSONObject(0);

                String author = jsonObject.getString("author");
                String content = jsonObject.getString("content");

                reviewAuthor.setText(author);
                reviewContent.setText(content);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
