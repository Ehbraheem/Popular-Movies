package com.example.android.popularmovies;

import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Ehbraheem on 05/04/2017.
 */

public abstract class APIDetails {

    // TODO: ADD Your API Key => here
    private static final String API_KEY = BuildConfig.MOVIE_API_KEY; // Add your key here
    private static final String MOVIE_API = "http://api.themoviedb.org/3/movie/";
    private static final String IMAGE_PATH = "http://image.tmdb.org/t/p/";

    private static final String VIDEO_THUMBNAIL = "http://img.youtube.com/vi/";
    public static final String DEFAULT_THUMBNAIL = "hqdefault.jpg";

    /*
    @param  String [apiPath, itemPath], boolean attatchKey
    @return
     */
    private static String attachExtraPath(String baseUrl, String ... paths) {
        String newPath = baseUrl;

        for (String path: paths) {
            newPath += path;
        }
        return newPath;
    }


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
        String absolutePath = attachExtraPath(IMAGE_PATH, size);
        return makeUrl(false, absolutePath, posterPath);
    }

    public static URL makeResourceUrl (String type) {
        String resourceType = attachExtraPath(MOVIE_API, type);
        return makeUrl(true, resourceType);
    }

    public static URL makeTraillerUrl(String key) {
        String movieInfo = attachExtraPath(MOVIE_API, key, "/videos");
        return makeUrl(true, movieInfo);
    }

    public static URL reviewUrl (String key) {
        String reviewInfo = attachExtraPath(MOVIE_API, key, "/reviews");
        return makeUrl(true, reviewInfo);
    }

    public static URL trailerThumbnails(String key) {
        String videoThumbnail = attachExtraPath(VIDEO_THUMBNAIL, key);
        return makeUrl(false, videoThumbnail, DEFAULT_THUMBNAIL);
    }
}