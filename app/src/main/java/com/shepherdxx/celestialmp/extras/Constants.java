package com.shepherdxx.celestialmp.extras;

import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;

public final class Constants {
    public static final int STATUS_RUNNING = 100500;
    public static final int STATUS_FINISHED = 100501;

    public static final String RECEIVER = "app.result.receiver";
    public static final String RECEIVER_DATA = RECEIVER + ".data";

    public static final String MP_PREPARE_RADIO ="app.broadCast.MP_PREPARE_RADIO";
    public static final String MP_PREPARE       ="app.broadCast.MP_PREPARE";
    public static final String MP_STARTED       ="app.broadCast.MP_STARTED";
    public static final String MP_STOPED        ="app.broadCast.MP_STOPED";
    public static final String MP_ERROR         ="app.broadCast.MP_ERROR";

    public static final int SERVICE_DELAY = 6000;

    public final static int MP_PAUSE    = 0;
    public final static int MP_PLAY     = 1;
    public final static int MP_HTTP     =10;
    public final static int MP_RADIO    =11;
    public final static int MP_SD       =12;
    public final static int MP_RAW      =13;
    public final static int MP_SD_U     =14;
    public final static int MP_EMPTY    =-0xA;
    public final static int SERVICE_START =14;

    public final static int PLAYLIST_All_Audio  = -1;
    public final static int PLAYLIST_Cache      = -2;
    public final static int PLAYLIST_RADIO      = -3;
    public static final String _PLAYLIST_RADIO ="Radio playlist";
    public static final String _PLAYLIST_All_Audio ="All Audio";
    public static final String _PLAYLIST_Cache ="Cache";



    public static final String PLAYLIST_ID = MediaStore.Audio.Playlists.Members._ID;
    public static final String AUDIO_ID = MediaStore.Audio.Playlists.Members.AUDIO_ID;


    public final static int YES=1;
    public final static int NO=0;

    public static final String CONTENT_AUTHORITY = "com.example.android.mpcelestial";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_PETS = "pets";
    public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PETS);

    public static final File MyCachePath = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_MUSIC + File.separator+"MyCache"+ File.separator);

    public static final File MusicPath= Environment.getExternalStoragePublicDirectory(
    Environment.DIRECTORY_MUSIC+ File.separator);



    public static final String DEFAULT_R_M = "Not repeated";


    public final static int FILE_SIZE_ASC=0;
    public final static int FILE_SIZE_DES=1;
    public final static int FILE_NAME_ASC=2;
    public final static int FILE_NAME_DES=3;
    public final static int FILE_L_M_ASC=4;
    public final static int FILE_L_M__DES=5;
    public static final Uri
            PLAYLIST_URI = MediaStore
                    .Audio
                    .Playlists
                    .EXTERNAL_CONTENT_URI;


    public static String BUNDLE = "Bundle";
    public static String iERROR = "Check your connection";

    public static String URI_RADIO_BASE = "http://ic3.101.ru:8000/v";
    public static String URI_RADIO_AFTER_BASE = "_1";
}
