package com.example.android.popularmovies.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.android.popularmovies.APIDetails;
import com.example.android.popularmovies.MainActivity;
import com.example.android.popularmovies.MovieParser;
import com.example.android.popularmovies.data.MovieContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

/**
 * Created by Ehbraheem on 4/20/2017.
 */

public class MovieSyncTask {

    synchronized public static void syncMovies(@NonNull Context context) {

        cacheMovies(context, MainActivity.POPULAR_MOVIES);
        cacheMovies(context, MainActivity.MOST_RATED_MOVIES);

    }

    private static void cacheMovies(@NonNull Context context, String movieType) {
        try{

            URL apiUrl = APIDetails.makeResourceUrl(movieType);

            JSONObject jsonResponse = MoviesNetworkUtils.getMoviesJsonFromUrl(apiUrl);

            ContentValues[] movieValues = MovieParser.makeAsContentValues(jsonResponse);


            if (movieValues != null) {

                for (ContentValues cv: movieValues) {
                    cv.put(MovieContract.MovieEntry.COLUMN_CATEGORY, movieType);
                };

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
