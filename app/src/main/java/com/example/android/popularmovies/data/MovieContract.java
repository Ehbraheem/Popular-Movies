package com.example.android.popularmovies.data;

import android.provider.BaseColumns;

/**
 * Created by Ehbraheem on 19/04/2017.
 */

public class MovieContract {

    public static final class MovieEntry implements BaseColumns {

        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_TITLE = "date";

        public static final String COLUMN_POSTER_URL = "poster_url";

        public static final String COLUMN_PLOT = "plot";

        public static final String COLUMN_RELEASE_DATE = "release_date";

        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static final String COLUMN_FAVORITE = "favorite";

        public static final String COLUMN_RATING = "rating";

    }
}
