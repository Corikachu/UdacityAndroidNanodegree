package com.example.corikachu.popularmoviesapp.ui.movies;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.corikachu.popularmoviesapp.ApplicationController;
import com.example.corikachu.popularmoviesapp.MovieData;
import com.example.corikachu.popularmoviesapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.example.corikachu.popularmoviesapp.utils.MovieAPIConstants.*;

/**
 * MovieListFragments
 * containing a simple view.
 */
public class MovieListFragments extends Fragment {

    private static final String TAG = MovieListFragments.class.getSimpleName();
    private ArrayList<MovieData> movieDataArrayList = new ArrayList<>();
    private CardViewAdapter adapter;

    private ProgressDialog mProgressDialog;

    @InjectView(R.id.movieCardListRecyclerView)
    RecyclerView recyclerView;

    public MovieListFragments() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);
        ButterKnife.inject(this, view);
        setHasOptionsMenu(true);

        // Use recyclerView. This view need to set LinearLayoutManager.
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity().getApplication());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        // Deal screen rotate
        if(savedInstanceState == null || !savedInstanceState.containsKey(getString(R.string.movie_list_key))){
            // Request movie data with volley.
            requestAPIQuery(POPULARITY);
        } else {
            // load movie data from saveInstanceState
            movieDataArrayList = savedInstanceState.getParcelableArrayList(getString(R.string.movie_list_key));
        }

        // Set CardView List Adapter.
        adapter = new CardViewAdapter(movieDataArrayList, getActivity());
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(getString(R.string.movie_list_key), movieDataArrayList);
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.sort_most_popularity) {
            requestAPIQuery(POPULARITY);
            return true;
        } else if (id == R.id.sort_highest_rated) {
            requestAPIQuery(HIGHEST_RATED);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Request movie data to api server with sort order.
    private void requestAPIQuery(String sortOrder) {
        String queryUrl = API_BASE_URL + QUERY_SORT_BY + sortOrder + "&" + QUERY_API_KEY + THEMOVIEDB_API_KEY;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, queryUrl, null,
                new ResponseListener(), error ->
                VolleyLog.d(TAG, "ERROR : " + error.getMessage())
        );
        ApplicationController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    class ResponseListener implements Response.Listener<JSONObject> {
        @Override
        public void onResponse(JSONObject response) {

            showProgressDialog();

            try {
                movieDataArrayList.clear();
                JSONArray jsonArray = response.getJSONArray("results");

                for (int i = 0; i < jsonArray.length(); i++) {

                    MovieData item = new MovieData();
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String backdropPath = API_IMAGE_BASE_URL+jsonObject.getString("backdrop_path");
                    item.setBackdropPath(backdropPath);
                    item.setId(jsonObject.getInt("id"));
                    item.setOverview(jsonObject.getString("overview"));
                    item.setReleaseDate(jsonObject.getString("release_date"));
                    item.setTitle(jsonObject.getString("title"));
                    item.setVoteAverage(jsonObject.getDouble("vote_average"));

                    movieDataArrayList.add(item);
                }
            }catch(JSONException e){
                e.printStackTrace();
            }
            adapter.notifyDataSetChanged();
            dismissProgressDialog();
        }
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage("Loading...");
        }
        mProgressDialog.show();
    }

    private void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }
}
