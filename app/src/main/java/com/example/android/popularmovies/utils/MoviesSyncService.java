package com.example.android.popularmovies.utils;


import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by Ehbraheem on 4/20/2017.
 */

public class MoviesSyncService extends IntentService {


    /**
     * Creates an IntentService.  Invoked by invoking the superclass construtor.
     */
    public MoviesSyncService() {
        super(MoviesSyncService.class.getName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if (MoviesNetworkUtils.networkCheck(this)) {
            MovieSyncTask.syncMovies(this);
        } else return;
    }
}
