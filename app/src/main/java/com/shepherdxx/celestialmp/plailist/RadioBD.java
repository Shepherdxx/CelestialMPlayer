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

import com.shepherdxx.celestialmp.extras.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import static com.shepherdxx.celestialmp.extras.Constants.URI_RADIO_BASE;
import static com.shepherdxx.celestialmp.extras.Constants.URI_RADIO_AFTER_BASE;

/**
 * Created by Shepherdxx on 03.11.2017.
 */

public class RadioBD {

    public RadioBD(){}

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


    int index;
    private String mUri;
    private Activity mActivity;
    private ArrayList<MyTrackInfo> rows=new ArrayList<>();
    String[] adi = {"http://us3.internet-radio.com:8007/",
            "http://air2.radiorecord.ru:805/rock_320",
            "http://84.22.142.130:8000/arstream?4&28",
            "http://81.88.36.42:8010/"};

    //создание плейлиста
    private void RadioPlayListCreation() {
        if (rows.isEmpty()) {
            for (index = 1; index <= 5; index++) {
                mUri = URI_RADIO_BASE + String.valueOf(index) + URI_RADIO_AFTER_BASE;
                Log.i("Radio ", String.valueOf(index) + " " + String.valueOf(mUri));
                rows.add(
                        song("Radio " + String.valueOf(index),
                                mUri,
                                mUri,
                                Constants.PLAYLIST_RADIO)
                );
            }
            addMoreTo(rows);
            RadioList=rows;
            Log.i("Radio", "плейлист создан");
        } else Log.i("Radio", "плейлист уже есть");
    }

    private void addMoreTo(ArrayList<MyTrackInfo> PTI){
        for (int i = 0; i < adi.length; i++) {
            mUri = adi[i];
            Log.i("Radio ", String.valueOf(index+i) + " " + String.valueOf(mUri));
            PTI.add(
                    song("Radio " + String.valueOf(index+i),
                            mUri,
                            mUri,
                            Constants.PLAYLIST_RADIO));
        }
    }

    private MyTrackInfo song(String mR, String mD, String mU , int id){
        MyTrackInfo song = new MyTrackInfo(mR,
                mD,
                mU);
        song.setPlaylistId(id);
        return song;
    }

    ArrayList<MyTrackInfo> RadioList= new ArrayList<>();

    public ArrayList<MyTrackInfo> RadioList(){
        RadioPlayListCreation();
        return RadioList;
    }

}