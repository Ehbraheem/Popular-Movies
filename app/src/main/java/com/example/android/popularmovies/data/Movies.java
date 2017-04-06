package com.example.android.popularmovies.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ehbraheem on 05/04/2017.
 */

public class Movies {

    public String title;
    public String posterUrl;
    public String plot;
    public String rating;
    public String releaseDate;

    public Movies(JSONObject object) {

        try {
            this.title = object.getString("original_title");
            this.plot = object.getString("overview");
            this.posterUrl = object.getString("poster_path");
            this.releaseDate = object.getString("release_date");
            this.rating = object.getString("vote_average");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
