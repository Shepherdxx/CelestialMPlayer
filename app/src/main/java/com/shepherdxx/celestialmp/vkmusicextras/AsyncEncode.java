package com.shepherdxx.celestialmp.vkmusicextras;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.shepherdxx.celestialmp.extras.AsyncResponse;

import java.io.File;

import static com.shepherdxx.celestialmp.extras.Constants.MyCachePath;


/**
 * Created by Shepherdxx on 16.09.2017.
 */

public class AsyncEncode
        extends AsyncTask<Void, Void, Void>
{

    Song sdata;
    ProgressDialog dialog;
    Context context;
    public AsyncResponse response = null;


    public AsyncEncode(Song data, Context context) {
        this.sdata = data;
        dialog = new ProgressDialog(context);
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        dialog.setMessage("Перекодирование ...");
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        //Создание папки для перекодированных файлов в случае её отстутсвия.
        if (!MyCachePath.exists()) {MyCachePath.mkdirs();}

        String filename = sdata.getPath();
        String file = sdata.getFilename();
        String mp3Name = MyCachePath + File.separator + file + ".mp3";

        VKMusicCacheEncoder m_Encoder = new VKMusicCacheEncoder(filename, mp3Name);
        m_Encoder.processBytes();
        Log.i("doInBackground", mp3Name);
        sdata.setPath(mp3Name);

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
        response.encodingFinish(sdata);
    }

}
