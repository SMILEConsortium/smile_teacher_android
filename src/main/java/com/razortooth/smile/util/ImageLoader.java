package com.razortooth.smile.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import android.util.Log;

public class ImageLoader {

    private static final int IO_BUFFER_SIZE = 4 * 1024;

    public static byte[] loadBitmap(String url) {
        InputStream in = null;
        BufferedOutputStream out = null;
        byte[] data = null;

        try {
            in = new BufferedInputStream(new URL(url).openStream(), IO_BUFFER_SIZE);

            final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
            out = new BufferedOutputStream(dataStream, IO_BUFFER_SIZE);
            IOUtil.copy(in, out);
            out.flush();

            data = dataStream.toByteArray();

        } catch (IOException e) {
            Log.e("", "Could not load Bitmap from: " + url);
        } finally {
            IOUtil.silentClose(in);
            IOUtil.silentClose(out);
        }

        return data;
    }
}
