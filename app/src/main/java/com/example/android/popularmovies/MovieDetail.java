package com.example.android.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.utils.MoviesNetworkUtils;
import com.example.android.popularmovies.utils.ReviewSyncService;
import com.example.android.popularmovies.utils.ReviewsAdapter;
import com.example.android.popularmovies.utils.TrailersAdapter;
import com.squareup.picasso.Picasso;

public class MovieDetail extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>,
        TrailersAdapter.TrailerVideoClickListener,
        SharedPreferences.OnSharedPreferenceChangeListener{

    private TextView mMovieTitle;
    private ImageView mMoviePoster;
    private RatingBar mMovieRating;
    private TextView mMoviePlot;
    private TextView mMovieReleaseDate;

    private RecyclerView mReviewList;
    private RecyclerView mTrailerList;

    private ReviewsAdapter mReviewAdapter;
    private TrailersAdapter mTrailerAdapter;

    private ScrollView mScrollView;

    private static final String[] MOVIE_DETAIL_PROJECTION = {
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
            MovieContract.TrailerEntry.COLUMN_KEY
    };


    public static final int INDEX_MOVIE_PLOT = 0;
    public static final int INDEX_MOVIE_FAVORITE = 1;
    public static final int INDEX_MOVIE_POSTER_URL = 2;
    public static final int INDEX_MOVIE_RATING = 3;
    public static final int INDEX_MOVIE_RELEASE_DATE = 4;
    public static final int INDEX_MOVIE_TITLE = 5;

    public static final int INDEX_REVIEW_AUTHOR  = 0;
    public static final int INDEX_REVIEW_CONTENT = 1;

    public static final int INDEX_TRAILER_NAME = 0;
    public static final int INDEX_TRAILER_KEY   = 1;

    private Uri mUri;

    private static final int MOVIE_DETAIL_LOADER = 445;

    private static final int REVIEW_LOADER = 556;

    private static final int TRAILER_LOADER = 756;

    public static final String MOVIE_ID_KEY = "movieId";

    private static final String MOVIE_TITLE_KEY  = "title";
    private static final String MOVIE_PLOT_KEY = "plot";
    private static final String MOVIE_RELEASE_DATE_KEY = "release_date";
    private static final String MOVIE_POSTER_URL_KEY = "poster_url";
    private static final String MOVIE_RATING_KEY = "rating";

    private String posterUrl;

    private String movieId;

    private SharedPreferences sharedPreferences;

    private boolean showAllVideos;
    private int videoQuality;
    private String numOfLines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ActionBar actionBar = this.getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mMoviePoster      = (ImageView) findViewById(R.id.movie_detail_thumbnail);
        mMovieTitle       = (TextView) findViewById(R.id.movie_detail_title);
        mMovieRating      = (RatingBar) findViewById(R.id.movie_detail_rating);
        mMoviePlot        = (TextView) findViewById(R.id.movie_detail_plot);
        mMovieReleaseDate = (TextView) findViewById(R.id.movie_detail_release_date);
        mReviewList       = (RecyclerView) findViewById(R.id.review_list);
        mTrailerList      = (RecyclerView) findViewById(R.id.trailer_list);
        mScrollView       = (ScrollView) findViewById(R.id.scrollView);

        mReviewAdapter  = new ReviewsAdapter(this);
        mTrailerAdapter = new TrailersAdapter(this, this);

        setUpSharedPreferences();

        mUri = getIntent().getData();

        if (mUri == null) throw new NullPointerException("URI for MovieDetail cannot be null");

        movieId = mUri.getLastPathSegment();


        if (savedInstanceState != null) {
            restoreFromState(savedInstanceState);
        } else {
            getSupportLoaderManager().restartLoader(MOVIE_DETAIL_LOADER, null, this);
        }

        getSupportLoaderManager().initLoader(REVIEW_LOADER, null, this);
        getSupportLoaderManager().initLoader(TRAILER_LOADER, null, this);

        checkNetWorkAndStartService();

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

    private void restoreFromState(Bundle savedInstanceState) {
        String imageSize = "w500/";

        String title = savedInstanceState.getString(MOVIE_TITLE_KEY);
        String plot = savedInstanceState.getString(MOVIE_PLOT_KEY);
        String releaseDate = savedInstanceState.getString(MOVIE_RELEASE_DATE_KEY);
        Float rating = savedInstanceState.getFloat(MOVIE_RATING_KEY);
        String poster = savedInstanceState.getString(MOVIE_POSTER_URL_KEY);
        mMovieTitle.setText(title);

        mMoviePlot.setText(plot);
        mMovieReleaseDate.setText(releaseDate);

        rating /= 2;
        mMovieRating.setNumStars(5);
        mMovieRating.setStepSize(0.5f);
        mMovieRating.setRating(rating);

        Picasso.with(this)
                .load(String.valueOf(APIDetails.makePosterUrl(poster,imageSize)))
                .into(mMoviePoster);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String title = mMovieTitle.getText().toString();
        String plot  = mMoviePlot.getText().toString();
        String releaseDate = mMovieReleaseDate.getText().toString();
        Float rating = mMovieRating.getRating();

        outState.putString(MOVIE_TITLE_KEY, title);
        outState.putString(MOVIE_PLOT_KEY, plot);
        outState.putString(MOVIE_RELEASE_DATE_KEY, releaseDate);
        outState.putString(MOVIE_POSTER_URL_KEY, posterUrl);
        outState.putFloat(MOVIE_RATING_KEY, rating);
    }

    private void checkNetWorkAndStartService() {
        final boolean connected = MoviesNetworkUtils.networkCheck(this);
        if (connected) {
            startTrailerAndReviewService();
        } else {

            Snackbar snackbar = Snackbar.make(mScrollView,
                    "Unable to sync reviews and trailers! ", Snackbar.LENGTH_LONG);

            snackbar.setAction("RETRY", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MoviesNetworkUtils.networkCheck(getApplicationContext())) {
                        startTrailerAndReviewService();
                    } else return;
                }
            });

            snackbar.setActionTextColor(Color.RED);
            snackbar.show();
        }
    }

    private void startTrailerAndReviewService() {
        Intent intent = new Intent(this, ReviewSyncService.class);
//        Intent trailerService = new Intent(this, TrailerSyncService.class);

        intent.putExtra(MOVIE_ID_KEY, movieId);
//        trailerService.putExtra(MOVIE_ID_KEY, movieId);

        startService(intent);
//        startService(trailerService);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movie_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_open_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
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

                String selection;
                String[] selectionAgrs;

                if (showAllVideos) {
                    selection = MovieContract.TrailerEntry.COLUMN_TYPE + " , " +
                            MovieContract.TrailerEntry.COLUMN_SIZE + " = ?, ? ";
                    selectionAgrs = makeSelectionArgs("Trailer", String.valueOf(videoQuality));
                } else {
                    selection = null;
                    selectionAgrs = null;
                }

                return new CursorLoader(this,
                        trailerUri,
                        TRAILER_PROJECTION,
                        selection,
                        selectionAgrs,
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
                posterUrl = data.getString(INDEX_MOVIE_POSTER_URL);
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

    private String[] makeSelectionArgs(String ... select) {
        return select;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_all_videos))) {
            showAllVideos = sharedPreferences.getBoolean(getString(R.string.pref_all_videos),
                    getResources().getBoolean(R.bool.show_all_videos));
        } else if (key.equals(getString(R.string.pref_video_quality))) {
            videoQuality = Integer.parseInt(sharedPreferences.getString(getString(R.string.pref_video_quality),
                    getString(R.string.pref_low_hd)));
        } else if (key.equals(getString(R.string.pref_reviews_length))) {
            numOfLines = sharedPreferences.getString(getString(R.string.pref_reviews_length),
                    getString(R.string.length_value_3));

            mReviewAdapter.swapNumberOfText(numOfLines);
        }
    }

    private void setUpSharedPreferences() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        showAllVideos = sharedPreferences.getBoolean(getString(R.string.pref_all_videos),
                getResources().getBoolean(R.bool.show_all_videos));

        videoQuality = Integer.parseInt(sharedPreferences.getString(getString(R.string.pref_video_quality),
                getString(R.string.pref_low_hd)));

        numOfLines = sharedPreferences.getString(getString(R.string.pref_reviews_length),
                getString(R.string.length_value_3));

        mReviewAdapter.swapNumberOfText(numOfLines);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }
}
