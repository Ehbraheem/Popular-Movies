package com.example.android.popularmovies;

import com.example.android.popularmovies.data.Movies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ehbraheem on 05/04/2017.
 */

public class MovieParser {

    private static final String MOVIES_KEY = "results";

    public JSONArray movies;

    public MovieParser(JSONObject response) {
        try {
            this.movies = response.getJSONArray(MOVIES_KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Movies[] parse () throws JSONException {
        Movies[] moviesList = new Movies[movies.length()];

        for (int i = 0; i < movies.length(); i++) {
            moviesList[i] = new Movies(movies.getJSONObject(i));
        }

        return moviesList;
    }

    public static Movies[] parse(JSONObject object) throws JSONException {
        return new MovieParser(object).parse();
    }
}
