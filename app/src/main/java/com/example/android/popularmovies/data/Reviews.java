package com.example.android.popularmovies.data;

import android.content.ContentValues;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ehbraheem on 4/22/2017.
 */

public class Reviews {

    public String content;
    public String author;

    public Reviews (JSONObject object) {
        try {
            this.content = object.getString("content");
            this.author  = object.getString("author");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ContentValues toContentValues() {

        ContentValues reviewValues = new ContentValues();

        reviewValues.put(MovieContract.ReviewEntry.COLUMN_AUTHOR, author);

        reviewValues.put(MovieContract.ReviewEntry.COLUMN_CONTENT, content);

        return reviewValues;
    }
}
