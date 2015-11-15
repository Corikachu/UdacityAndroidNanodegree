package com.example.corikachu.popularmoviesapp.ui.movies;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.corikachu.popularmoviesapp.ApplicationController;
import com.example.corikachu.popularmoviesapp.MovieData;
import com.example.corikachu.popularmoviesapp.R;
import com.example.corikachu.popularmoviesapp.ui.details.DetailFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.corikachu.popularmoviesapp.utils.MovieAPIConstants.API_BASE_URL;
import static com.example.corikachu.popularmoviesapp.utils.MovieAPIConstants.API_IMAGE_BASE_URL;
import static com.example.corikachu.popularmoviesapp.utils.MovieAPIConstants.POPULARITY;
import static com.example.corikachu.popularmoviesapp.utils.MovieAPIConstants.QUERY_API_KEY;
import static com.example.corikachu.popularmoviesapp.utils.MovieAPIConstants.QUERY_SORT_BY;
import static com.example.corikachu.popularmoviesapp.utils.MovieAPIConstants.THEMOVIEDB_API_KEY;

public class MovieListActivity extends Activity {

    private final static String TAG = MovieListActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        if (findViewById(R.id.detailFragment) != null) {
            MovieListFragments.adapter.setTwoPane(true);
        }
    }
}
