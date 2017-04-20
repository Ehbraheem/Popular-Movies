package com.example.android.popularmovies.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.example.android.popularmovies.APIDetails;
import com.example.android.popularmovies.MovieParser;
import com.example.android.popularmovies.data.MovieContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

/**
 * Created by Ehbraheem on 4/20/2017.
 */

public class MovieSyncTask {

    synchronized public static void syncMovies(Context context) {

        try{

            URL apiUrl = APIDetails.makeResourceUrl("popular");

            JSONObject jsonResponse = MoviesNetworkUtils.getMoviesJsonFromUrl(apiUrl);

            ContentValues[] movieValues = MovieParser.makeAsContentValues(jsonResponse);

            if (movieValues != null) {

                ContentResolver movieContentResolver = context.getContentResolver();

                movieContentResolver.bulkInsert(
                        MovieContract.MovieEntry.CONTENT_URI,
                        movieValues);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
