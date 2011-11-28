package com.razortooth.smile.util;

import java.io.IOException;
import java.io.InputStream;

public class IOUtil {

	public static final void silentClose(InputStream is) {
		try {
			if (is != null) {
				is.close();
			}
		} catch (IOException e) {
			// Empty
		}
	}

}
