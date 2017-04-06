package com.example.android.popularmovies.utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ehbraheem on 06/04/2017.
 */

public interface APICallback {

    void handleData(JSONObject object) throws JSONException;
}
