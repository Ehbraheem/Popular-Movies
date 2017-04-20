package com.example.android.popularmovies.utils;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Ehbraheem on 4/20/2017.
 */

public class MoviesNetworkUtils {

    public static JSONObject getMoviesJsonFromUrl(@NonNull URL url) {

        JSONObject response = null;

        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            InputStream stream = new BufferedInputStream(connection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));

            StringBuilder builder = new StringBuilder();
            String inputString;

            while ((inputString = bufferedReader.readLine()) != null) {
                builder.append(inputString);
            }

            response = new JSONObject(String.valueOf(builder));
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
        }
        return response;
    }

}
