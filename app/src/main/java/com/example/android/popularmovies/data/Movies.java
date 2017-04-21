package com.example.android.popularmovies.data;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ehbraheem on 05/04/2017.
 */

// TODO: Add the remaining field for proper database insert

public class Movies implements Parcelable {

    public String title;
    public String posterUrl;
    public String plot;
    public String rating;
    public String releaseDate;
    public String movieId;

    public Movies(JSONObject object) {

        try {
            this.title = object.getString("original_title");
            this.plot = object.getString("overview");
            this.posterUrl = object.getString("poster_path");
            this.releaseDate = object.getString("release_date");
            this.rating = object.getString("vote_average");
            this.movieId = String.valueOf(object.getInt("id"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeStringArray(new String[] {this.title,
                                            this.plot,
                                            this.posterUrl,
                                            this.releaseDate,
                                            this.rating,
                                            this.movieId
        });

    }

    public static final Parcelable.Creator<Movies> CREATOR
            = new Creator<Movies>() {
        @Override
        public Movies createFromParcel(Parcel source) {
            return new Movies(source);
        }

        @Override
        public Movies[] newArray(int size) {
            return new Movies[size];
        }
    };

    private Movies(Parcel source){
        String[] data = new String[5];

        source.readStringArray(data);

        this.title        = data[0];
        this.plot         = data[1];
        this.posterUrl    = data[2];
        this.releaseDate  = data[3];
        this.rating       = data[4];
        this.movieId      = data[5];
    }

    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();

        contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, title);
        contentValues.put(MovieContract.MovieEntry.COLUMN_PLOT, plot);
        contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER_URL, posterUrl);
        contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, releaseDate);
        contentValues.put(MovieContract.MovieEntry.COLUMN_RATING, rating);
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movieId);

        return contentValues;
    }
}
