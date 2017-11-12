package com.shepherdxx.celestialmp.extras;

import com.shepherdxx.celestialmp.vkmusicextras.Song;

import java.io.File;

/**
 * Created by Shepherdxx on 17.09.2017.
 */

public interface AsyncResponse {
    void processFinish(File output);
    void encodingFinish(Song sdata);
    void processFinish(String mName);
}