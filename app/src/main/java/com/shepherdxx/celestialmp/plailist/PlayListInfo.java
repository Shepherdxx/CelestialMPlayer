package com.shepherdxx.celestialmp.plailist;

import com.shepherdxx.celestialmp.extras.Constants;

import java.util.ArrayList;

import static com.shepherdxx.celestialmp.extras.Constants._PLAYLIST_All_Audio;
import static com.shepherdxx.celestialmp.extras.Constants._PLAYLIST_Cache;
import static com.shepherdxx.celestialmp.extras.Constants._PLAYLIST_RADIO;

/**
 * Created by Shepherdxx on 08.11.2017.
 */

public class PlayListInfo {
    /**
     * ID of this playlist to manipulate
     */
    public long playlistId;
    /**
     * Name of this playlist (used for the toast message)
     */
    public String name;
    /**
     * Populate playlist using this audioIds
     */
    public ArrayList<Long> audioIds;

    public PlayListInfo(long playlistId, String name) {
        this.playlistId = playlistId;
        this.name = name;
    }

    public int plType;

    public ArrayList<TrackInfo> audioTracks;

    public long getPlaylistId()         {return playlistId;}

    public String getName()             {return name;}

    public static PlayListInfo All()    {return  new PlayListInfo(Constants.PLAYLIST_All_Audio,_PLAYLIST_All_Audio);}

    public static PlayListInfo Cache()  {return  new PlayListInfo(Constants.PLAYLIST_Cache,_PLAYLIST_Cache);}

    public static PlayListInfo Radio()  {return  new PlayListInfo(Constants.PLAYLIST_RADIO,_PLAYLIST_RADIO);}
}
