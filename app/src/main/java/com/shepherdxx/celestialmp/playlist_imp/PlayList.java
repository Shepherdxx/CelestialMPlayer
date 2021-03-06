package com.shepherdxx.celestialmp.playlist_imp;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import com.shepherdxx.celestialmp.R;
import com.shepherdxx.celestialmp.medialibrary.MediaLibrary;
import com.shepherdxx.celestialmp.plailist.QueryTask;
import com.shepherdxx.celestialmp.plailist.Song;

import java.util.ArrayList;

import static com.shepherdxx.celestialmp.extras.Constants.MP_EMPTY;

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
     * @return The id of the playlist, or {@link MP_EMPTY) if there is no playlist with the
     * given name.
     */
    public static long getPlaylist(Context context, String name)
    {
        long id = MP_EMPTY;
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
        if (id != MP_EMPTY)
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
        if (playlistId == MP_EMPTY)
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
        if (playlistId == MP_EMPTY)
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
     * @return the id of the playlist, MP_EMPTY on error
     */
    public static long getFavoritesId(Context context, boolean create) {
        String playlistName = context.getString(R.string.playlist_favorites);
        long playlistId = getPlaylist(context, playlistName);

        if (playlistId == MP_EMPTY && create == true)
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
        if (playlistId == MP_EMPTY || song == null)
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




}
