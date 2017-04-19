package com.example.android.popularmovies.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Ehbraheem on 19/04/2017.
 */

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIES = "movies";

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIES)
                .build();


        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_TITLE = "date";

        public static final String COLUMN_POSTER_URL = "poster_url";

        public static final String COLUMN_PLOT = "plot";

        public static final String COLUMN_RELEASE_DATE = "release_date";

        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static final String COLUMN_FAVORITE = "favorite";

        public static final String COLUMN_RATING = "rating";

        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;

        public static final String CONTENT_FAVORITE_DIR = CONTENT_DIR_TYPE + "/" + COLUMN_FAVORITE + "s";

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;


        public static String createSqlSelectorForFavoriteMovies() {
            return MovieEntry.COLUMN_FAVORITE + " = ?";
        }
    }
}
