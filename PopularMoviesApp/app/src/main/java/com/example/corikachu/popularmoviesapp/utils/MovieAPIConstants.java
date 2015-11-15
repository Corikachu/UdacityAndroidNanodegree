package com.example.corikachu.popularmoviesapp.utils;

/**
 * Movie Api Constants
 * Usage
 * http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=[PRIVATE KEY]
 * http://image.tmdb.org/t/p/w500/8uO0gUM8aNqYLs1OsTBQiXu0fEv.jpg
 */
public interface MovieAPIConstants {

//    String THEMOVIEDB_API_KEY = "[PRIVATE KEY]";
    String THEMOVIEDB_API_KEY = "e62154b63b41fec8aca2831fe1141d00";
    String API_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
    String API_MOVIE_URL = "http://api.themoviedb.org/3/movie/";
    String API_IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w500/";

    String QUERY_API_KEY = "api_key=";
    String QUERY_SORT_BY = "sort_by=";

    String POPULARITY = "popularity.desc";
    String HIGHEST_RATED = "vote_average.desc";
}
