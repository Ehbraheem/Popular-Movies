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
import com.example.android.popularmovies.MainActivity;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.Movies;
import com.squareup.picasso.Picasso;



/**
 * Created by AppsWorkforce2 on 06/04/2017.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private static final String TAG = MoviesAdapter.class.getSimpleName();

    private ListItemClickListener mOnclickListener;

    //    private static int viewHolderCout;
    private Context mContext;

    public interface ListItemClickListener {

        void onListItemClick(int itemIndex, Movies movie);
    }
//
//    private int nmNumberItems;
    private Movies[] movies;

    public MoviesAdapter(Movies[] movies, MainActivity activity) {
        this.movies = movies;
        this.mContext = activity;
        this.mOnclickListener = activity;
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

    class MovieViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        final TextView mTitle;
        final ImageView mPosterImage;

        final RatingBar mUserRating;

        public MovieViewHolder(View itemView) {

            super(itemView);

            this.mTitle = (TextView) itemView.findViewById(R.id.movies_title);
            this.mPosterImage = (ImageView) itemView.findViewById(R.id.movies_thumbnail);
            this.mUserRating = (RatingBar) itemView.findViewById(R.id.movie_rating);

            itemView.setOnClickListener(this);
//            this.mPlot = (RatingBar) itemView.findViewById(R.id.movies_title);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnclickListener.onListItemClick(clickedPosition, movies[clickedPosition]);
        }

        void bind(Movies movie, Context context) {

            String imageSize = "w185/";

            mTitle.setText(movie.title);

            float rating = Float.parseFloat(movie.rating) / 2;

            mUserRating.setNumStars(5);
            mUserRating.setStepSize(0.5f);
            mUserRating.setRating(rating);

            Picasso.with(context)
                    .load(String.valueOf(APIDetails.makePosterUrl(movie.posterUrl, imageSize )))
                    .into(mPosterImage);
        }
    }
}