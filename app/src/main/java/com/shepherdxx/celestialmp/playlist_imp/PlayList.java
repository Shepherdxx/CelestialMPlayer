package com.shepherdxx.celestialmp.playlist_imp;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import com.shepherdxx.celestialmp.R;
import com.shepherdxx.celestialmp.medialibrary.MediaLibrary;
import com.shepherdxx.celestialmp.plailist.Song;

import java.util.ArrayList;

/**
 * Created by Shepherdxx on 02.11.2017.
 */

public class PlayList {

    /**
     * Queries all the playlists known to the MediaLibrary.
     *
     * @param context the context to use
     * @return The queried cursor.
     */
    public static Cursor queryPlaylists(Context context) {
        final String[] projection = { MediaLibrary.PlaylistColumns._ID, MediaLibrary.PlaylistColumns.NAME };
        final String sort = MediaLibrary.PlaylistColumns.NAME;
        return MediaLibrary.queryLibrary(context, MediaLibrary.TABLE_PLAYLISTS, projection, null, null, sort);
    }

    /**
     * Retrieves the id for a playlist with the given name.
     *
     * @param context the context to use
     * @param name The name of the playlist.
     * @return The id of the playlist, or -1 if there is no playlist with the
     * given name.
     */
    public static long getPlaylist(Context context, String name)
    {
        long id = -1;
        final String[] projection = { MediaLibrary.PlaylistColumns._ID };
        final String selection = MediaLibrary.PlaylistColumns.NAME+"=?";
        final String[] selectionArgs = { name };
        Cursor cursor = MediaLibrary.queryLibrary(context, MediaLibrary.TABLE_PLAYLISTS, projection, selection, selectionArgs, null);

        if (cursor != null) {
            if (cursor.moveToNext())
                id = cursor.getLong(0);
            cursor.close();
        }

        return id;
    }

    /**
     * Create a new playlist with the given name. If a playlist with the given
     * name already exists, it will be overwritten.
     *
     * @param context the context to use
     * @param name The name of the playlist.
     * @return The id of the new playlist.
     */
    public static long createPlaylist(Context context, String name)
    {
        long id = getPlaylist(context, name);
        if (id != -1)
            deletePlaylist(context, id);

        id = MediaLibrary.createPlaylist(context, name);
        return id;
    }

    /**
     * Run the given query and add the results to the given playlist. Should be
     * run on a background thread.
     *
     * @param context the context to use
     * @param playlistId The playlist id of the playlist to
     * modify.
     * @param query The query to run. The audio id should be the first column.
     * @return The number of songs that were added to the playlist.
     */
    public static int addToPlaylist(Context context, long playlistId, QueryTask query) {
        ArrayList<Long> result = new ArrayList<Long>();
        Cursor cursor = query.runQuery(context);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                result.add(cursor.getLong(0));
            }
        }
        return addToPlaylist(context, playlistId, result);
    }

    /**
     * Adds a set of audioIds to the given playlist. Should be
     * run on a background thread.
     *
     * @param context the context to use
     * @param playlistId The playlist id of the playlist to
     * modify.
     * @param audioIds An ArrayList with all IDs to add
     * @return The number of songs that were added to the playlist.
     */
    public static int addToPlaylist(Context context, long playlistId, ArrayList<Long> audioIds) {
        if (playlistId == -1)
            return 0;
        return MediaLibrary.addToPlaylist(context, playlistId, audioIds);
    }

    /**
     * Removes a set of audioIds from the given playlist. Should be
     * run on a background thread.
     *
     * @param context the context to use
     * @param playlistId id of the playlist to
     * modify.
     * @param audioIds An ArrayList with all IDs to drop
     * @return The number of songs that were removed from the playlist
     */
    public static int removeFromPlaylist(Context context, long playlistId, ArrayList<Long> audioIds) {
        if (playlistId == -1)
            return 0;

        String idList = TextUtils.join(", ", audioIds);
        String selection = MediaLibrary.PlaylistSongColumns.SONG_ID+" IN ("+idList+") AND "+MediaLibrary.PlaylistSongColumns.PLAYLIST_ID+"="+playlistId;
        return MediaLibrary.removeFromPlaylist(context, selection, null);
    }

    /**
     * Delete the playlist with the given id.
     *
     * @param context the context to use
     * @param id the id of the playlist.
     */
    public static void deletePlaylist(Context context, long id) {
        MediaLibrary.removePlaylist(context, id);
    }


    /**
     * Rename the playlist with the given id.
     *
     * @param context the context to use
     * @param id The Media.Audio.Playlists id of the playlist.
     * @param newName The new name for the playlist.
     */
    public static void renamePlaylist(Context context, long id, String newName) {
        MediaLibrary.renamePlaylist(context, id, newName);
    }

    /**
     * Returns the ID of the 'favorites' playlist.
     *
     * @param context The Context to use
     * @param create Create the playlist if it does not exist
     * @return the id of the playlist, -1 on error
     */
    public static long getFavoritesId(Context context, boolean create) {
        String playlistName = context.getString(R.string.playlist_favorites);
        long playlistId = getPlaylist(context, playlistName);

        if (playlistId == -1 && create == true)
            playlistId = createPlaylist(context, playlistName);

        return playlistId;
    }

    /**
     * Searches for given song in given playlist
     *
     * @param context the context to use
     * @param playlistId The ID of the Playlist to query
     * @param song The Song to search in given playlistId
     * @return true if `song' was found in `playlistId'
     */
    public static boolean isInPlaylist(Context context, long playlistId, Song song) {
        if (playlistId == -1 || song == null)
            return false;

        boolean found = false;
        String selection = MediaLibrary.PlaylistSongColumns.PLAYLIST_ID+"=? AND "+MediaLibrary.PlaylistSongColumns.SONG_ID+"=?";
        String[] selectionArgs = { Long.toString(playlistId), Long.toString(song.id) };

        Cursor cursor = MediaLibrary.queryLibrary(context, MediaLibrary.TABLE_PLAYLISTS_SONGS, Song.EMPTY_PLAYLIST_PROJECTION, selection, selectionArgs, null);
        if (cursor != null) {
            found = cursor.getCount() != 0;
            cursor.close();
        }
        return found;
    }

    //////
    /////  //    //   //////
    //      //  //    //   //
    ////      //      //////
    //      //  //    //
    ////  //      //  //


//    Context mBase;
//    Uri uri=MediaStore.Audio.Playlists.getContentUri("playlist");
//
//    public void addnewPlaylist(String newplaylist) {
//        ContentResolver resolver = mBase.getContentResolver();
//        ContentValues values = new ContentValues(1);
//        values.put(MediaStore.Audio.Playlists.NAME, newplaylist);
//        resolver.insert(uri, values);
//    }
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
//    public Cursor getandroidPlaylistcursor(Context context) {
//        ContentResolver resolver = context.getContentResolver();
//        final String id = MediaStore.Audio.Playlists._ID;
//        final String name = MediaStore.Audio.Playlists.NAME;
//        final String[] columns = { id, name };
//        final String criteria = null;
//        return  resolver.query(uri, columns, criteria, null,
//                name + " ASC");
//
//    }
//
////    private void oip(){
////    Cursor cursor = plist.getPlaylistTracks(getActivity(), playlist_id);
////    // replace with your own method to get cursor
////    ArrayList<String> audio_ids = new ArrayList<String>();
////
////    // build up the array with audio_id's
////    int i = 0;
////    if (cursor != null && cursor.moveToFirst()) {
////        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
////            String audio_id = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Playlists.Members.AUDIO_ID));
////            audio_ids.add(audio_id);
////        }}}
////
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
