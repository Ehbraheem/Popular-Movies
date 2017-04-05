package com.example.android.popularmovies;

import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Ehbraheem on 05/04/2017.
 */

abstract class APIDetails {

    private static final String API_KEY = "522e083da03504672b87e8e12a6cbe55";
    private static final String MOVIE_API = "http://api.themoviedb.org/3/movie/popular";
    private static final String IMAGE_PATH = "http://image.tmdb.org/t/p/w185/";

    /*
    @param  String [apiPath, itemPath], boolean attatchKey
    @return
     */
    private static URL makeUrl (boolean attatchKey, String ... path) {
        URL url = null;
        String itemPath = null;
        String apiPath = path[0];
        Uri resourceUri = null;

        Uri.Builder uri = Uri.parse(apiPath)
                .buildUpon();

        if (path.length == 2 && !attatchKey) { // We are not appending the key
            itemPath = path[1];
            resourceUri = uri.appendPath(itemPath).build();
        } else {
            resourceUri = uri.appendQueryParameter("api_key", API_KEY).build();
        }

        try {
           url = new URL(resourceUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    static URL makePosterUrl (String posterPath) {
        return makeUrl(false, IMAGE_PATH, posterPath);
    }

    static URL makeResourceUrl () {
        return makeUrl(true, MOVIE_API);
    }
}
