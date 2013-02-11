package org.smile.smilec.bu.json;

import java.io.IOException;
import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;

import org.smile.smilec.bu.exception.DataAccessException;
import org.smile.smilec.util.IOUtil;

public class CurrentMessageJSONParser {

    private static final String TYPE = "TYPE";
    private static final String ENCODING = "UTF-8";

    public static final String getStatus(InputStream is) throws DataAccessException {
        String s;
        try {
            s = IOUtil.loadContent(is, ENCODING);

            JSONObject json = new JSONObject(s);

            String type = "";
            if (!json.toString().equals("{}")) {
                type = json.getString(TYPE);
            }

            return type;
        } catch (IOException e) {
            throw new DataAccessException(e);
        } catch (JSONException e) {
            throw new DataAccessException(e);
        }
    }
}
