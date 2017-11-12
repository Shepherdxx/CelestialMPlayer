package com.shepherdxx.celestialmp.vkmusicextras;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.shepherdxx.celestialmp.extras.AsyncResponse;

import java.io.File;

import static android.os.Environment.getExternalStorageDirectory;

/**
 * Created by Shepherdxx on 16.09.2017.
 */

public class VkCacheFolder extends AsyncTask<Void, Void, Void> {

    //Переменные

    String[] vkpath = {
            "Android",
            "data",
            "com.vkontakte.android",
            "cache",
            "PlayerProxy"};

    public AsyncResponse delegate = null;
    String Folder;
    File SDFolder;
    Context context;
    File meta;
    File sdata;
    ProgressDialog dialog;
    private int count = 0;
    private int length = vkpath.length - 1;

    //
    public VkCacheFolder(Context context) {
        this.context = context;
        dialog = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {
        dialog.setMessage("Поиск ...");
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        findVkCacheFolder(sdata);
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
        delegate.processFinish(meta);
    }

    public void SD_check() {
        String sdState = android.os.Environment.getExternalStorageState();
        //Получаем состояние SD карты (подключена она или нет) - возвращается true и false соответственно
        if (sdState.equals(android.os.Environment.MEDIA_MOUNTED)) {
            // если true
            Folder = getExternalStorageDirectory().toString();
            SDFolder = getExternalStorageDirectory();
        } else {
            Folder = Environment.getExternalStorageDirectory().getPath();
            SDFolder = Environment.getExternalStorageDirectory();
            Toast.makeText(context, "SD карты нет или не доступна", Toast.LENGTH_SHORT).show();
        }
        this.sdata = SDFolder;
    }

    private String match(String m) {
        if (m.matches(vkpath[count])) {
            count = (count == length ? count : count + 1);
            return m;
        }
        return null;
    }

    private void findVkCacheFolder(File dir) {
        for (File file : dir.listFiles()) {
            if (match(file.getName()) != null) {
                meta = file;
                Log.i("findVkCacheFolder", meta.toString());
                findVkCacheFolder(file);
            }
        }
    }
}

