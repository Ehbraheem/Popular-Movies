package com.example.android.popularmovies.utils;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.MovieDetail;
import com.example.android.popularmovies.R;

/**
 * Created by Ehbraheem on 4/22/2017.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {

    private Cursor mCursor;
    private Context mContext;

    public ReviewsAdapter(@NonNull Context context) {
        mContext = context;
    }


    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        boolean attatchToParentNow = false;
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review, parent, attatchToParentNow);

        view.setFocusable(true);

        ReviewViewHolder viewHolder = new ReviewViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {

        mCursor.moveToPosition(position);

        holder.bind();
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        final TextView mReview;
        final TextView mAuthor;


        public ReviewViewHolder(View itemView) {
            super(itemView);

            this.mAuthor = (TextView) itemView.findViewById(R.id.reviewer_name);
            this.mReview = (TextView) itemView.findViewById(R.id.movies_review);
        }

        void bind() {
            String content = mCursor.getString(MovieDetail.INDEX_REVIEW_CONTENT);
            String author  = mCursor.getString(MovieDetail.INDEX_REVIEW_AUTHOR);

            mReview.setText(content);
            mAuthor.setText(author);
        }
    }

    public void swapCursor(Cursor cursor) {
        mCursor = cursor;
        notifyDataSetChanged();
    }
}
