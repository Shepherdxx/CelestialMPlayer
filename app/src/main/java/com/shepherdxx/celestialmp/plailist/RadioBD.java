package com.shepherdxx.celestialmp.plailist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageButton;

import com.shepherdxx.celestialmp.MP_BackgroundService;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import static com.shepherdxx.celestialmp.extras.Constants.BUNDLE;
import static com.shepherdxx.celestialmp.extras.Constants.MP_PLAY;
import static com.shepherdxx.celestialmp.extras.Constants.MP_RADIO;
import static com.shepherdxx.celestialmp.extras.Constants.URI_RADIO_BASE;
import static com.shepherdxx.celestialmp.extras.Constants.URI_RADIO_AFTER_BASE;

/**
 * Created by Shepherdxx on 03.11.2017.
 */

public class RadioBD {

    public RadioBD(Activity mActivity){
        this.mActivity = mActivity;
    }

    /** scheme:[//[user:password@]host[:port]][/]path[?query][#fragment]
     * String URI_Second_BASE = "https://www.internet-radio.com/station/"
     */
    final static String URL_SCHEME ="http://";
    final static String AUTHORITY = "www.internet-radio.com";
    final static String PATH ="stations";

    public final static String SEARCH_QUERY_URL_EXTRA="URL_EXTRA";

//    final static String PARAM_QUERY = "c[q]";
//    final static String PARAM_SECTION = "c[section]";
//    final static String PARAM_PERFORMER = "c[performer]";
//    final static String LOGIN = "+79831419764:Space7fun@";


    /*
     * The sort field. One of stars, forks, or updated.
     * Default: results are sorted by best match if no field is specified.
     */
//    final static String PARAM_SORT = "sort";
//    final static String sortBy = "stars";
    final static String performer = "1";
    final static String section = "audio";

    /**
     * Builds the URL used to query Github.
     *
     * @param mSearch The keyword that will be queried for.
     * @return The URL to use to query the weather server.
     */
    public static URL buildUrl(String mSearch) {
        // Fill in this method to build the proper Github query URL
        Uri builtUri = Uri.parse(URL_SCHEME).buildUpon()
//                .appendQueryParameter(PARAM_QUERY, mSearch)
//                .appendQueryParameter(PARAM_SECTION, section)
//                .appendQueryParameter(PARAM_PERFORMER, performer)
                .authority(AUTHORITY)
                .appendPath(PATH)

                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }





//    url1 = input("Please enter a URL from Tunein Radio: ");
//    request = urllib.request.Request(url1);
//    response = urllib.request.urlopen(request);
//    raw_file = response.read().decode('utf-8');
//    API_key = re.findall(r"StreamUrl\":\"(.*?),\"",raw_file);
//#print API_key;
//#print "The API key is: " + API_key[0];
//    request2 = urllib.request.Request(str(API_key[0]));
//    response2 = urllib.request.urlopen(request2);
//    key_content = response2.read().decode('utf-8');
//    raw_stream_url = re.findall(r"Url\": \"(.*?)\"",key_content);
//    bandwidth = re.findall(r"Bandwidth\":(.*?),", key_content);
//    reliability = re.findall(r"lity\":(.*?),", key_content);
//    isPlaylist = re.findall(r"HasPlaylist\":(.*?),",key_content);
//    codec = re.findall(r"MediaType\": \"(.*?)\",", key_content);
//    tipe = re.findall(r"Type\": \"(.*?)\"", key_content);
//    total = 0
//            for element in raw_stream_url:
//    total = total + 1
//    i = 0
//    print ("I found " + str(total) + " streams.");
//for element in raw_stream_url:
//    print ("Stream #" + str(i + 1));
//    print ("Stream stats:");
//    print ("Bandwidth: " + str(bandwidth[i]) + " kilobytes per second.");
//    print ("Reliability: " + str(reliability[i]) + "%");
//    print ("HasPlaylist: " + str(isPlaylist[i]));
//    print ("Stream codec: " + str(codec[i]));
//    print ("This audio stream is " + tipe[i].lower());
//    print ("Pure streaming URL: " + str(raw_stream_url[i]));
//    i = i + 1
//    input("Press enter to close")


