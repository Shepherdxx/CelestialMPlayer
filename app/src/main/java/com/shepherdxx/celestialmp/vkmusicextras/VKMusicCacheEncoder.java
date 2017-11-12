package com.shepherdxx.celestialmp.vkmusicextras;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Позаиствовано с https://github.com/CheeryLee/VKEncodeMusic
 */

public class VKMusicCacheEncoder {
    String outFilename;
    String inFilename;

    int mask[] = { 0x0D, 0x1E, 0x2F, 0x40, 0x51, 0x62, 0x73, 0x84, 0x95,
            0xA6, 0xB7, 0xC8, 0xD9, 0xEA, 0xFB, 0x0C };

    public VKMusicCacheEncoder(String _in, String _out) {
        inFilename = _in;
        outFilename = _out;
    }

    public void processBytes() {
        int str_byte = 0;

        File encodedFile = new File(inFilename);
        File encodedFileInfo = new File(inFilename+".info");

        try {
            FileInputStream finStream = new FileInputStream(encodedFile);
            FileOutputStream foutStream = new FileOutputStream(outFilename);
            byte buffer[] = new byte[finStream.available()];
            finStream.read(buffer, 0, finStream.available());

            for (int i = 0; i < buffer.length; i++) {
                //int f_byte = buffer[i].intValue();

                buffer[i] ^= mask[str_byte];
                mask[str_byte] += 0x10;

                if (str_byte < 15)
                    str_byte++;
                else
                    str_byte = 0;
            }

            foutStream.write(buffer, 0, buffer.length);
            finStream.close();
            foutStream.close();

            encodedFile.delete();
            encodedFileInfo.delete();

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
