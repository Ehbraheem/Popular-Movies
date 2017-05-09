package com.example.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by prof.BOLA on 4/21/2017.
 */

public class ReviewDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "reviews.db";

    private static final int DATABASE_VERSION = 5;

    public ReviewDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_REVIEW_TABLE =

                "CREATE TABLE IF NOT EXISTS " + MovieContract.ReviewEntry.TABLE_NAME + " ( "                              +

                        MovieContract.ReviewEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "                          +

                        MovieContract.ReviewEntry.COLUMN_CONTENT + " TEXT NOT NULL, "                                   +

                        MovieContract.ReviewEntry.COLUMN_AUTHOR + " STRING NOT NULL, "                                  +

                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER, "                                         +
                        " FOREIGN KEY ( " + MovieContract.MovieEntry.COLUMN_MOVIE_ID + " ) REFERENCES "                 +
                        MovieContract.MovieEntry.TABLE_NAME + " ( " + MovieContract.MovieEntry.COLUMN_MOVIE_ID + " )"   +

                        " UNIQUE ( " + MovieContract.ReviewEntry.COLUMN_AUTHOR + " ) ON CONFLICT REPLACE );";

        db.execSQL(SQL_CREATE_REVIEW_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.ReviewEntry.TABLE_NAME);

        onCreate(db);
    }
}
