package com.example.android.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.utils.ReviewSyncService;
import com.example.android.popularmovies.utils.ReviewsAdapter;
import com.example.android.popularmovies.utils.TrailersAdapter;
import com.squareup.picasso.Picasso;

public class MovieDetail extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>,
        TrailersAdapter.TrailerVideoClickListener {

    private TextView mMovieTitle;
    private ImageView mMoviePoster;
    private RatingBar mMovieRating;
    private TextView mMoviePlot;
    private TextView mMovieReleaseDate;

    private RecyclerView mReviewList;
    private RecyclerView mTrailerList;

    private ReviewsAdapter mReviewAdapter;
    private TrailersAdapter mTrailerAdapter;

    private static final String[] MOVIE_DETAIL_PROJECTION = {
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_PLOT,
            MovieContract.MovieEntry.COLUMN_FAVORITE,
            MovieContract.MovieEntry.COLUMN_POSTER_URL,
            MovieContract.MovieEntry.COLUMN_RATING,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_TITLE
    };

    private static final String[] REVIEW_PROJECTION = {
            MovieContract.ReviewEntry.COLUMN_AUTHOR,
            MovieContract.ReviewEntry.COLUMN_CONTENT
    };

    private static final String[] TRAILER_PROJECTION = {
            MovieContract.TrailerEntry.COLUMN_NAME,
            MovieContract.TrailerEntry.COLUMN_KEY,
            MovieContract.TrailerEntry.COLUMN_TYPE,
            MovieContract.TrailerEntry.COLUMN_SIZE
    };


    public static final int INDEX_MOVIE_ID = 0;
    public static final int INDEX_MOVIE_PLOT = 1;
    public static final int INDEX_MOVIE_FAVORITE = 2;
    public static final int INDEX_MOVIE_POSTER_URL = 3;
    public static final int INDEX_MOVIE_RATING = 4;
    public static final int INDEX_MOVIE_RELEASE_DATE = 5;
    public static final int INDEX_MOVIE_TITLE = 6;

    public static final int INDEX_REVIEW_AUTHOR  = 0;
    public static final int INDEX_REVIEW_CONTENT = 1;

    public static final int INDEX_TRAILER_NAME = 0;
    public static final int INDEX_TRAILER_KEY   = 1;
    public static final int INDEX_TRAILER_TYPE  = 2;
    public static final int INDEX_TRAILER_SIZE  = 3;

    private Uri mUri;

    private static final int MOVIE_DETAIL_LOADER = 445;

    private static final int REVIEW_LOADER = 556;

    private static final int TRAILER_LOADER = 756;

    public static final String MOVIE_ID_KEY = "movieId";

    private String movieId;

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
        mReviewList       = (RecyclerView) findViewById(R.id.review_list);
        mTrailerList      = (RecyclerView) findViewById(R.id.trailer_list);

        mReviewAdapter  = new ReviewsAdapter(this);
        mTrailerAdapter = new TrailersAdapter(this, this);

        mUri = getIntent().getData();

        if (mUri == null) throw new NullPointerException("URI for MovieDetail cannot be null");

        movieId = mUri.getLastPathSegment();
        Intent intent = new Intent(this, ReviewSyncService.class);
//        Intent trailerService = new Intent(this, TrailerSyncService.class);

        intent.putExtra(MOVIE_ID_KEY, movieId);
//        trailerService.putExtra(MOVIE_ID_KEY, movieId);

        startService(intent);
//        startService(trailerService);

        getSupportLoaderManager().initLoader(MOVIE_DETAIL_LOADER, null, this);
        getSupportLoaderManager().initLoader(REVIEW_LOADER, null, this);
        getSupportLoaderManager().initLoader(TRAILER_LOADER, null, this);



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

            case REVIEW_LOADER:
                Uri reviewUri = MovieContract.ReviewEntry.CONTENT_URI(movieId);

                return new CursorLoader(this,
                        reviewUri,
                        REVIEW_PROJECTION,
                        null,
                        null,
                        null
                );

            case TRAILER_LOADER:

                Uri trailerUri = MovieContract.TrailerEntry.CONTENT_URI(movieId);

                return new CursorLoader(this,
                        trailerUri,
                        TRAILER_PROJECTION,
                        null,
                        null,
                        null);

            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        switch (loader.getId()) {

            case MOVIE_DETAIL_LOADER:
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

                break;

            case REVIEW_LOADER:

                if (data != null) {
                    mReviewAdapter.swapCursor(data);
                    setUpRecyclerView("review");
                }
                break;

            case TRAILER_LOADER:

                if (data != null) {
                    int count = data.getCount();
                    int counting = 0;
                    setUpRecyclerView("trailer");
                    mTrailerAdapter.swapCursor(data);
                }
                break;

            default:
                throw new RuntimeException("Loader Not Implemented: " + loader.getId());
        }

    }

    private void setUpRecyclerView(String option) {

        if (option.equals("review")) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                    LinearLayoutManager.VERTICAL, false );
            mReviewList.setAdapter(mReviewAdapter);
            mReviewList.setLayoutManager(layoutManager);
        } else {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                    LinearLayoutManager.HORIZONTAL, false );
            mTrailerList.setAdapter(mTrailerAdapter);
            mTrailerList.setLayoutManager(layoutManager);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Override
    public void openYoutube(String key) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + key));

        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException e) {
            startActivity(webIntent);
        }
    }
}
