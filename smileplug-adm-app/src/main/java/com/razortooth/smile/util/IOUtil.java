package com.razortooth.smile.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.razortooth.smile.bu.Constants;

public class IOUtil {

    private static final int IO_BUFFER_SIZE = 4 * 1024;

    private IOUtil() {
        // Empty
    }

    public static final void silentClose(Closeable c) {
        try {
            if (c != null) {
                c.close();
            }
        } catch (IOException e) {
            // Empty
        }
    }

    public static byte[] loadBytesFromResource(String resource) throws IOException {
        InputStream is = IOUtil.class.getResourceAsStream(resource);
        return loadBytes(is);
    }

    public static byte[] loadBytes(InputStream is) throws IOException {

        int len;
        int size = 1024;
        byte[] buf;

        if (is instanceof ByteArrayInputStream) {
            size = is.available();
            buf = new byte[size];
            len = is.read(buf, 0, size);
        } else {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            buf = new byte[size];
            while ((len = is.read(buf, 0, size)) != -1) {
                bos.write(buf, 0, len);
            }
            buf = bos.toByteArray();
        }
        return buf;

    }

    public static byte[] loadBytes(File file) throws IOException {

        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("File '" + file.getAbsolutePath() + "' is too large");
        }

        FileInputStream fis = new FileInputStream(file);
        try {
            return loadBytes(fis);
        } finally {
            silentClose(fis);
        }

    }

    public static String loadContent(InputStream in, String encoding) throws IOException {
        byte[] bytes = loadBytes(in);
        return new String(bytes, encoding);
    }

    public static Bitmap loadBitmapFromUrl(String url) {
        Bitmap bitmap = null;
        InputStream in = null;

        try {
            in = new BufferedInputStream(new URL(url).openStream(), IO_BUFFER_SIZE);

            final byte[] data = loadBytes(in);
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        } catch (IOException e) {
            Log.e(Constants.LOG_CATEGORY, "Could not load Bitmap from: " + url);
        } finally {
            silentClose(in);
        }

        return bitmap;
    }
}
