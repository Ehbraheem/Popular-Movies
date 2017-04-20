package com.example.android.popularmovies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.example.android.popularmovies.utils.APICallback;
import com.example.android.popularmovies.utils.MoviesNetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

/**
 * Created by Ehbraheem on 05/04/2017.
 */

public class GetMovies extends AsyncTask<URL, Void, JSONObject> {

    private APICallback callback;
    private Context mContext;

    public GetMovies(APICallback callback, Context context) {
        this.callback = callback;
        this.mContext = context;
    }

    @Override
    protected JSONObject doInBackground(URL... params) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null) {
            URL apiUrl = params[0];
            JSONObject response = null;

            return MoviesNetworkUtils.getMoviesJsonFromUrl(apiUrl);
        }

        JSONObject jsonObject = new JSONObject();
        String json = "Network error or you are not connected!...";
        try {
            jsonObject = jsonObject.put("error", json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(JSONObject object) {
        try {
            callback.handleData(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
