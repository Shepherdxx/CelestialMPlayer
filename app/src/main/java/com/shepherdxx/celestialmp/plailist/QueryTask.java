package com.shepherdxx.celestialmp.plailist;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.shepherdxx.celestialmp.extras.Constants;
import com.shepherdxx.celestialmp.medialibrary.MediaLibrary;

/**
 * Created by Shepherdxx on 08.11.2017.
 */

public class QueryTask {
    public final String table;
    public final String[] projection;
    public final String selection;
    public final String[] selectionArgs;
    public String sortOrder;

    /**
     * Used for {@lin k SongTimeline#addSongs(android.content.Context, QueryTask)}.
     * One of SongTimeline.MODE_*.
     */
    public int mode;

    /**
     * Type of the group being query. One of MediaUtils.TYPE_*.
     */
    public int type;

    /**
     * Data. Required value depends on value of mode. See individual mode
     * documentation for details.
     */
    public long data;

    /**
     * Create the tasks. All arguments are passed directly to
     * MediaLibrary.runQuery().
     */
    public QueryTask(String table, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        this.table = table;
        this.projection = projection;
        this.selection = selection;
        this.selectionArgs = selectionArgs;
        this.sortOrder = sortOrder;
    }

    /**
     * Run the query. Should be called on a background thread.
     *
     * @param context The Context to use
     */
    public Cursor runQuery(Context context) {
        return MediaLibrary.queryLibrary(context, table, projection, selection, selectionArgs, sortOrder);
    }

    public Cursor runMyQuery(Context context) {
        Log.i("runMyQuery ", table);
        Cursor cursor = context.getContentResolver().query(
                SD_check(),
                projection,
                selection,
                selectionArgs,
                sortOrder
        );
        return cursor;
    }

    private Uri SD_check(){
       return MediaStore.Audio.Media.
               getContentUriForPath(Environment.getExternalStorageDirectory().getPath());
//        if (Environment.getExternalStorageDirectory().exists())
//            return MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//        return  MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
    }

    private Uri pl_check(){
        if (Environment.getExternalStorageDirectory().exists())
            return MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
        return  MediaStore.Audio.Playlists.INTERNAL_CONTENT_URI;
    }

    public static QueryTask queryTask(){
        return new QueryTask(null,null,null,null,null);}
    public Cursor playlistSummary(Context context, int playlistId) {
        final String[] projection = { "*" };
        String selection= Constants.PLAYLIST_ID+"=" + playlistId;
        Uri uri = pl_check();
        return context.getContentResolver().query(
                uri,
                projection,
                selection,
                null,
                null);
    }
}
