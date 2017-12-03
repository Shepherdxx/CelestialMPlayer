package com.shepherdxx.celestialmp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static com.shepherdxx.celestialmp.MP_MediaPlayer.LOG_TAG;
import static com.shepherdxx.celestialmp.plailist.RadioBD.SEARCH_QUERY_URL_EXTRA;
import static com.shepherdxx.celestialmp.plailist.RadioBD.getResponseFromHttpUrl;

public class LoaderActivity
        extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<String>
{
    TextView tw;
    private static final int RADIO_SEARCH_LOADER = 22;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        tw = findViewById(R.id.ws_tv);
/* подключение лоадера
        *
        */
        getSupportLoaderManager().initLoader(RADIO_SEARCH_LOADER, null, this);
        /*
        */
        startSearch("http://air2.radiorecord.ru:805/rock_320");
    }







    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new android.support.v4.content.AsyncTaskLoader<String>(this) {

            // Override onStartLoading
            @Override
            protected void onStartLoading() {
                // If args is null, return.
                /* If no arguments were passed, we don't have a query to perform. Simply return. */
                if (args == null) {
                    Log.i("onStartLoading","nope");
                    return;
                }
                Log.i("onStartLoading","yep");
//                // Show the loading indicator
//                /*
//                 * When we initially begin loading in the background, we want to display the
//                 * loading indicator to the user
//                 */
//                mLoadingIndicator.setVisibility(View.VISIBLE);

                // COMPLETED (8) Force a load
                forceLoad();
            }

            // Override loadInBackground
            @Override
            public String loadInBackground() {
                // Get the String for our URL from the bundle passed to onCreateLoader
                /* Extract the search query from the args using our constant */
                String key = args.getString(SEARCH_QUERY_URL_EXTRA);
                Log.i("loadInBackground", key);
                // If the URL is null or empty, return null */
                if (key == null
                        || TextUtils.isEmpty(key)) {
                    return null;
                }

                // Copy the try / catch block from the AsyncTask's doInBackground method
                /* Parse the URL from the passed in String and perform the search */
                result=reqText(key);

                return result;
            }
        };

    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<String> loader, String data) {
        tw.setText(data);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<String> loader) {

    }

    String result;
    URL buildUrl(String s) {
        URL radioSearch = null;
        try {
            radioSearch = new URL(s);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }return radioSearch;
    }
    public String reqText(String key){
        String textInfo =null;
        switch(key){
            case "http://air2.radiorecord.ru:805/rock_320" :
                String URI_S="http://www.radiorecord.ru/xml/rock_online_v8.txt";
                URL radioSearch = buildUrl(URI_S);
                String searchResults = null;
                try {
                    searchResults = getResponseFromHttpUrl(radioSearch);
                    Log.i("searchResults", searchResults);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i("searchResults", "WTF");
                }
                textInfo=searchResults;
                break;
        }
        String message = new StringBuilder("reqText")
                .append(File.pathSeparator)
                .append(key)
                .append(File.pathSeparator)
                .append(textInfo)
                .toString();
        Log.i(LOG_TAG,message);
        return textInfo;
    }

    private void startSearch(String key){
        URL vkSearchUrl = buildUrl(key);

        // COMPLETED (19) Create a bundle called queryBundle
        Bundle queryBundle = new Bundle();
        // COMPLETED (20) Use putString with SEARCH_QUERY_URL_EXTRA as the key and the String value of the URL as the value
        queryBundle.putString(SEARCH_QUERY_URL_EXTRA, vkSearchUrl.toString());

        LoaderManager loaderManager = getSupportLoaderManager();
        // COMPLETED (22) Get our Loader by calling getLoader and passing the ID we specified
        Loader<String> SearchLoader = loaderManager.getLoader(RADIO_SEARCH_LOADER);
        // COMPLETED (23) If the Loader was null, initialize it. Else, restart it.
        if (SearchLoader == null) {
            loaderManager.initLoader(RADIO_SEARCH_LOADER, queryBundle, this);
        } else {
            loaderManager.restartLoader(RADIO_SEARCH_LOADER, queryBundle, this);
        }
        Log.i("startSearch", vkSearchUrl.toString());
    }


}
