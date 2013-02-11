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
package net.iharder;

import org.junit.Assert;
import org.junit.Test;

import org.smile.smilec.util.IOUtil;

public class Base64Test {

    private static final int STRING_LEN = 2264;
    private static final int ARRAY_LEN = 1697;
    private static final String IMAGE_TEST = "/android.jpg";

    @Test
    public void testEncodeAndDecode() throws Exception {

        // Check the original array
        byte[] originalArray = IOUtil.loadBytesFromResource(IMAGE_TEST);
        Assert.assertEquals(ARRAY_LEN, originalArray.length);

        // Encode the array as string and check it
        String encodedString = Base64.encodeBytes(originalArray);
        Assert.assertEquals(STRING_LEN, encodedString.length());

        // Check the decoded array
        byte[] decodedArray = Base64.decode(encodedString);
        Assert.assertEquals(ARRAY_LEN, decodedArray.length);

        // Check the both arrays
        Assert.assertEquals(originalArray.length, decodedArray.length);
        for (int i = 0; i < originalArray.length; i++) {
            Assert.assertEquals(originalArray[i], decodedArray[i]);
        }

    }

}
