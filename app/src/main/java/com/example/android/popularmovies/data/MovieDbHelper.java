package com.example.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ehbraheem on 19/04/2017.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final String DATBASE_NAME = "movies.db";

    private static final int DATABASE_VERSION = 14;

    public MovieDbHelper(Context context) {
        super(context, DATBASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_MOVIE_TABLE =

                "CREATE TABLE IF NOT EXISTS " + MovieContract.MovieEntry.TABLE_NAME + " ("      +

                        MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "   +

                        MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " STRING NOT NULL, "     +

                        MovieContract.MovieEntry.COLUMN_FAVORITE + " INTEGER DEFAULT 0, "       +

//                        MovieContract.MovieEntry.COLUMN_CATEGORY + " STRING NOT NULL, "         +

                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, "        +

                        MovieContract.MovieEntry.COLUMN_PLOT + " TEXT NOT NULL, "               +

                        MovieContract.MovieEntry.COLUMN_POSTER_URL + " STRING NOT NULL, "       +

                        MovieContract.MovieEntry.COLUMN_RATING + " REAL NOT NULL, "             +

                        MovieContract.MovieEntry.COLUMN_TITLE + " STRING NOT NULL, "            +

                        " UNIQUE ( " + MovieContract.MovieEntry.COLUMN_MOVIE_ID + " ) ON CONFLICT IGNORE);";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);

        onCreate(db);
    }
}
