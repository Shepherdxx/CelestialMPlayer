package com.shepherdxx.celestialmp.extras;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.shepherdxx.celestialmp.R;

import java.io.File;

/**
 * Created by Shepherdxx on 06.12.2017.
 */

public class Debug extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.debug_view);
        String finalString =cusorText_1()+"\n"+check()+"\n"
                + cusorText_2()+"\n"+cusorText_3()
                +"\n"+cusorText_4()+"\n"+cusorText_5();
        textSet(finalString);
    }

    String check() {
        if (Environment.getExternalStorageState() == null)
            return "getExternalStorageState == null";
            return Environment.getDownloadCacheDirectory().toString();
    }

    void textSet(String data){
        TextView textView=findViewById(R.id.debug_tv);
        textView.setText(data);
    }

    private String cusorText_1(){
        StringBuilder data = new StringBuilder();
        try {
            Uri uri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
            final String[] projection = {"*"};
            Cursor cursor = getContentResolver().query(
                    uri,
                    projection,
                    null,
                    null,
                    null
            );
            if (cursor != null) {
                cursor.moveToFirst();
                for (int r = 0; r < cursor.getCount()-1; r++, cursor.moveToNext()) {
                    String id= cursor.getColumnName(r);
                    data.append(id).append("  ");
                }
                cursor.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            data.append("cusorText_1  error");
            return data.toString();
        }
        return data.toString();
    }

    private String cusorText_2(){
        StringBuilder data = new StringBuilder();
        try {
            Uri uri = MediaStore.Audio.Playlists.INTERNAL_CONTENT_URI;
            final String[] projection = {"*"};
            Cursor cursor = getContentResolver().query(
                    uri,
                    projection,
                    null,
                    null,
                    null
            );
            if (cursor != null) {
                cursor.moveToFirst();
                for (int r = 0; r < cursor.getCount()-1; r++, cursor.moveToNext()) {
                    String id= cursor.getColumnName(r);
                    data.append(id).append("  ");
                }
                cursor.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            data.append("cusorText_2  error");
            return data.toString();
        }
        return data.toString();
    }

    private String cusorText_3(){
        StringBuilder data = new StringBuilder();
        try {
            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            final String[] projection = {"*"};
            Cursor cursor = getContentResolver().query(
                    uri,
                    projection,
                    null,
                    null,
                    null
            );
            if (cursor != null) {
                cursor.moveToFirst();
                for (int r = 0; r < cursor.getCount()-1; r++, cursor.moveToNext()) {
                    String id= cursor.getColumnName(r);
                    data.append(id).append("  ");
                }
                cursor.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            data.append("cusorText_3  error");
            return data.toString();
        }
        return data.toString();
    }

    private String cusorText_4(){
        StringBuilder data = new StringBuilder();
        try {
            Uri uri = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
            final String[] projection = {"*"};
            Cursor cursor = getContentResolver().query(
                    uri,
                    projection,
                    null,
                    null,
                    null
            );
            if (cursor != null) {
                cursor.moveToFirst();
                for (int r = 0; r < cursor.getCount()-1; r++, cursor.moveToNext()) {
                    String id = cursor.getColumnName(r);
                    data.append(id).append("  ");
                }
                cursor.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            data.append("cusorText_4  error");
            return data.toString();
        }
        return data.toString();
    }

    private String cusorText_5(){
        StringBuilder data = new StringBuilder();
        try {
            Uri uri = MediaStore.Audio.Media.getContentUriForPath(Environment.getDataDirectory().getPath());
            final String[] projection = {"*"};
            Cursor cursor = getContentResolver().query(
                    uri,
                    projection,
                    null,
                    null,
                    null
            );
            if (cursor != null) {
                cursor.moveToFirst();
                for (int r = 0; r < cursor.getCount()-1; r++, cursor.moveToNext()) {
                    String id= cursor.getColumnName(r);
                    data.append(id).append("  ");
                }
                cursor.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            data.append("cusorText_5  error");
            return data.toString();
        }
        return data.toString();
    }

}
