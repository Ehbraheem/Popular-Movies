package com.example.android.popularmovies.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.example.android.popularmovies.APIDetails;
import com.example.android.popularmovies.TrailerParser;
import com.example.android.popularmovies.data.MovieContract;

import org.json.JSONObject;

import java.net.URL;

/**
 * Created by Ehbraheem on 4/23/2017.
 */

public class TrailerSyncTask {

    synchronized public static void syncTrailers (@NonNull Context context, String movieId) {

        try {

            URL trailerUrl = APIDetails.makeTraillerUrl(movieId);

            JSONObject trailerJson = MoviesNetworkUtils.getMoviesJsonFromUrl(trailerUrl);

            ContentValues[] trailerValues = TrailerParser.parse(trailerJson);

            if (trailerValues != null) {

                ContentResolver trailerContentResolver = context.getContentResolver();

                Uri trailerUri = MovieContract.TrailerEntry.CONTENT_URI(movieId);

                trailerContentResolver.bulkInsert(trailerUri, trailerValues);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
