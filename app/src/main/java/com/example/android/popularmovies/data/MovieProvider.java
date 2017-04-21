package com.example.android.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Ehbraheem on 19/04/2017.
 */

public class MovieProvider extends ContentProvider {


    public static final int CODE_MOVIES = 100;

    public static final int CODE_SINGLE_MOVIE = 101;

    public static final int CODE_FAVORITE_MOVIES = 200;

    private MovieDbHelper movieDbHelper;
    public static final UriMatcher sUriMatcher = buildUriMatcher();


    private static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.PATH_MOVIES, CODE_MOVIES);

        // without having to implicitly query the movies
        matcher.addURI(authority, MovieContract.PATH_MOVIES + "/favorites", CODE_FAVORITE_MOVIES);

        matcher.addURI(authority, MovieContract.PATH_MOVIES + "/#", CODE_SINGLE_MOVIE);

        return matcher;
    }

    @Override
    public boolean onCreate() {

        Context context = getContext();

        movieDbHelper = new MovieDbHelper(context);

        return true;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {

        final SQLiteDatabase db = movieDbHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {

            case CODE_MOVIES:
                db.beginTransaction();
                int moviesInserted = 0;

                try {
                    for (ContentValues cv: values) {
                        long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, cv);

                        if (_id != -1) moviesInserted++;
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (moviesInserted > 0 ) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return moviesInserted;

            default:
                return super.bulkInsert(uri, values);
        }

    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        Cursor cursor;

        switch (sUriMatcher.match(uri)) {

            case CODE_FAVORITE_MOVIES:

                String favorite = MovieContract.MovieEntry.createSqlSelectorForFavoriteMovies();
                String[] favaoriteSelector = makeSelectionArgs(1);

                cursor = movieDbHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        favorite,
                        favaoriteSelector,
                        null,
                        null,
                        sortOrder
                );

                break;
            case CODE_MOVIES:

                cursor = movieDbHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );

                break;

            case CODE_SINGLE_MOVIE:

                String movieID = uri.getLastPathSegment();

                String movieSelection = MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ";

                String[] selectionArguments = makeSelectionArgs(movieID);

                cursor = movieDbHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        movieSelection,
                        selectionArguments,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        switch (sUriMatcher.match(uri)) {

            case CODE_FAVORITE_MOVIES:

                return MovieContract.MovieEntry.CONTENT_FAVORITE_DIR;

            case CODE_MOVIES:

                return MovieContract.MovieEntry.CONTENT_DIR_TYPE;

            case CODE_SINGLE_MOVIE:

                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;

            default:

                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        int numRowsDeleted;

        if (null == selection) selection = "1";

        switch (sUriMatcher.match(uri)) {

            case CODE_MOVIES:

                numRowsDeleted = movieDbHelper.getWritableDatabase().delete(
                        MovieContract.MovieEntry.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        throw new RuntimeException(
                "I am not implementing this method. Use bulkInsert instead");
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        int numUpdated = 0;

        switch (sUriMatcher.match(uri)) {

            case CODE_SINGLE_MOVIE:

                String movieID = uri.getLastPathSegment();
                String[] selectionArgument = makeSelectionArgs(movieID);

                numUpdated = movieDbHelper.getWritableDatabase().update(
                        MovieContract.MovieEntry.TABLE_NAME,
                        values,
                        MovieContract.MovieEntry._ID + " = ?",
                        selectionArgument
                );
                break;

            case CODE_MOVIES:

                numUpdated = movieDbHelper.getWritableDatabase().update(
                        MovieContract.MovieEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs
                );
                break;

            case CODE_FAVORITE_MOVIES:

                String favorite = MovieContract.MovieEntry.createSqlSelectorForFavoriteMovies();
                String[] favaoriteSelector = makeSelectionArgs(1);

                numUpdated = movieDbHelper.getWritableDatabase().update(
                        MovieContract.MovieEntry.TABLE_NAME,
                        values,
                        favorite,
                        favaoriteSelector
                );
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        if (numUpdated > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numUpdated;
    }


    private String[] makeSelectionArgs(@NonNull Object args) {
        return new String[]{String.valueOf(args)};
    }
}