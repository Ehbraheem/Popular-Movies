package com.example.android.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Ehbraheem on 19/04/2017.
 */

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIES = "movies";

    public static final String PATH_REVIEWS = "reviews";

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIES)
                .build();


        public static final String TABLE_NAME = "movie";

        public static final String COLUMN_TITLE = "title";

        public static final String COLUMN_POSTER_URL = "poster_url";

        public static final String COLUMN_PLOT = "plot";

        public static final String COLUMN_CATEGORY = "category";

        public static final String COLUMN_RELEASE_DATE = "release_date";

        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static final String COLUMN_FAVORITE = "favorite";

        public static final String COLUMN_RATING = "rating";

        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;

        public static final String CONTENT_FAVORITE_DIR = CONTENT_DIR_TYPE + "/" + COLUMN_FAVORITE + "s";

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;

        public static Uri buildUriWithId(int id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(String.valueOf(id))
                    .build();
        }

        public static String createSqlSelectorForFavoriteMovies() {
            return MovieEntry.COLUMN_FAVORITE + " = ?";
        }
    }

    public static final class ReviewEntry implements BaseColumns {

        public static Uri CONTENT_URI(String movieId) {
            return MovieEntry.CONTENT_URI.buildUpon()
                    .appendPath(movieId)
                    .appendPath(PATH_REVIEWS)
                    .build();
        }
//        public static final Uri CONTENT_URI =


        public static final String TABLE_NAME = "reviews";

        public static final String COLUMN_CONTENT = "content";

        public static final String COLUMN_AUTHOR = "author";

        public static Uri buildReviewUri(long id, String movieId) {
            return ContentUris.withAppendedId(CONTENT_URI(movieId), id);
        }
    }
}
