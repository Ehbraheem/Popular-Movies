package com.example.android.popularmovies.utils;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.android.popularmovies.APIDetails;
import com.example.android.popularmovies.MainActivity;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.Movies;
import com.squareup.picasso.Picasso;

import java.net.URL;


/**
 * Created by Ehbraheem on 06/04/2017.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private static final String TAG = MoviesAdapter.class.getSimpleName();

    private ListItemClickListener mOnclickListener;

    //    private static int viewHolderCout;
    private Context mContext;

    public interface ListItemClickListener {

        void onListItemClick(int itemIndex);

        void onFavoriteItemClick(int itemIndex);
    }
//
//    private int nmNumberItems;
//    private Movies[] movies;
    private Cursor mCursor;

    public MoviesAdapter(Context context, ListItemClickListener listener) {
        this.mContext = context;
        this.mOnclickListener = listener;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {

        mCursor.moveToPosition(position);

        Log.d(TAG, "#" + position);
        holder.bind(mContext);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        boolean attatchToParentNow = false;
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movies_card, parent, attatchToParentNow);

        view.setFocusable(true);

        MovieViewHolder movieViewHolder = new MovieViewHolder(view);

        Log.d(TAG, "onCreateViewHolder: called");

        return movieViewHolder;
    }

    class MovieViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        final TextView mTitle;
        final ImageButton mPosterImage;

        final RatingBar mUserRating;

        final ImageButton mFavoriteView;

        public MovieViewHolder(View itemView) {

            super(itemView);

            this.mTitle = (TextView) itemView.findViewById(R.id.movies_title);
            this.mPosterImage = (ImageButton) itemView.findViewById(R.id.movies_thumbnail);
            this.mUserRating = (RatingBar) itemView.findViewById(R.id.movie_rating);
            this.mFavoriteView = (ImageButton) itemView.findViewById(R.id.action_add_favorite);

            mPosterImage.setOnClickListener(this);
            mFavoriteView.setOnClickListener(this);
//            this.mPlot = (RatingBar) itemView.findViewById(R.id.movies_title);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mCursor.moveToPosition(position);
            int movieId = mCursor.getInt(MainActivity.INDEX_MOVIE_ID);
            if (v.getId() == R.id.action_add_favorite) {
                mOnclickListener.onFavoriteItemClick(movieId);
            } else {
                mOnclickListener.onListItemClick(movieId);
            }
        }

        void bind(Context context) {

            String imageSize = "w185/";

            String title = mCursor.getString(MainActivity.INDEX_MOVIE_TITLE);
            String posterUrl = mCursor.getString(MainActivity.INDEX_MOVIE_POSTER_URL);
            Float rating = mCursor.getFloat(MainActivity.INDEX_MOVIE_RATING);
            int favorite = mCursor.getInt(MainActivity.INDEX_FAVORITE);

            mTitle.setText(title);

            rating = rating / 2;

            mUserRating.setNumStars(5);
            mUserRating.setStepSize(0.5f);
            mUserRating.setRating(rating);

            URL imageUrl = APIDetails.makePosterUrl(posterUrl, imageSize );

            Picasso.with(context)
                    .load(String.valueOf(imageUrl))
                    .into(mPosterImage);

            mFavoriteView.setImageResource(favorite == 0 ? R.drawable.ic_not_liked : R.drawable.ic_liked);
        }
    }

    public void swapCursor(Cursor cursor) {
        mCursor = cursor;
        notifyDataSetChanged();
    }
}