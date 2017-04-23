package com.example.android.popularmovies;

import android.content.ContentValues;

import com.example.android.popularmovies.data.Movies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ehbraheem on 05/04/2017.
 */

public class MovieParser {

    private static final String MOVIES_KEY = "results";

    private JSONArray movies;

    public MovieParser(JSONObject response) {
        try {
            this.movies = response.getJSONArray(MOVIES_KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Object[] parse () throws JSONException {
        Object[] moviesList = new ContentValues[movies.length()];

//        if (type.equals(Movies.class.getName())) {
//
//            for (int i = 0; i < movies.length(); i++) {
//                moviesList[i] = new Movies(movies.getJSONObject(i));
//            }
//        } else {
            for (int i = 0; i < movies.length(); i++) {
                moviesList[i] = new Movies(movies.getJSONObject(i)).toContentValues();
            }
//        }
        return moviesList;
    }

    public static Movies[] parse(JSONObject object) throws JSONException {
        return (Movies[]) new MovieParser(object).parse();
    }

    public static ContentValues[] makeAsContentValues(JSONObject object) throws JSONException {
        return (ContentValues[]) new MovieParser(object).parse();
    }
}
