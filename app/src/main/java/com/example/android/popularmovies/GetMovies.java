package com.example.android.popularmovies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.example.android.popularmovies.utils.APICallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
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

            try {
                HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();

                InputStream stream = new BufferedInputStream(connection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));

                StringBuilder builder = new StringBuilder();
                String inputString;

                while ((inputString = bufferedReader.readLine()) != null) {
                    builder.append(inputString);
                }

                response = new JSONObject(String.valueOf(builder));
                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
            }
            return response;
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
