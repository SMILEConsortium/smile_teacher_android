package net.iharder;

import org.junit.Assert;
import org.junit.Test;

import com.razortooth.smile.util.IOUtil;

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
