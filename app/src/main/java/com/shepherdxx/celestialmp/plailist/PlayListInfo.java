package com.shepherdxx.celestialmp.plailist;

import java.util.ArrayList;

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

    public ArrayList<PlayerTrackInfo> audioTracks;

    public long getPlaylistId()         {return playlistId;}

    public String getName()             {return name;}

    public static PlayListInfo All()    {return  new PlayListInfo(-1, "All Audio");}

    public static PlayListInfo Cache()  {return  new PlayListInfo(-2,"Cache");}
}
