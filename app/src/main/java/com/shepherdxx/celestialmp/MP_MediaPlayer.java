package com.shepherdxx.celestialmp;

import android.media.MediaPlayer;
import android.util.Log;

import static com.shepherdxx.celestialmp.MP_BackgroundService.mPlayer;

/**
 * Created by Shepherdxx on 16.07.2017.
 */

public class MP_MediaPlayer
        extends MediaPlayer
{


    public static String LOG_TAG = MP_MediaPlayer.class.getSimpleName();
    private String songName,albumName,artistName;

    private int position;

    private boolean Request=true;

    private int MP_Type;


    public boolean getRequest() {
        boolean answer=Request;
        Request=false;
//        System.out.println(answer);
        return answer;
    }

    public void setRequest(boolean request) {
        Request = request;
    }

    void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        System.out.println(LOG_TAG + "Смотреть тут позиция" + String.valueOf(position));
        return position;
    }

    void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSongName() {
        System.out.println(LOG_TAG + "Смотреть тут"+songName);
        return songName;
    }

    public String getAlbumName() {
        System.out.println(LOG_TAG + "Смотреть тут"+albumName);
        return albumName;
    }

    public String getArtistName() {
        System.out.println(LOG_TAG + "Смотреть тут"+albumName);
        return artistName;
    }

    public int getMP_Type() {
        Log.i(LOG_TAG,"getMP_Type" + String.valueOf(MP_Type));
        return MP_Type;
    }

    public void setMP_Type(int MP_Type) {
        this.MP_Type = MP_Type;
    }


    public int getState() {
        int state=0;
        if (mPlayer.isPlaying())state=1;
        return state;
    }

    boolean isOnAir(){
        if (mPlayer!=null){
            if (mPlayer.isPlaying()) return true;
        else return false;}
        return false;
    };


}
