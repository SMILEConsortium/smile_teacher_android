package com.razortooth.smile.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class ImageLoader {

    private static final int IO_BUFFER_SIZE = 4 * 1024;

    public static Bitmap loadBitmap(String url) {
        Bitmap bitmap = null;
        InputStream in = null;
        BufferedOutputStream out = null;

        try {
            in = new BufferedInputStream(new URL(url).openStream(), IO_BUFFER_SIZE);

            final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
            out = new BufferedOutputStream(dataStream, IO_BUFFER_SIZE);
            IOUtil.copy(in, out);
            out.flush();

            final byte[] data = dataStream.toByteArray();
            BitmapFactory.Options options = new BitmapFactory.Options();

            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
        } catch (IOException e) {
            Log.e("", "Could not load Bitmap from: " + url);
        } finally {
            IOUtil.silentClose(in);
            IOUtil.silentClose(out);
        }

        return bitmap;
    }
}
