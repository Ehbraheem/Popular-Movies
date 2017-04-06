package com.example.android.popularmovies.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.android.popularmovies.APIDetails;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.Movies;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by AppsWorkforce2 on 06/04/2017.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private static final String TAG = MoviesAdapter.class.getSimpleName();

    //    private static int viewHolderCout;
    private Context mContext;
//
//    private int nmNumberItems;
    private Movies[] movies;

    public MoviesAdapter(Movies[] movies, Context context) {
        this.movies = movies;
        this.mContext = context;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Log.d(TAG, "#" + position);
        Movies movie = movies[position];
        holder.bind(movie, mContext);
    }

    @Override
    public int getItemCount() {
        return movies.length;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        boolean attatchToParentNow = false;
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movies_card, parent, attatchToParentNow);

        MovieViewHolder movieViewHolder = new MovieViewHolder(view);

        Log.d(TAG, "onCreateViewHolder: called");

        return movieViewHolder;
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        TextView mTitle;
        ImageView mPosterImage;
//        final TextView mPlot;
//        final TextView mReleaseDate;
//        final RatingBar mUserRating;

        public MovieViewHolder(View itemView) {

            super(itemView);

            this.mTitle = (TextView) itemView.findViewById(R.id.movies_title);
            this.mPosterImage = (ImageView) itemView.findViewById(R.id.movies_thumbnail);
//            this.mPlot = (RatingBar) itemView.findViewById(R.id.movies_title);
        }

        void bind(Movies movie, Context context) {
            mTitle.setText(movie.title);
            Picasso.with(context)
                    .load(String.valueOf(APIDetails.makePosterUrl(movie.posterUrl)))
                    .into(mPosterImage);
        }
    }
}