    private static final int RADIO_SEARCH_LOADER = 22;



    int index;
    private Uri mUri;
//    AdapterMS mAdapter;
    RecyclerView mRadioList;
    LinearLayoutManager mLayoutManager;
    int mCurCheckPosition = 5;
    Context context;
    private Activity mActivity;
    private ArrayList<PlayerTrackInfo> rows = new ArrayList<>();
    String ERROR = "Check your connection";
    ImageButton btPlay, btNext, btBack;
    String[] RadioPath;
    String[] songTitle;
    Toolbar tb;
    String[] adi = {"http://us3.internet-radio.com:8007/",
            "http://air2.radiorecord.ru:805/rock_320",
            "http://84.22.142.130:8000/arstream?4&28",
            "http://81.88.36.42:8010/"};
    int MPType, MPState;
    Intent intent;
    Bundle Bondiana;
    Handler myHandler = new Handler();

//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.radio_player);
//
//        /* подключение лоадера
//        *
//        */
//        getSupportLoaderManager().initLoader(RADIO_SEARCH_LOADER, null, this);
//        /*
//        */
//        context = this;
//
//        RadioPlayListCreation();
//        findView();
//        mLayoutManager = new LinearLayoutManager(this);
//        mRadioList.setLayoutManager(mLayoutManager);
//        mAdapter = new AdapterMS(context, rows);
//        mRadioList.setAdapter(mAdapter);
//        mAdapter.setmOnViewClicklListener(this);
//
    public Intent RadioIntent(int mCurCheckPosition){
        RadioPlayListCreation();
        intent = new Intent(mActivity, MP_BackgroundService.class);
        Bondiana = new Bundle();
        Bondiana.putInt("MPType", MPType);
        Bondiana.putInt("MPState",MP_PLAY);
        Bondiana.putInt("MPData",mCurCheckPosition);
        Bondiana.putStringArray("SongPath", RadioPath);
        Bondiana.putStringArray("SongTitle", songTitle);
        intent.putExtra(BUNDLE,Bondiana);
        return intent;
    }

    public Intent RadioIntent(){
        return RadioIntent(mCurCheckPosition);
    }

    //создание плейлиста
    private void RadioPlayListCreation() {
        MPType = MP_RADIO;
        if (rows.isEmpty()) {
            for (index = 1; index <= 5; index++) {
                mUri = Uri.parse(URI_RADIO_BASE + String.valueOf(index) + URI_RADIO_AFTER_BASE);
                Log.i("Radio ", String.valueOf(index) + " " + String.valueOf(mUri));
                rows.add(new PlayerTrackInfo("Radio " + String.valueOf(index), mUri.toString(), mUri));
            }
            addMore();

            Log.i("Radio", "плейлист создан");
        } else Log.i("Radio", "плейлист уже есть");
        //Создание Листа путей
        CreatePathList();
    }

    private void addMore(){
        for (int i = 0; i < adi.length; i++) {
            mUri = Uri.parse(adi[i]);
            Log.i("Radio ", String.valueOf(index+i) + " " + String.valueOf(mUri));
            rows.add(new PlayerTrackInfo("Radio " + String.valueOf(index+i), mUri.toString(), mUri));
        }
    }

