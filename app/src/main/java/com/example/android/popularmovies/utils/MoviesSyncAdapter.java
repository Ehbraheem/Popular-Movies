package com.example.android.popularmovies.utils;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

/**
 * Created by Ehbraheem on 19/04/2017.
 */

public class MoviesSyncAdapter extends AbstractThreadedSyncAdapter {

    public static final int SYNC_INTERVAL = 1440; // 60 seconds (1 minutes) * 1440  = 24hrs;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;

    ContentResolver mContentResolver;

    private static final int MOVIE_NOTIFICATION_ID = 4004;

    public MoviesSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);

        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

    }
}
