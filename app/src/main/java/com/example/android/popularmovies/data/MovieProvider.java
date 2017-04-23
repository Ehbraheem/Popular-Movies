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
import android.support.v7.view.menu.MenuAdapter;

/**
 * Created by Ehbraheem on 19/04/2017.
 */

public class MovieProvider extends ContentProvider {


    public static final int CODE_MOVIES = 100;

    public static final int CODE_SINGLE_MOVIE = 101;

    public static final int CODE_SINGLE_MOVIE_REVIEW = 1011;

    public static final int CODE_FAVORITE_MOVIES = 200;

    private MovieDbHelper movieDbHelper;

    private ReviewDbHelper reviewDbHelper;

    public static final UriMatcher sUriMatcher = buildUriMatcher();


    private static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.PATH_MOVIES, CODE_MOVIES);

        // without having to implicitly query the movies
        matcher.addURI(authority, MovieContract.PATH_MOVIES + "/favorites", CODE_FAVORITE_MOVIES);

        matcher.addURI(authority, MovieContract.PATH_MOVIES + "/#", CODE_SINGLE_MOVIE);

        // Reviews of a paticluar movie
        matcher.addURI(authority,
                MovieContract.PATH_MOVIES + "/#/" + MovieContract.PATH_REVIEWS,
                CODE_SINGLE_MOVIE_REVIEW);

        return matcher;
    }

    @Override
    public boolean onCreate() {

        Context context = getContext();

        movieDbHelper = new MovieDbHelper(context);

        reviewDbHelper = new ReviewDbHelper(context);

        return true;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {

        final SQLiteDatabase db = movieDbHelper.getWritableDatabase();
        final SQLiteDatabase reviewDb = reviewDbHelper.getWritableDatabase();
        String movieId;

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

            case CODE_SINGLE_MOVIE_REVIEW:

                reviewDb.beginTransaction();
                int reviewsInserted = 0;
                movieId = uri.getPathSegments().get(1);

                try {

                    for (ContentValues cv: values) {
                        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movieId);
                        long _id = reviewDb.insert(MovieContract.ReviewEntry.TABLE_NAME, null, cv);

                        if (_id != -1) reviewsInserted++;
                    }
                    reviewDb.endTransaction();
                } finally {
                    if (reviewDb.inTransaction()) {
                        reviewDb.endTransaction();
                    }
                }

                if (reviewsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }


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

            case CODE_SINGLE_MOVIE_REVIEW:

                String movieId = uri.getPathSegments().get(1);
                String reviewSelection = MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ";
                String[] reviewMovieId = makeSelectionArgs(movieId);

                cursor = reviewDbHelper.getReadableDatabase().query(
                        MovieContract.ReviewEntry.TABLE_NAME,
                        projection,
                        reviewSelection,
                        reviewMovieId,
                        null,
                        null,
                        sortOrder
                );
                break;

            default:
//                Log.d("URI", )
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
        final SQLiteDatabase db = movieDbHelper.getWritableDatabase();
        final SQLiteDatabase reviewDb = reviewDbHelper.getWritableDatabase();
        String movieId;

        Uri returnUri = null;

        long id = 0;

        switch (sUriMatcher.match(uri)) {

            case CODE_SINGLE_MOVIE:

                id = db.insert(
                        MovieContract.MovieEntry.TABLE_NAME,
                        null,
                        values
                );

                if (id > 0) {
                    returnUri = MovieContract.MovieEntry.buildUriWithId((int) id);
                }

                break;

            case CODE_SINGLE_MOVIE_REVIEW:
                movieId = uri.getPathSegments().get(1);
                values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movieId);

                id = reviewDb.insert(
                        MovieContract.ReviewEntry.TABLE_NAME,
                        null,
                        values
                );

                if (id > 0) {
                    returnUri = MovieContract.ReviewEntry.buildReviewUri(id, movieId);
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
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