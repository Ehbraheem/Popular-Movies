package com.example.android.popularmovies;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.data.Movies;
import com.squareup.picasso.Picasso;

public class MovieDetail extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private TextView mMovieTitle;
    private ImageView mMoviePoster;
    private RatingBar mMovieRating;
    private TextView mMoviePlot;
    private TextView mMovieReleaseDate;

    public static final String[] MOVIE_DETAIL_PROJECTION = {
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_PLOT,
            MovieContract.MovieEntry.COLUMN_FAVORITE,
            MovieContract.MovieEntry.COLUMN_POSTER_URL,
            MovieContract.MovieEntry.COLUMN_RATING,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_TITLE
    };

    public static final int INDEX_MOVIE_ID = 0;
    public static final int INDEX_MOVIE_PLOT = 1;
    public static final int INDEX_MOVIE_FAVORITE = 2;
    public static final int INDEX_MOVIE_POSTER_URL = 3;
    public static final int INDEX_MOVIE_RATING = 4;
    public static final int INDEX_MOVIE_RELEASE_DATE = 5;
    public static final int INDEX_MOVIE_TITLE = 6;

    private Uri mUri;

    private static final int MOVIE_DETAIL_LOADER = 445;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

//        this.getSupportActionBar().setDisplayShowHomeEnabled(true);

        mMoviePoster      = (ImageView) findViewById(R.id.movie_detail_thumbnail);
        mMovieTitle       = (TextView) findViewById(R.id.movie_detail_title);
        mMovieRating      = (RatingBar) findViewById(R.id.movie_detail_rating);
        mMoviePlot        = (TextView) findViewById(R.id.movie_detail_plot);
        mMovieReleaseDate = (TextView) findViewById(R.id.movie_detail_release_date);

        mUri = getIntent().getData();

        if (mUri == null) throw new NullPointerException("URI for MovieDetail cannot be null");

        getSupportLoaderManager().initLoader(MOVIE_DETAIL_LOADER, null, this);



//        if (intent.hasExtra(Intent.EXTRA_TEXT)) {
//            Bundle data = intent.getExtras();
//            Movies movie = data.getParcelable(Intent.EXTRA_TEXT);
//
//            if (movie != null) {
//
//                mMovieTitle.setText(movie.title);
//
//                mMoviePlot.setText(movie.plot);
//                mMovieReleaseDate.setText(movie.releaseDate);
//
//                float rating = Float.parseFloat(movie.rating) / 2;
//                mMovieRating.setNumStars(5);
//                mMovieRating.setStepSize(0.5f);
//                mMovieRating.setRating(rating);
//
//                String imageSize = "w780/";
//
//                Picasso.with(this)
//                        .load(String.valueOf(APIDetails.makePosterUrl(movie.posterUrl,imageSize)))
//                        .into(mMoviePoster);
//            }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {

        switch (loaderId) {

            case MOVIE_DETAIL_LOADER:

                return new CursorLoader(this,
                        mUri,
                        MOVIE_DETAIL_PROJECTION,
                        null,
                        null,
                        null);
            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        boolean cursorHasValidData = false;
        if (data != null && data.moveToFirst()) {
            cursorHasValidData = true;
        }

        if (!cursorHasValidData) return;


        String title = data.getString(INDEX_MOVIE_TITLE);
        String plot = data.getString(INDEX_MOVIE_PLOT);
        String releaseDate = data.getString(INDEX_MOVIE_RELEASE_DATE);
        Float rating = data.getFloat(INDEX_MOVIE_RATING);
        String posterUrl = data.getString(INDEX_MOVIE_POSTER_URL);
        int favorite = data.getInt(INDEX_MOVIE_FAVORITE);

        mMovieTitle.setText(title);

        mMoviePlot.setText(plot);
        mMovieReleaseDate.setText(releaseDate);

        rating /= 2;
        mMovieRating.setNumStars(5);
        mMovieRating.setStepSize(0.5f);
        mMovieRating.setRating(rating);

        String imageSize = "w500/";

        Picasso.with(this)
                .load(String.valueOf(APIDetails.makePosterUrl(posterUrl,imageSize)))
                .into(mMoviePoster);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
