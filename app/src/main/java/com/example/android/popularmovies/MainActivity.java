package com.example.android.popularmovies;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
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

import com.example.android.popularmovies.data.Movies;
import com.example.android.popularmovies.utils.APICallback;
import com.example.android.popularmovies.utils.MoviesAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class MainActivity extends AppCompatActivity
        implements APICallback, MoviesAdapter.ListItemClickListener {

    private RecyclerView mMoviesList;
    private MoviesAdapter mMoviesAdapter;
    private GridLayoutManager mLayoutManager;
    private ProgressDialog mProgressDialog;
    private TextView mErrorTextView;

    private static final String POPULAR_MOVIES = "popular";

    private static final String MOST_RATED_MOVIES = "top_rated";

//    private ImageView mErrorImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbar();
//        setSupportActionBar(toolbar);

        mMoviesList    = (RecyclerView) findViewById(R.id.movies_list);

        mErrorTextView = (TextView) findViewById(R.id.error_textview);
//        mErrorImage    = (ImageView) findViewById(R.id.error_image);

        mMoviesList.setHasFixedSize(true);


        requestData(POPULAR_MOVIES);
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
            requestData(POPULAR_MOVIES);
            return true;
        } else if (id == R.id.action_highest_rated) {
            requestData(MOST_RATED_MOVIES);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void requestData(String type) {

        mProgressDialog = new ProgressDialog(this);

        mProgressDialog.setMessage("Please Wait...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.show();

        Context context = getApplicationContext();
        GetMovies getMovies = new GetMovies(this, context);

        URL apiDetails = APIDetails.makeResourceUrl(type);

        getMovies.execute(apiDetails);
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

            Movies[] movies = MovieParser.parse(object);

            mMoviesAdapter = new MoviesAdapter(movies, this);

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
    public void onListItemClick(int itemIndex, Movies movies) {

        Class detailActivity = MovieDetail.class;

        Intent intent = new Intent(this, detailActivity);

        Bundle bundle = new Bundle();
        bundle.putParcelable(Intent.EXTRA_TEXT, movies);

        intent.putExtras(bundle);

        startActivity(intent);
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
}
