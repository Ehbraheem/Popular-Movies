package com.example.android.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.android.popularmovies.data.Movies;
import com.squareup.picasso.Picasso;

public class MovieDetail extends AppCompatActivity {

    private TextView mMovieTitle;
    private ImageView mMoviePoster;
    private RatingBar mMovieRating;
    private TextView mMoviePlot;
    private TextView mMovieReleaseDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        this.getSupportActionBar().setDisplayShowHomeEnabled(true);

        mMoviePoster      = (ImageView) findViewById(R.id.movie_detail_thumbnail);
        mMovieTitle       = (TextView) findViewById(R.id.movie_detail_title);
        mMovieRating      = (RatingBar) findViewById(R.id.movie_detail_rating);
        mMoviePlot        = (TextView) findViewById(R.id.movie_detail_plot);
        mMovieReleaseDate = (TextView) findViewById(R.id.movie_detail_release_date);

        Intent intent = getIntent();

        if (intent.hasExtra(Intent.EXTRA_TEXT)) {
            Bundle data = intent.getExtras();
            Movies movie = data.getParcelable(Intent.EXTRA_TEXT);

            if (movie != null) {

                mMovieTitle.setText(movie.title);

                mMoviePlot.setText(movie.plot);
                mMovieReleaseDate.setText(movie.releaseDate);

                float rating = Float.parseFloat(movie.rating) / 2;
                mMovieRating.setNumStars(5);
                mMovieRating.setStepSize(0.5f);
                mMovieRating.setRating(rating);

                String imageSize = "w780/";

                Picasso.with(this)
                        .load(String.valueOf(APIDetails.makePosterUrl(movie.posterUrl,imageSize)))
                        .into(mMoviePoster);
            }
        }
    }
}
