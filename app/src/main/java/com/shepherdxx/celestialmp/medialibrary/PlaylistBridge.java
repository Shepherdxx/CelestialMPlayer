package com.shepherdxx.celestialmp.medialibrary;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Shepherdxx on 08.11.2017.
 */

public class PlaylistBridge {
    /**
     * Queries all native playlists and imports them
     *
     * @param context the context to use
     */
    static void importAndroidPlaylists(Context context) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = null;

        try {
            cursor = resolver.query(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Audio.Playlists._ID, MediaStore.Audio.Playlists.NAME}, null, null, null);
        } catch (SecurityException e) {
            Log.v("VanillaMusic", "Unable to query existing playlists, exception: "+e);
        }

        if (cursor != null) {
            while (cursor.moveToNext()) {
                long playlistId = cursor.getLong(0);
                String playlistName = cursor.getString(1);
                importAndroidPlaylist(context, playlistName, playlistId);
            }
            cursor.close();
        }
    }

    /**
     * Imports a single native playlist into our own media library
     *
     * @param context the context to use
     * @param targetName the name of the playlist in our media store
     * @param playlistId the native playlist id to import
     */
    static void importAndroidPlaylist(Context context, String targetName, long playlistId) {
        ArrayList<Long> bulkIds = new ArrayList<>();
        ContentResolver resolver = context.getContentResolver();
        Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlistId);
        Cursor cursor = null;

        try {
            cursor = resolver.query(uri, new String[]{MediaStore.Audio.Media.DATA}, null, null, MediaStore.Audio.Playlists.Members.DEFAULT_SORT_ORDER);
        } catch (SecurityException e) {
            Log.v("VanillaMusic", "Failed to query playlist: "+e);
        }

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String path = cursor.getString(0);
                // We do not need to do a lookup by path as we can calculate the id used
                // by the mediastore using the path
                bulkIds.add(MediaLibrary.hash63(path));
            }
            cursor.close();
        }

        if (bulkIds.size() == 0)
            return; // do not import empty playlists

        long targetPlaylistId = MediaLibrary.createPlaylist(context, targetName);
        if (targetPlaylistId == -1)
            return; // already exists, won't touch

        MediaLibrary.addToPlaylist(context, targetPlaylistId, bulkIds);
    }
}
