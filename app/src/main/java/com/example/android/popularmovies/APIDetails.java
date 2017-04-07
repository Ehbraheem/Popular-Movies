package com.example.android.popularmovies;

import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Ehbraheem on 05/04/2017.
 */

public abstract class APIDetails {

    private static final String API_KEY = "522e083da03504672b87e8e12a6cbe55";
    private static final String MOVIE_API = "http://api.themoviedb.org/3/movie/popular";
    private static final String IMAGE_PATH = "http://image.tmdb.org/t/p/";

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
            resourceUri = uri.appendEncodedPath(itemPath).build();
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

    public static URL makePosterUrl (String posterPath, String size) {
        String absolutePath = IMAGE_PATH + size;
        return makeUrl(false, absolutePath, posterPath);
    }

    public static URL makeResourceUrl () {
        return makeUrl(true, MOVIE_API);
    }
}
