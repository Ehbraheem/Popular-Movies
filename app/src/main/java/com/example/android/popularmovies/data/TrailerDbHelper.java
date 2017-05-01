package com.example.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by prof.BOLA on 4/23/2017.
 */

public class TrailerDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "trailers.db";

    private static final int DATABASE_VERSION = 2;

    public TrailerDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String CREATE_SQL_TRAILER_TABLE =
                "CREATE TABLE IF NOT EXISTS " + MovieContract.TrailerEntry.TABLE_NAME + " ( "           +

                        MovieContract.TrailerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "                 +

                        MovieContract.TrailerEntry.COLUMN_KEY + " STRING NOT NULL, "                    +

                        MovieContract.TrailerEntry.COLUMN_NAME + " STRING NOT NULL, "                   +

                        MovieContract.TrailerEntry.COLUMN_SIZE + " INTEGER NOT NULL, "                  +

                        MovieContract.TrailerEntry.COLUMN_TYPE + " STRING NOT NULL, "                   +

                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, "                +

                        " FOREIGN KEY ( " + MovieContract.MovieEntry.COLUMN_MOVIE_ID + " ) REFERENCES " +

                        MovieContract.MovieEntry.TABLE_NAME + " ( " + MovieContract.MovieEntry.COLUMN_MOVIE_ID +
                        " ) "                                                                           +

                        "UNIQUE ( " + MovieContract.TrailerEntry.COLUMN_KEY  + " ) ON CONFLICT REPLACE );";

        db.execSQL(CREATE_SQL_TRAILER_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.TrailerEntry.TABLE_NAME);

        onCreate(db);
    }
}
