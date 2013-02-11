package org.smile.smilec.bu.json;

import org.json.JSONObject;

import org.smile.smilec.domain.Student;

public class StudentJSONParser {

    private static final String IP = "IP";
    private static final String NAME = "NAME";

    public static final Student process(JSONObject object) {

        String ip = object.optString(IP);
        String name = object.optString(NAME);

        Student student = new Student(ip, name);
        return student;

    }

}
