package com.shepherdxx.celestialmp.vkmusicextras;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.shepherdxx.celestialmp.extras.AsyncResponse;

import java.io.File;

import static com.shepherdxx.celestialmp.extras.Constants.MyCachePath;
import static com.shepherdxx.celestialmp.extras.Constants.NO;
import static com.shepherdxx.celestialmp.extras.Constants.YES;


/**
 * Created by Shepherdxx on 17.09.2017.
 */

public class SingleMediaScanner
    implements MediaScannerConnection.MediaScannerConnectionClient
{

    private MediaScannerConnection mMs;
    private File mFile;
    Song sdata;
    String Name;
    int rename;
    public AsyncResponse response = null;

    Context context;


    public SingleMediaScanner(Context context, Song sdata, int rename) {
        this.context=context;
        mMs = new MediaScannerConnection(context, this);
        mMs.connect();
        this.sdata=sdata;
        this.rename=rename;
        mFile = new File(sdata.getPath());
}
    public SingleMediaScanner(Context context, String path) {
        this.rename=NO;
        this.context=context;
        mMs = new MediaScannerConnection(context, this);
        mMs.connect();
        mFile = new File(path);
    }

    @Override
    public void onMediaScannerConnected() {
        mMs.scanFile(mFile.getAbsolutePath(), null);
        Log.i("onMediaScannerConnected", mFile.getAbsolutePath() + File.pathSeparator);
    }

    @Override
    public void onScanCompleted(String path, Uri uri) {
    if (rename==YES){
        find(sdata);
        rename(sdata, Name);
        response.processFinish(Name);}

        mMs.disconnect();
    }

    private void find(Song sdata) {
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA};
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        Cursor cursor = context.
                getContentResolver().query(
                uri,
                projection,
                selection,
                null,
                null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                Log.i("cursor", "cursor1");
                String name, url;
                do {
                    url = cursor.
                            getString((cursor.getColumnIndex(
                                    MediaStore.Audio.Media.DATA)));
//                Log.i("cursor",url+File.pathSeparator +sdata.getPath());
                    name = cursor.
                            getString((cursor.getColumnIndex(
                                    MediaStore.Audio.Media.TITLE)));

                    if (url.matches(sdata.getPath()) && name != null) {
                        Name = name;
                    }
//                    Log.i("cursor", url + File.pathSeparator + Name);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

    }

    private void rename(Song sdata, String name) {
        Log.i("rename", sdata.getPath() + File.pathSeparator + name);
        //здесь указываем абсолютный путь к файлу
        File file = new File(sdata.getPath());
        if (name==null) name=file.getName();
        File newFile = new File(MyCachePath + File.separator + name + ".mp3");
        if (file.renameTo(newFile)) {
            System.out.println("Файл переименован успешно");
        } else {
            System.out.println("Файл не был переименован");
        }
    }
}
