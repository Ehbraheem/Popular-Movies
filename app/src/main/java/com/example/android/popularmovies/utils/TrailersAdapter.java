package com.example.android.popularmovies.utils;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.popularmovies.MovieDetail;
import com.example.android.popularmovies.R;

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

        final Button mTrailerName;
        final TextView mTrailerType;

        public TrailerViewHolder(View view) {

            super(view);

            mTrailerName = (Button) view.findViewById(R.id.trailer_name);
            mTrailerType = (TextView) view.findViewById(R.id.trailer_type);
        }

        void bind() {

            String name = mCursor.getString(MovieDetail.INDEX_TRAILER_NAME);
            String type = mCursor.getString(MovieDetail.INDEX_TRAILER_TYPE);

            mTrailerType.setText(type);
            mTrailerName.setText(name);
        }
    }

    public void swapCursor(Cursor cursor) {
        mCursor = cursor;
        notifyDataSetChanged();
    }
}
