package com.example.android.popularmovies;

import android.content.ContentValues;

import com.example.android.popularmovies.data.Reviews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ehbraheem on 4/22/2017.
 */

public class ReviewParser {

    private static final String REVIEW_KEY = "results";

    public static ContentValues[] parse(JSONObject object) {

        ContentValues[] reviewValues = null;
        try {
            JSONArray reviews = object.getJSONArray(REVIEW_KEY);

            reviewValues = new ContentValues[reviews.length()];

            for (int i = 0; i < reviews.length(); i++) {
                reviewValues[i] = new Reviews(reviews.getJSONObject(i)).toContentValues();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return reviewValues;
    }
}
