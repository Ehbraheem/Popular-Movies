package com.example.android.popularmovies.utils;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.example.android.popularmovies.MovieDetail;

/**
 * Created by Ehbraheem on 4/22/2017.
 */

public class ReviewSyncService extends IntentService {


    /**
     * Creates an IntentService.  Invoked by invoking the superclass construtor.
     */
    public ReviewSyncService() {
        super(ReviewSyncService.class.getName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        String movieId = intent.getStringExtra(MovieDetail.MOVIE_ID_KEY);

        ReviewSyncTask.syncReviews(this, movieId);
    }
}
