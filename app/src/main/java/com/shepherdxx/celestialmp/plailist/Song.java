package com.shepherdxx.celestialmp.plailist;

import android.database.Cursor;

import com.shepherdxx.celestialmp.medialibrary.MediaLibrary;

/**
 * Created by Shepherdxx on 08.11.2017.
 */

public class Song implements Comparable<Song> {
    /**
     * Indicates that this song was randomly selected from all songs.
     */
    public static final int FLAG_RANDOM = 0x1;
    /**
     * If set, this song has no cover art. If not set, this song may or may not
     * have cover art.
     */
    public static final int FLAG_NO_COVER = 0x2;
    /**
     * The number of flags.
     */
    public static final int FLAG_COUNT = 2;


    public static final String[] EMPTY_PROJECTION = {
            MediaLibrary.SongColumns._ID,
    };

    public static final String[] FILLED_PROJECTION = {
            MediaLibrary.SongColumns._ID,
            MediaLibrary.SongColumns.PATH,
            MediaLibrary.SongColumns.TITLE,
            MediaLibrary.AlbumColumns.ALBUM,
            MediaLibrary.ContributorColumns.ARTIST,
            MediaLibrary.SongColumns.ALBUM_ID,
            MediaLibrary.ContributorColumns.ARTIST_ID,
            MediaLibrary.SongColumns.DURATION,
            MediaLibrary.SongColumns.SONG_NUMBER,
    };

    public static final String[] EMPTY_PLAYLIST_PROJECTION = {
            MediaLibrary.PlaylistSongColumns.SONG_ID,
    };

    public static final String[] FILLED_PLAYLIST_PROJECTION = {
            MediaLibrary.PlaylistSongColumns.SONG_ID,
            MediaLibrary.SongColumns.PATH,
            MediaLibrary.SongColumns.TITLE,
            MediaLibrary.AlbumColumns.ALBUM,
            MediaLibrary.ContributorColumns.ARTIST,
            MediaLibrary.SongColumns.ALBUM_ID,
            MediaLibrary.ContributorColumns.ARTIST_ID,
            MediaLibrary.SongColumns.DURATION,
            MediaLibrary.SongColumns.SONG_NUMBER,
    };

//    /**
//     * The cache instance.
//     */
//    private static CoverCache sCoverCache = null;

    /**
     * Id of this song in the MediaStore
     */
    public long id;
    /**
     * Id of this song's album in the MediaStore
     */
    public long albumId;
    /**
     * Id of this song's artist in the MediaStore
     */
    public long artistId;

    /**
     * Path to the data for this song
     */
    public String path;

    /**
     * Song title
     */
    public String title;
    /**
     * Album name
     */
    public String album;
    /**
     * Artist name
     */
    public String artist;

    /**
     * Length of the song in milliseconds.
     */
    public long duration;
    /**
     * The position of the song in its album.
     */
    public int trackNumber;

    /**
     * Song flags. Currently {@link #FLAG_RANDOM} or {@link #FLAG_NO_COVER}.
     */
    public int flags;

    /**
     * Initialize the song with the specified id. Call populate to fill fields
     * in the song.
     */
    public Song(long id)
    {
        this.id = id;
    }

    /**
     * Initialize the song with the specified id and flags. Call populate to
     * fill fields in the song.
     */
    public Song(long id, int flags)
    {
        this.id = id;
        this.flags = flags;
    }

    /**
     * Return true if this song was retrieved from randomSong().
     */
    public boolean isRandom()
    {
        return (flags & FLAG_RANDOM) != 0;
    }

    /**
     * Returns true if the song is filled
     */
    public boolean isFilled()
    {
        return (id != -1 && path != null);
    }

    /**
     * Populate fields with data from the supplied cursor.
     *
     * @param cursor Cursor queried with FILLED_PROJECTION projection
     */
    public void populate(Cursor cursor)
    {
        id = cursor.getLong(0);
        path = cursor.getString(1);
        title = cursor.getString(2);
        album = cursor.getString(3);
        artist = cursor.getString(4);
        albumId = cursor.getLong(5);
        artistId = cursor.getLong(6);
        duration = cursor.getLong(7);
        trackNumber = cursor.getInt(8);
    }

    /**
     * Get the id of the given song.
     *
     * @param song The Song to get the id from.
     * @return The id, or 0 if the given song is null.
     */
    public static long getId(Song song)
    {
        if (song == null)
            return 0;
        return song.id;
    }

//    /**
//     * Query the large album art for this song.
//     *
//     * @param context A context to use.
//     * @return The album art or null if no album art could be found
//     */
//    public Bitmap getCover(Context context) {
//        return getCoverInternal(context, CoverCache.SIZE_LARGE);
//    }
//
//    /**
//     * Query the small album art for this song.
//     *
//     * @param context A context to use.
//     * @return The album art or null if no album art could be found
//     */
//    public Bitmap getSmallCover(Context context) {
//        return getCoverInternal(context, CoverCache.SIZE_SMALL);
//    }

//    /**
//     * Internal implementation of getCover
//     *
//     * @param context A context to use.
//     * @param size The desired cover size
//     * @return The album art or null if no album art could be found
//     */
//    private Bitmap getCoverInternal(Context context, int size) {
//        if (CoverCache.mCoverLoadMode == 0 || id <= -1 || (flags & FLAG_NO_COVER) != 0)
//            return null;
//
//        if (sCoverCache == null)
//            sCoverCache = new CoverCache(context.getApplicationContext());
//
//        Bitmap cover = sCoverCache.getCoverFromSong(this, size);
//
//        if (cover == null)
//            flags |= FLAG_NO_COVER;
//        return cover;
//    }

    @Override
    public String toString()
    {
        return String.format("%d %d %s", id, albumId, path);
    }

    /**
     * Compares the album ids of the two songs; if equal, compares track order.
     */
    @Override
    public int compareTo(Song other)
    {
        if (albumId == other.albumId)
            return trackNumber - other.trackNumber;
        if (albumId > other.albumId)
            return 1;
        return -1;
    }
}
