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
package org.smilec.smile.bu.json;

import java.io.IOException;
import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;
import org.smilec.smile.bu.exception.DataAccessException;
import org.smilec.smile.util.IOUtil;
import org.smilec.smile.util.SendEmailAsyncTask;

import android.text.GetChars;

public class CurrentMessageJSONParser {

    private static final String TYPE = "TYPE";
    private static final String ENCODING = "UTF-8";

    public static final String getStatus(InputStream is) throws DataAccessException {
        String s;
        try {
            s = IOUtil.loadContent(is, ENCODING);    
            s = "{[}";   // to test the sending of the JSONException            
            
            JSONObject json = new JSONObject(s);

            String type = "";
            if (!json.toString().equals("{}")) {
                type = json.getString(TYPE);
            }

            return type;
        } catch (IOException e) {
            throw new DataAccessException(e);
        } catch (JSONException e) {
        	new SendEmailAsyncTask(e.getMessage(),JSONException.class.getName(),CurrentMessageJSONParser.class.getName()).execute();
            throw new DataAccessException(e);
        }
    }
}
