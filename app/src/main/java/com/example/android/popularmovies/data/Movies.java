package com.example.android.popularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ehbraheem on 05/04/2017.
 */

public class Movies implements Parcelable {

    public String title;
    public String posterUrl;
    public String plot;
    public String rating;
    public String releaseDate;

    public Movies(JSONObject object) {

        try {
            this.title = object.getString("original_title");
            this.plot = object.getString("overview");
            this.posterUrl = object.getString("poster_path");
            this.releaseDate = object.getString("release_date");
            this.rating = object.getString("vote_average");

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
                                            this.rating
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
    }
}
