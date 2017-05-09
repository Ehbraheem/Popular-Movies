package com.example.android.popularmovies;

import android.content.ContentValues;

import com.example.android.popularmovies.data.Trailers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ehbraheem on 4/23/2017.
 */

public class TrailerParser {

    private static final String TRAILER_KEY = "results";

    public static ContentValues[] parse(JSONObject object) {

        ContentValues[] trailerValues = null;
        try {
            JSONArray trailers = object.getJSONArray(TRAILER_KEY);

            trailerValues = new ContentValues[trailers.length()];

            for (int i = 0; i < trailers.length(); i++) {
                trailerValues[i] = new Trailers(trailers.getJSONObject(i)).toContentValues();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return trailerValues;
    }
}
