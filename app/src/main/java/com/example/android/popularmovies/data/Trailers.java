package com.example.android.popularmovies.data;

import android.content.ContentValues;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ehbraheem on 4/23/2017.
 */

public class Trailers {

    public String key;
    public String name;
    public String type;
    public int size;

    public Trailers(JSONObject object) {
        try {
            this.key    = object.getString("key");
            this.name   = object.getString("name");
            this.size   = object.getInt("size");
            this.type   = object.getString("type");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ContentValues toContentValues() {

        ContentValues trailerValues = new ContentValues();

        trailerValues.put(MovieContract.TrailerEntry.COLUMN_KEY, key);

        trailerValues.put(MovieContract.TrailerEntry.COLUMN_NAME, name);

        trailerValues.put(MovieContract.TrailerEntry.COLUMN_SIZE, size);

        trailerValues.put(MovieContract.TrailerEntry.COLUMN_TYPE, type);

        return trailerValues;
    }
}
