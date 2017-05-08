package com.example.android.popularmovies;

import android.content.ContentValues;
import android.support.v4.app.LoaderManager;
import android.app.ProgressDialog;
import android.support.v4.content.CursorLoader;
import android.content.Intent;
import android.support.v4.content.Loader;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.utils.APICallback;
import com.example.android.popularmovies.utils.MoviesAdapter;
import com.example.android.popularmovies.utils.MoviesSyncService;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>, APICallback, MoviesAdapter.ListItemClickListener{

    public static final String[] MAIN_MOVIE_PROJECTION = {
            MovieContract.MovieEntry.COLUMN_POSTER_URL,
            MovieContract.MovieEntry.COLUMN_RATING,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_FAVORITE,
    };


    public static final int INDEX_MOVIE_POSTER_URL = 0;
    public static final int INDEX_MOVIE_RATING = 1;
    public static final int INDEX_MOVIE_TITLE = 2;
    public static final int INDEX_MOVIE_ID = 3;
    public static final int INDEX_FAVORITE = 4;

    private RecyclerView mMoviesList;
    private MoviesAdapter mMoviesAdapter;
    private GridLayoutManager mLayoutManager;
    private ProgressDialog mProgressDialog;
    private TextView mErrorTextView;

    public static final String POPULAR_MOVIES = "popular";

    public static final String MOST_RATED_MOVIES = "top_rated";

    public static final String FAVORITE_MOVIES = "favorites";

    private static final int MOVIE_LOADER_ID = 778;

    private static final int FAVORITE_MOVIES_LOADER_ID = 224;

    private static final int MOST_RATED_MOVIES_LOADER_ID = 445;

    public static final int POPULAR_MOVIES_LOADER_ID = 667;

//    private ImageView mErrorImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        collapsingToolbar();

        mMoviesList    = (RecyclerView) findViewById(R.id.movies_list);

        mErrorTextView = (TextView) findViewById(R.id.error_textview);
//        mErrorImage    = (ImageView) findViewById(R.id.error_image);

        mMoviesList.setHasFixedSize(true);

        mProgressDialog = new ProgressDialog(this);

        mProgressDialog.setMessage("Please Wait...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.show();


        getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);

        Intent intent = new Intent(this, MoviesSyncService.class);
        startService(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.movie_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_popular) {
            requestData(POPULAR_MOVIES_LOADER_ID);
            return true;
        } else if (id == R.id.action_highest_rated) {
            requestData(MOST_RATED_MOVIES_LOADER_ID);
            return true;
        } else if (id == R.id.favorite_movies) {
            requestData(FAVORITE_MOVIES_LOADER_ID);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void requestData(int id) {

        mProgressDialog = new ProgressDialog(this);

        mProgressDialog.setMessage("Please Wait...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.show();
//
//        Context context = getApplicationContext();
////        GetMovies getMovies = new GetMovies(this, context);
//
//        URL apiDetails = APIDetails.makeResourceUrl(type);
//
////        getMovies.execute(apiDetails);

        getSupportLoaderManager().restartLoader(id, null, this);
    }

    @Override
    public void handleData(JSONObject object) throws JSONException {

        mProgressDialog.cancel();

        if (object.has("error")) {

            mMoviesList.setVisibility(View.INVISIBLE);

//            mErrorImage.setVisibility(View.VISIBLE);

            String message = object.getString("error");

            mErrorTextView.setVisibility(View.VISIBLE);
            mErrorTextView.setText(message);


        } else {

            mMoviesAdapter = new MoviesAdapter(this, this);

            setUpRecyclerView();


        }


//        for (Movies movie : movies) {
//
//            mDisplayDetails.append(movie.plot + "/n/n/n");
//        }
    }

    private  int convertDPtoPixel(int dp) {
        Resources resources = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics()));
    }

    private void setUpRecyclerView() {

//        mErrorImage.setVisibility(View.GONE);
        mErrorTextView.setVisibility(View.GONE);

        int dpToPixel = convertDPtoPixel(10);
        boolean includeEdge = true;

        GridSpacing gridSpacing = new GridSpacing(2, dpToPixel, includeEdge);
        mLayoutManager = new GridLayoutManager(this, 2);

        if (mMoviesAdapter != null) {
            mMoviesList.setLayoutManager(mLayoutManager);
            mMoviesList.addItemDecoration(gridSpacing);
            mMoviesList.setItemAnimator(new DefaultItemAnimator());
            mMoviesList.setAdapter(mMoviesAdapter);
        }
    }

    @Override
    public void onListItemClick(int itemIndex) {

        Class detailActivity = MovieDetail.class;

        Intent intent = new Intent(this, detailActivity);

        Uri uriForMovieClicked = MovieContract.MovieEntry.buildUriWithId(itemIndex);

        intent.setData(uriForMovieClicked);

        startActivity(intent);
    }

    @Override
    public void onFavoriteItemClick(int itemIndex) {
        ContentValues cv = new ContentValues();
        cv.put(MovieContract.MovieEntry.COLUMN_FAVORITE, 1);
        Uri uriForFavoriteMovie = MovieContract.MovieEntry.buildUriWithId(itemIndex);

        getContentResolver().update(uriForFavoriteMovie, cv, null, null);
    }

    /*
          Initializing collapsing toolbar
          Will show and hide the toolbar title on scroll
         */
    private void collapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)
                findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar is expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }

                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {

        Uri moviesUri;

        switch (loaderId) {

            case MOVIE_LOADER_ID:

                 moviesUri = MovieContract.MovieEntry.CONTENT_URI;

                return new CursorLoader(
                        this,
                        moviesUri,
                        MAIN_MOVIE_PROJECTION,
                        null,
                        null,
                        null
                );

            case FAVORITE_MOVIES_LOADER_ID:

                moviesUri = MovieContract.MovieEntry.FAVORITE_URI;

                return new CursorLoader(
                        this,
                        moviesUri,
                        MAIN_MOVIE_PROJECTION,
                        null,
                        null,
                        null
                );

            case POPULAR_MOVIES_LOADER_ID:

                moviesUri = MovieContract.MovieEntry.CONTENT_URI;
                String popularSelector = MovieContract.MovieEntry.createSqlSelectorForCategories();

                String[] popularSelectorArgs = new String[]{POPULAR_MOVIES};

                return new CursorLoader(
                        this,
                        moviesUri,
                        MAIN_MOVIE_PROJECTION,
                        popularSelector,
                        popularSelectorArgs,
                        null
                );

            case MOST_RATED_MOVIES_LOADER_ID:

                moviesUri = MovieContract.MovieEntry.CONTENT_URI;
                String mostRatedSelector = MovieContract.MovieEntry.createSqlSelectorForCategories();

                String[] mostRatedSelectorArgs = new String[]{MOST_RATED_MOVIES};

                return new CursorLoader(
                        this,
                        moviesUri,
                        MAIN_MOVIE_PROJECTION,
                        mostRatedSelector,
                        mostRatedSelectorArgs,
                        null
                );


            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mProgressDialog.cancel();
        mMoviesAdapter = new MoviesAdapter(this, this);

        setUpRecyclerView();

        mMoviesAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMoviesAdapter.swapCursor(null);
    }
}
