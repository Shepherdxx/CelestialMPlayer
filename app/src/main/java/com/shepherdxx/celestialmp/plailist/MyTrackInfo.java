package com.shepherdxx.celestialmp.plailist;

import android.media.MediaPlayer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.shepherdxx.celestialmp.extras.Constants.FILE_L_M_ASC;
import static com.shepherdxx.celestialmp.extras.Constants.FILE_L_M__DES;
import static com.shepherdxx.celestialmp.extras.Constants.FILE_NAME_ASC;
import static com.shepherdxx.celestialmp.extras.Constants.FILE_NAME_DES;
import static com.shepherdxx.celestialmp.extras.Constants.FILE_SIZE_ASC;
import static com.shepherdxx.celestialmp.extras.Constants.FILE_SIZE_DES;

/**
 * Created by Shepherdxx on 03.11.2017.
 */

public class MyTrackInfo {
    private String fileName, trackName, Artist, Album;
    private long duration;
    private int vk_Id;
    // Drawable resource ID
    private int mImageResourceId = NO_IMAGE_PROVAIDED;
    private static final int NO_IMAGE_PROVAIDED = -1;

    public MyTrackInfo(String fileName, String trackName, String Artist, String Album) {
      this(fileName, trackName, Artist, Album, 0);
   }

    public MyTrackInfo(String fileName, String trackName, String Artist, String Album, int vk_Id){
        this.fileName = fileName;
        this.trackName=trackName;
        this.Artist=Artist;
        this.Album=Album;
        this.vk_Id=vk_Id;
   }

    public MyTrackInfo(String fileName, String trackName, String Artist, String Album, long duration){
        this.fileName = fileName;
        this.trackName=trackName;
        this.Artist=Artist;
        this.Album=Album;
        this.duration=duration;
    }
   
    public MyTrackInfo(String mRadio, String mDescription, String mUri){
        this.mRadio = mRadio;
        this.mUri = mUri;
        this.mDescription=mDescription;
    }

    public MyTrackInfo(String mFileName, String mFilePath, long mFileSize, long mFileCreated) {
        this.mFileName =  mFileName;
        this.mFilePath = mFilePath;
        this.mFileSize = mFileSize;
        this.mFileCreated = mFileCreated;
    }

    private String mRadio;
    private String mDescription;
    private String mUri;

    public String getmRadio() {
        return mRadio;
    }

    public String getmDescription() {
        return mDescription;
    }

    private String getmUri() {
        return mUri;
    }

    public void setmRadio(String mRadio) {
        this.mRadio = mRadio;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public void setmUri(String mUri) {
        this.mUri = mUri;
    }

    public java.lang.String getFileName() {
        return fileName;
    }

    public String getTrackName() {
        return trackName;
    }

    public String getArtist() {
        return Artist;
    }

    public String getAlbum() {
        return Album;
    }

    public int getVk_Id() {
        return vk_Id;
    }

    /**
     * Get the image resource ID
     */
    public int getImageResourceId() {
        return this.mImageResourceId;
    }

    /** дает нам знать есть ли изображение
     * @return
     */

    public boolean hasImage() {
        return mImageResourceId!=NO_IMAGE_PROVAIDED;}


    public static Comparator<MyTrackInfo> ScrollComparator(int var) {
        Comparator<MyTrackInfo> comparator=null;
        switch (var) {
            case FILE_SIZE_ASC:
                comparator = new Comparator<MyTrackInfo>() {
                    public int compare(MyTrackInfo s1, MyTrackInfo s2) {
                        long rollno1 = s1.getmFileSize();
                        long rollno2 = s2.getmFileSize();
                        /*For ascending order*/
                        return (int) rollno1 - (int) rollno2;}};
                break;
            case FILE_SIZE_DES:
                comparator = new Comparator<MyTrackInfo>() {
                    public int compare(MyTrackInfo s1, MyTrackInfo s2) {
                        long rollno1 = s1.getmFileSize();
                        long rollno2 = s2.getmFileSize();
                        /*For descending order*/
                        return (int) rollno2 - (int) rollno1;}};
                break;
            case FILE_NAME_ASC:
                comparator = new Comparator<MyTrackInfo>() {
                    public int compare(MyTrackInfo s1, MyTrackInfo s2) {
                        String StudentName1 = s1.getmFileName().toUpperCase();
                        String StudentName2 = s2.getmFileName().toUpperCase();
                        //ascending order
                        return StudentName1.compareTo(StudentName2);}
                };
                break;
            case FILE_NAME_DES:
                comparator = new Comparator<MyTrackInfo>() {
                    public int compare(MyTrackInfo s1, MyTrackInfo s2) {
                        String StudentName1 = s1.getmFileName().toUpperCase();
                        String StudentName2 = s2.getmFileName().toUpperCase();
                        //descending order
                        return StudentName2.compareTo(StudentName1);
                    }
                };
                break;
            case FILE_L_M_ASC:
                comparator = new Comparator<MyTrackInfo>() {
                    public int compare(MyTrackInfo s1, MyTrackInfo s2) {
                        long rollno1 = s1.getmFileCreated();
                        long rollno2 = s2.getmFileCreated();
                            /*For ascending order*/
                        return (int) rollno1 - (int) rollno2;
                    }
                };
                break;
            case FILE_L_M__DES:
                comparator = new Comparator<MyTrackInfo>() {
                    public int compare(MyTrackInfo s1, MyTrackInfo s2) {
                        long rollno1 = s1.getmFileSize();
                        long rollno2 = s2.getmFileSize();
                        /*For descending order*/
                        return (int) rollno2 - (int) rollno1;}};
                break;
        }
        return comparator;
    }

    // String value
    private String mFileName;
    private String mFilePath;
    private long mFileSize;
    private long mFileCreated;




    public String getmFileName() {
        return mFileName;
    }

    public long getmFileCreated() {
        return mFileCreated;
    }

    public long getmFileSize() {
        return mFileSize;
    }

    public String getmFilePath() {
        return mFilePath;
    }


    /**
     * An array of sample (dummy) items.
     */
    public static final List<MyTrackInfo.DummyItem> ITEMS = new ArrayList<MyTrackInfo.DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, MyTrackInfo.DummyItem> ITEM_MAP = new HashMap<String, MyTrackInfo.DummyItem>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(MyTrackInfo.DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static MyTrackInfo.DummyItem createDummyItem(int position) {
        return new MyTrackInfo.DummyItem(String.valueOf(position), "Item " + position, makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    public String getData(){
        if (getFileName()!=null) return getFileName();
        if (getmUri()!=null)return getmUri();
        return null;
    }

    public String getTitle() {
        if (getmRadio()!=null) return getmRadio();
        if (getTrackName()!=null)return getTrackName();
        return null;
    }

    public Long getDuration() {
        return duration;
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public final String id;
        public final String content;
        public final String details;

        public DummyItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }

    private int playlistId;

    public int getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(int playlistId) {
        this.playlistId = playlistId;
    }

}
