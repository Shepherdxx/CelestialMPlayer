package com.shepherdxx.celestialmp.vkmusicextras;

/**
 * Created by Shepherdxx on 14.09.2017.
 */

public class Song {

    String path;
    String filename;
    String title;
    String artist;
    boolean isDecoded;

    public void setPath(String path) { this.path = path; }
    public void setFilename(String filename) { this.filename = filename; }
    public void setTitle(String title) { this.title = title; }
    public void setArtist(String artist) { this.artist = artist; }
    public void setDecoded(boolean isDecoded) { this.isDecoded = isDecoded; }

    public String getPath() { return path; }
    public String getFilename() { return filename; }
    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public boolean getSongDecoded() {
        if (isDecoded == true)
            return true;
        return false;
    }

}