package com.shepherdxx.celestialmp.plailist;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.shepherdxx.celestialmp.extras.Constants;

import java.io.File;
import java.util.ArrayList;

import static com.shepherdxx.celestialmp.extras.Constants.MyCachePath;

/**
 * Created by Shepherdxx on 28.11.2017.
 */

public class PlayListTrue {

    Context mContext;
    private String logTag=PlayListTrue.class.getSimpleName();

    public PlayListTrue(){}
    public PlayListTrue(Context context)    {mContext = context;}

    public PlayListInfo createPlaylist(int id){
        PlayListInfo current;
        switch (id){
            case Constants.PLAYLIST_RADIO:
                current=PlayListInfo.Radio();
                current.plType = Constants.MP_RADIO;
                current.audioTracks=plCreate(id);
                Log.i("PlayListTrue create", current.audioTracks.toString());
                break;
            case Constants.PLAYLIST_All_Audio:
                current=PlayListInfo.All();
                current.plType = Constants.MP_SD_U;
                current.audioTracks=plCreate(id);
                break;
            case Constants.PLAYLIST_Cache:
                current=PlayListInfo.Cache();
                current.plType = Constants.MP_SD_U;
                current.audioTracks=plCreate(id);
                Log.i("PlayListTrue create", " " + current.audioTracks.size());
                break;
            default:
                current=null;
        }
        return current;
    }




    //Создаем плейлист
    private ArrayList<TrackInfo> plCreate(int id) {
        Log.i("plCreate", "плейлист подготовка");
        switch (id) {
            case Constants.PLAYLIST_RADIO:
                return new RadioBD().RadioList();
            case Constants.PLAYLIST_Cache:
                return loadTracks(id, MyCachePath.getAbsolutePath());
            case Constants.PLAYLIST_All_Audio:
                return loadTracks(id,null);
            default:
                return null;
        }
    }

    private ArrayList<TrackInfo> loadTracks(int id, String cUri) {
        ArrayList<TrackInfo> rows=new ArrayList<>();
        TrackInfo songSD;
        Uri uri = SD_check();
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";
        String sortBy = MediaStore.Audio.Media.ARTIST;
        Log.i(logTag, "SD_check() " +uri.toString());
        Cursor cursor = mContext.getContentResolver().query(uri, null, selection, null, sortBy);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String artist, name, album, url;
                long duration;
                do {
                    album = cursor.
                            getString((cursor.getColumnIndex(
                                    MediaStore.Audio.Media.ALBUM)));
                    artist = cursor.
                            getString((cursor.getColumnIndex(
                                    MediaStore.Audio.Media.ARTIST)));
                    url = cursor.
                            getString((cursor.getColumnIndex(
                                    MediaStore.Audio.Media.DATA)));
                    File f = new File(url);

                    name = cursor.
                            getString((cursor.getColumnIndex(
                                    MediaStore.Audio.Media.TITLE)));

                    if (name == null && cursor.
                            getString((cursor.getColumnIndex(
                                    MediaStore.Audio.Media.DISPLAY_NAME))) != null) {
                        name = cursor.
                                getString((cursor.getColumnIndex(
                                        MediaStore.Audio.Media.DISPLAY_NAME))).
                                replace(".mp3", "").replace("_", " ").replace(artist, "").replace(" - ", "");
                    }

                    duration = cursor.
                            getLong((cursor.getColumnIndex(
                                    MediaStore.Audio.Media.DURATION)));


                    if (cUri==null){
                        songSD = new TrackInfo(url, name, artist, album, duration);
                        songSD.setPlaylistId(id);
                        rows.add(songSD);
                    Log.i(logTag, "MusicScroll " + url + File.pathSeparator + name + File.pathSeparator + album);}
                    else if (f.exists() && f.getAbsolutePath().contains(cUri)) {
                        songSD = new TrackInfo(url, name, artist, album, duration);
                        songSD.setPlaylistId(id);
                        rows.add(songSD);
                        Log.i(logTag, "MusicScroll " + url + File.pathSeparator + name + File.pathSeparator + album);
                    }

                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        Log.i(logTag,"rows "+ rows.size());
        return rows;
    }

    private Uri SD_check(){
        if (Environment.getExternalStorageDirectory().exists())
            return MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        return  MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
    }
}