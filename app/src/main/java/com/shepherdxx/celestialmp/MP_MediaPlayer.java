package com.shepherdxx.celestialmp;

import android.media.MediaPlayer;
import android.util.Log;

/**
 * Created by Shepherdxx on 16.07.2017.
 */

public class MP_MediaPlayer
        extends MediaPlayer
{


    public static String LOG_TAG = MP_MediaPlayer.class.getSimpleName();
    private int position;
    private int MP_Type;

    public MP_MediaPlayer(){}

    public static MP_MediaPlayer newPlayer(){
        return new MP_MediaPlayer();}

    void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        System.out.println(LOG_TAG + "Смотреть тут позиция" + String.valueOf(position));
        return position;
    }

    public int getMP_Type() {
        Log.i(LOG_TAG,"getMP_Type" + String.valueOf(MP_Type));
        return MP_Type;
    }

    public void setMP_Type(int MP_Type) {
        this.MP_Type = MP_Type;
    }




}
