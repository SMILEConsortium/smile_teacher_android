/**
Copyright 2012-2013 SMILE Consortium, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
**/
package org.smilec.smile.util;

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
