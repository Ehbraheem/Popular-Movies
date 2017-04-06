package com.example.android.popularmovies;

import android.graphics.Movie;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.data.Movies;
import com.example.android.popularmovies.utils.APICallback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class MainActivity extends AppCompatActivity
        implements APICallback{

    private TextView mDisplayDetails;
    private ImageView mPosterDisplay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDisplayDetails = (TextView) findViewById(R.id.tv_display_json);

        mPosterDisplay = (ImageView) findViewById(R.id.im_display_image);

        requestData();
    }


    private void requestData() {

        GetMovies getMovies = new GetMovies(this);

        URL apiDetails = APIDetails.makeResourceUrl();

        getMovies.execute(apiDetails);
    }

    @Override
    public void handleData(JSONObject object) throws JSONException {

        Movies[] movies = MovieParser.parse(object);

        Picasso.with(this)
                .load(String.valueOf(APIDetails.makePosterUrl(movies[0].posterUrl)))
                .into(mPosterDisplay);

        for (Movies movie : movies) {

            mDisplayDetails.append(movie.plot + "/n/n/n");
        }
    }
}