    private void CreatePathList() {
        int st;
        RadioPath = new String[rows.size()];
        songTitle = new String[rows.size()];
        try {
            for (st = 0; st < rows.size(); st++) {
                RadioPath[st] = String.valueOf(rows.get(st).getmUri());
                songTitle[st] = rows.get(st).getmRadio();
                PlayerTrackInfo trackInfo = new PlayerTrackInfo(songTitle[st],String.valueOf(st),Uri.parse(RadioPath[st]));
                RadioList.add(trackInfo);
                System.out.println(RadioPath[st]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ArrayList<PlayerTrackInfo> RadioList= new ArrayList<>();

    public ArrayList<PlayerTrackInfo> RadioList(){
        RadioPlayListCreation();
        return RadioList;
    }
//
//
//        @Override
//        public void onItemClick (ImageView im, View v, MusicScroll obj,int position){
//            ConnectivityManager cm =
//                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//            try {
//                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//                boolean isConnected = activeNetwork.isConnectedOrConnecting();
//                if (isConnected) {
//                    mCurCheckPosition = position;
//                    Log.i("onItemClick", "received");
//                    Bondiana.putInt("MPData", mCurCheckPosition);
//                    MPState = MP_PLAY;
//                    Bondiana.putInt("MPState", MPState);
//                    intent.putExtra("Bundle", Bondiana);
//                    startService(intent);
//                }
//            } catch (NullPointerException e) {
//                Toast.makeText(this, ERROR, Toast.LENGTH_SHORT).show();
//            }
//        }
//
//        @Override
//        public Loader<String> onCreateLoader ( int id, final Bundle args){
//            return new AsyncTaskLoader<String>(this) {
//
//                    // Override onStartLoading
//                    @Override
//                    protected void onStartLoading() {
//                        // If args is null, return.
//                /* If no arguments were passed, we don't have a query to perform. Simply return. */
//                        if (args == null) {
//                            Log.i("onStartLoading", "nope");
//                            return;
//                        }
//                        Log.i("onStartLoading", "yep");
////                // Show the loading indicator
////                /*
////                 * When we initially begin loading in the background, we want to display the
////                 * loading indicator to the user
////                 */
////                mLoadingIndicator.setVisibility(View.VISIBLE);
//
//                        // COMPLETED (8) Force a load
//                        forceLoad();
//                    }
//
//                    // Override loadInBackground
//                    @Override
//                    public String loadInBackground() {
//                        // Get the String for our URL from the bundle passed to onCreateLoader
//                /* Extract the search query from the args using our constant */
//                        String URI_Second_BASE = args.getString(SEARCH_QUERY_URL_EXTRA);
//                        Log.i("loadInBackground", URI_Second_BASE);
//                        // If the URL is null or empty, return null */
//                        if (URI_Second_BASE == null
//                                || TextUtils.isEmpty(URI_Second_BASE)) {
//                            return null;
//                        }
//
//                        // Copy the try / catch block from the AsyncTask's doInBackground method
//                /* Parse the URL from the passed in String and perform the search */
//                        try {
//                            URL radioSearch = new URL(URI_Second_BASE);
//                            String searchResults = RadioSearch.getResponseFromHttpUrl(radioSearch);
//                            Log.i("searchResults", searchResults);
//                            return searchResults;
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                            Log.i("searchResults", "WTF");
//                            return null;
//                        }
//                    }
//            };
//        }
//
//
//
//        private void startSearch () {
//            URL vkSearchUrl = RadioSearch.buildUrl("");
//
//            // COMPLETED (19) Create a bundle called queryBundle
//            Bundle queryBundle = new Bundle();
//            // COMPLETED (20) Use putString with SEARCH_QUERY_URL_EXTRA as the key and the String value of the URL as the value
//            queryBundle.putString(SEARCH_QUERY_URL_EXTRA, vkSearchUrl.toString());
//
//            LoaderManager loaderManager = getSupportLoaderManager();
//            // COMPLETED (22) Get our Loader by calling getLoader and passing the ID we specified
//            Loader<String> SearchLoader = loaderManager.getLoader(RADIO_SEARCH_LOADER);
//            // COMPLETED (23) If the Loader was null, initialize it. Else, restart it.
//            if (SearchLoader == null) {
//                loaderManager.initLoader(RADIO_SEARCH_LOADER, queryBundle, this);
//            } else {
//                loaderManager.restartLoader(RADIO_SEARCH_LOADER, queryBundle, this);
//            }
//            Log.i("startSearch", vkSearchUrl.toString());
//        }
//        private void find_more_station () {
//            Log.i("find_more_station", "whenever");
//            //        int count = 0;
//        }
//        }
}