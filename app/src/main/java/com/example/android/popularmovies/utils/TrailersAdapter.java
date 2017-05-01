package com.example.android.popularmovies.utils;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.android.popularmovies.APIDetails;
import com.example.android.popularmovies.MovieDetail;
import com.example.android.popularmovies.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Ehbraheem on 4/23/2017.
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailerViewHolder> {

    private Cursor mCursor;
    private Context mContext;

    public TrailersAdapter(@NonNull Context context) {
        mContext = context;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        boolean attatchtoParentNow = false;
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.trailer, parent, attatchtoParentNow);
        view.setFocusable(true);

        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {

        mCursor.moveToPosition(position);

        holder.bind();
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder {

        final ImageButton mTrailerThumbnails;
        final TextView mTrailerType;

        public TrailerViewHolder(View view) {

            super(view);

            mTrailerThumbnails = (ImageButton) view.findViewById(R.id.movie_trailer_thumbnails);
            mTrailerType = (TextView) view.findViewById(R.id.trailer_type);
        }

        void bind() {

            String key = mCursor.getString(MovieDetail.INDEX_TRAILER_KEY);
            String type = mCursor.getString(MovieDetail.INDEX_TRAILER_NAME);

            mTrailerType.setText(type);
            Picasso.with(mContext)
                    .load(String.valueOf(APIDetails.trailerThumbnails(key)))
                    .into(mTrailerThumbnails);
        }
    }

    public void swapCursor(Cursor cursor) {
        mCursor = cursor;
        notifyDataSetChanged();
    }
}
