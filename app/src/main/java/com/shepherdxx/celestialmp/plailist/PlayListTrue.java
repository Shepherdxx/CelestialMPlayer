package com.shepherdxx.celestialmp.plailist;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import com.shepherdxx.celestialmp.extras.Constants;
import com.shepherdxx.celestialmp.playlist_imp.QueryTask;

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
        return createPlaylist(id,null);
    }
    public PlayListInfo createPlaylist(int id, String name){
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
                current=new PlayListInfo(id,name);
                current.plType = Constants.MP_SD_U;
                current.audioIds= getAudoiID(id);
                ArrayList<Long> audioIds = current.audioIds;
                current.audioTracks=plCreate(id,audioIds);
                break;
        }
        return current;
    }

    private ArrayList<MyTrackInfo> plCreate(int id) {
        return plCreate(id, null);
    }

    //Создаем плейлист
    private ArrayList<MyTrackInfo> plCreate(int id,ArrayList<Long> audioIds) {
        ArrayList<MyTrackInfo> cur;
        Log.i("plCreate", "плейлист подготовка");
        switch (id) {
            case Constants.PLAYLIST_RADIO:
                cur = new RadioBD().RadioList();
                break;
            case Constants.PLAYLIST_Cache:
                cur = loadTracks(id, MyCachePath.getAbsolutePath(),audioIds);
                break;
            default:
                cur = loadTracks(id,null,audioIds);
                break;
        }
        if (cur!=null)Log.i("PlayListTrue create", " " + cur.size());
        return cur;
    }

    private ArrayList<MyTrackInfo> loadTracks(int id, String cUri, ArrayList<Long> audioIds) {
        ArrayList<MyTrackInfo> rows=new ArrayList<>();
        String table = "TRACK";


        final String[] projection = {
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION
        };
        String selection= MediaStore.Audio.Media.IS_MUSIC + "!=0";
        if (audioIds!=null) selection = MediaStore.Audio.Media._ID;
        String sortBy = MediaStore.Audio.Media.ARTIST;

        QueryTask queryTask=new QueryTask(table, projection, selection, null, sortBy);
        Cursor cursor=queryTask.runMyQuery(mContext);
        if (cursor != null) {
            MyTrackInfo songSD;
            String artist, name, album, url;
            long duration;

            while (cursor.moveToNext()) {
                album = cursor.getString(3);
                artist = cursor.getString(4);
                url = cursor.getString(0);
                name = cursor.getString(1);
                duration = cursor.getLong(5);

                if (name == null && cursor.getString(2) != null) {
                    name = cursor.getString(2).
                            replace(".mp3", "")
                            .replace("_", " ")
                            .replace(artist, "")
                            .replace(" - ", "");
                }

                File f = new File(url);
                if (duration!=0)
                if (cUri == null) {
                    songSD = new MyTrackInfo(url, name, artist, album, duration);
                    songSD.setPlaylistId(id);
                    rows.add(songSD);
                    Log.i(logTag, "MusicScroll " + url + File.pathSeparator + name + File.pathSeparator + album);
                } else if (f.exists() && f.getAbsolutePath().contains(cUri)) {
                    songSD = new MyTrackInfo(url, name, artist, album, duration);
                    songSD.setPlaylistId(id);
                    rows.add(songSD);
                    Log.i(logTag, "MusicScroll " + url + File.pathSeparator + name + File.pathSeparator + album);
                }
            }cursor.close();
        }
        return rows;
    }

    private ArrayList<Long> getAudoiID(int playlistId) {
        ArrayList<Long> ids = new ArrayList<>();
        QueryTask queryTask = QueryTask.queryTask();
        Cursor cursor = queryTask.audioId(mContext, playlistId);
        if (cursor != null) {
            cursor.moveToFirst();
            for(int r= 0; r<cursor.getCount(); r++, cursor.moveToNext()){
                long id=cursor.getLong(0);
                ids.add(id);
                Log.i("getAudoiID", "AudoiID" + File.pathSeparator + id);
            }cursor.close();
        }
        return ids;
    }
}