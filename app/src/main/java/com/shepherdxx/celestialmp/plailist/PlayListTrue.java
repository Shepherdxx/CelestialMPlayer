package com.shepherdxx.celestialmp.plailist;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.shepherdxx.celestialmp.extras.Constants;
import com.shepherdxx.celestialmp.extras.PopUpToast;

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
                Log.i(logTag, "create " + current.audioTracks.toString());
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
                Log.i(logTag, "create " + current.audioTracks.size());
                break;
            default:
                ArrayList<Long> audioIds= getAudioID(id);
                current=getPlayListInfo(id);
                current.plType = Constants.MP_SD_U;
                current.audioIds = audioIds;
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

    private MyTrackInfo cursorTrack(Cursor cursor,int playlistId){
        MyTrackInfo songSD=null;
        String artist, title, name, album, url;
        long duration,audioId;
        url = cursor.getString(0);
        artist = cursor.getString(1);
        album = cursor.getString(2);
        name = cursor.getString(3);
        title = cursor.getString(4);
        duration = cursor.getLong(5);
        audioId = cursor.getLong(6);
        String[] proj = {url, artist, album, name, title};
        if (duration!=0){
            songSD = new MyTrackInfo(proj,duration,audioId);
            songSD.setPlaylistId(playlistId);}
        return songSD;
    }

    private ArrayList<MyTrackInfo> loadTracks(int playlistId, String cUri, ArrayList<Long> audioIds) {
        ArrayList<MyTrackInfo> rows=new ArrayList<>();
        String table = "TRACK";
        final String[] projection = MyTrackInfo.FILLED_PROJECTION;
        String selection= MediaStore.Audio.Media.IS_MUSIC + "!=0";
        if (audioIds != null) {
            int i;
            for (i = 0; i < audioIds.size(); i++)
                selection = new StringBuilder()
                        .append(selection)
                        .append(" AND ")
                        .append(MediaStore.Audio.Media._ID)
                        .append("= ")
                        .append(audioIds.get(i))
                        .toString();
        }
        String sortBy = MediaStore.Audio.Media.ARTIST;

        QueryTask queryTask=new QueryTask(table, projection, selection, null, sortBy);
        Cursor cursor=queryTask.runMyQuery(mContext);
        if (cursor != null) {
            MyTrackInfo songSD;
            while (cursor.moveToNext()) {
                songSD = cursorTrack(cursor,playlistId);
                if (songSD != null) {File f = new File(songSD.getFileName());
                    if (cUri == null) rows.add(songSD);
                    else
                    if (f.exists() && f.getAbsolutePath().contains(cUri)) {
                        rows.add(songSD);
//                    Log.i(logTag, "MusicScroll " + url + File.pathSeparator + name + File.pathSeparator + album);
                    }
                }
            }cursor.close();
        }
        return rows;
    }

    private ArrayList<Long> getAudioID(int playlistId) {
        ArrayList<Long> ids = new ArrayList<>();
        QueryTask queryTask = QueryTask.queryTask();
        Cursor cursor = queryTask.playlistSummary(mContext, playlistId);
        if (cursor != null) {
            cursor.moveToFirst();
            for(int r= 0; r<cursor.getCount(); r++, cursor.moveToNext()){
                long id=cursor.getLong(0);
                Log.i("getAudioID", "playlistID" + File.pathSeparator + id);
                id=cursor.getLong(2);
                Log.i("getAudioID", "macroPListId" + File.pathSeparator + id);
                ids.add(id);
                id=cursor.getLong(3);
                Log.i("getAudioID", "macroPListId" + File.pathSeparator + id);
            }cursor.close();
        }
        return ids;
    }

    private PlayListInfo getPlayListInfo(int playlistId) {
        String name=null;
        PlayListInfo info=null;
        Cursor cursor = getAndroidPlaylistCursor(mContext, Constants.PLAYLIST_URI);
        if (cursor != null) {
            cursor.moveToFirst();
            for(int r= 0; r<cursor.getCount(); r++, cursor.moveToNext()){
                if (playlistId == cursor.getLong(0))
                    name = cursor.getString(1);

            }cursor.close();
            if (name!=null)info = new PlayListInfo(playlistId, name);
            else new PopUpToast(mContext).setMessage("не возможно загрузить прлейлист");
            Log.i(logTag,"createPlaylist №" + playlistId  + File.pathSeparator + name);
        }
        return info;
    }

    public static Cursor getAndroidPlaylistCursor(Context context,Uri uri) {
        ContentResolver resolver = context.getContentResolver();
        final String id = MediaStore.Audio.Playlists._ID;
        final String name = MediaStore.Audio.Playlists.NAME;
        final String[] projection = { id, name };
        final String selection = null;
        return  resolver.query(uri, projection, selection, null,
                name + " ASC");

    }

//    private void oip(){
//    Cursor cursor = plist.getPlaylistTracks(getActivity(), playlist_id);
//    // replace with your own method to get cursor
//    ArrayList<String> audio_ids = new ArrayList<String>();
//
//    // build up the array with audio_id's
//    int i = 0;
//    if (cursor != null && cursor.moveToFirst()) {
//        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
//            String audio_id = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Playlists.Members.AUDIO_ID));
//            audio_ids.add(audio_id);
//        }}}
//
//    public void addTrackToPlaylist(Context context, String audio_id,
//                                   long playlist_id, int pos) {
//        Uri newuri = MediaStore.Audio.Playlists.Members.getContentUri(
//                "external", playlist_id);
//        ContentResolver resolver = context.getContentResolver();
//        ContentValues values = new ContentValues();
//        values.put(MediaStore.Audio.Playlists.Members.PLAY_ORDER, pos);
//        values.put(MediaStore.Audio.Playlists.Members.AUDIO_ID, audio_id);
//        values.put(MediaStore.Audio.Playlists.Members.PLAYLIST_ID,
//                playlist_id);
//        resolver.insert(newuri, values);
//    }
//
//
////
////
////    public void PlaySongsFromAPlaylist(int playListID){
////
////        String[] ARG_STRING = {MediaStore.Audio.Media._ID,
////                MediaStore.Audio.Media.DATA,
////                MediaStore.Audio.Media.DISPLAY_NAME,
////                MediaStore.Video.Media.SIZE,
////                android.provider.MediaStore.MediaColumns.DATA};
////        Uri membersUri = MediaStore.Audio.Playlists.Members.getContentUri("external", playListID);
////        Cursor songsWithingAPlayList = mThis.managedQuery(membersUri, ARG_STRING, null, null, null);
////        int theSongIDIwantToPlay = 0; // PLAYING FROM THE FIRST SONG
////        if(songsWithingAPlayList != null)
////        {
////            songsWithingAPlayList.moveToPosition(theSongIDIwantToPlay);
////            String DataStream = songsWithingAPlayList.getString(4);
////            PlayMusic(DataStream);
////            songsWithingAPlayList.close();
////        }
////    }
//
////    public static void PlayMusic(String DataStream){
////        MediaPlayer mpObject = new MediaPlayer();
////        if(DataStream == null)
////            return;
////        try {
////            mpObject.setDataSource(DataStream);
////            mpObject.prepare();
////            mpObject.start();
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////    }
}