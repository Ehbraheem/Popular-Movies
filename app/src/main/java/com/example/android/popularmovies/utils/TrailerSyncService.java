package com.example.android.popularmovies.utils;


import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.example.android.popularmovies.MovieDetail;

/**
 * Created by prof.BOLA on 4/23/2017.
 */

public class TrailerSyncService extends IntentService {


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public TrailerSyncService() {
        super(TrailerSyncService.class.getName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        String movieId = intent.getStringExtra(MovieDetail.MOVIE_ID_KEY);

        TrailerSyncTask.syncTrailers(this, movieId);
    }
}
