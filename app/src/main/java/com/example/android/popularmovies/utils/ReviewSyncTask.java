package com.example.android.popularmovies.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.example.android.popularmovies.APIDetails;
import com.example.android.popularmovies.ReviewParser;
import com.example.android.popularmovies.data.MovieContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

/**
 * Created by Ehbraheem on 4/22/2017.
 */

public class ReviewSyncTask {

    synchronized public static void syncReviews(@NonNull Context context, String movieId) {

        try {

            URL reviewUrl = APIDetails.reviewUrl(movieId);

            JSONObject reviewJson = MoviesNetworkUtils.getMoviesJsonFromUrl(reviewUrl);

            ContentValues[] reviewValues = ReviewParser.parse(reviewJson);

            if (reviewValues != null) {

                ContentResolver reviewContentResolver = context.getContentResolver();

                Uri reviewUri = MovieContract.ReviewEntry.CONTENT_URI(movieId);

                reviewContentResolver.bulkInsert(reviewUri, reviewValues);
            }
        } finally {

        }
    }
}
