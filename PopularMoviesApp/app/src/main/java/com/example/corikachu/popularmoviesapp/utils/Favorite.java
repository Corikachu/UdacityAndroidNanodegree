package com.example.corikachu.popularmoviesapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


import java.util.ArrayList;

/**
 * Save Favorite
 */
public class Favorite {

    private SharedPreferences prefs;

    public Favorite(Context context) {
        this.prefs = context.getSharedPreferences("Favorite", Context.MODE_PRIVATE);
    }

    public boolean save(String id) {
        SharedPreferences.Editor editor;
        editor = prefs.edit();

        ArrayList<String> movieIdList = load();

        for (int i = 0 ; i<movieIdList.size() ; i++){
            if (movieIdList.get(i).equals(id)){
                movieIdList.remove(i);
                return false;
            }
        }

        movieIdList.add(id);
        try {
            editor.putString("MovieList", ObjectSerializer.serialize(movieIdList));
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor.apply();
        return true;
    }

    public ArrayList<String> load() {
        ArrayList<String> movieIdList = new ArrayList<>();
        try {
            movieIdList = (ArrayList<String>) ObjectSerializer.deserialize(prefs.getString("MovieList", ObjectSerializer.serialize(new ArrayList())));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return movieIdList;
    }
}
