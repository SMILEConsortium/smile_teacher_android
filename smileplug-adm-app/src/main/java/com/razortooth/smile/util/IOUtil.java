package com.razortooth.smile.util;

import java.io.Closeable;
import java.io.IOException;

public class IOUtil {

    private IOUtil() {
        // Empty
    }

    // public static final void silentClose(InputStream is) {
    // try {
    // if (is != null) {
    // is.close();
    // }
    // } catch (IOException e) {
    // // Empty
    // }
    // }

    public static final void silentClose(Closeable c) {
        try {
            if (c != null) {
                c.close();
            }
        } catch (IOException e) {
            // Empty
        }
    }

}
