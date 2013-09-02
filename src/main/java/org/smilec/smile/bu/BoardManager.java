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
package org.smilec.smile.bu;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smilec.smile.bu.exception.DataAccessException;
import org.smilec.smile.bu.json.AnswersAndRatingsJSONParser;
import org.smilec.smile.bu.json.CurrentMessageJSONParser;
import org.smilec.smile.bu.json.QuestionJSONParser;
import org.smilec.smile.bu.json.ResultsJSONParser;
import org.smilec.smile.bu.json.StudentJSONParser;
import org.smilec.smile.domain.Board;
import org.smilec.smile.domain.Question;
import org.smilec.smile.domain.Results;
import org.smilec.smile.domain.Student;
import org.smilec.smile.util.HttpUtil;
import org.smilec.smile.util.IOUtil;
import org.smilec.smile.util.SendEmailAsyncTask;
import org.smilec.smile.util.SmilePlugUtil;

import android.accounts.NetworkErrorException;
import android.content.Context;

public class BoardManager extends AbstractBaseManager {

    private static final String ENCODING = "UTF-8";

    private static enum Type {
        HAIL, QUESTION, QUESTION_PIC, ANSWER
    }

    public Board getBoard(String ip, Context context) throws DataAccessException,
        NetworkErrorException {

        String url = SmilePlugUtil.createUrl(ip, SmilePlugUtil.ALL_DATA_URL);
        InputStream is = HttpUtil.executeGet(url);

        try {
            is = get(ip, context, url);
            return loadBoard(is);
        } finally {
            IOUtil.silentClose(is);
        }

    }

    public Results retrieveResults(String ip, Context context) throws DataAccessException,
        NetworkErrorException {

        String url = SmilePlugUtil.createUrl(ip, SmilePlugUtil.RESULTS_URL);
        InputStream is = HttpUtil.executeGet(url);

        try {
            is = get(ip, context, url);
            return loadResults(is);
        } finally {
            IOUtil.silentClose(is);
        }

    }

    private static Results loadResults(InputStream is) throws DataAccessException {
        String s;
        Results results = null;
        try {
            s = IOUtil.loadContent(is, ENCODING);

            JSONObject json = new JSONObject(s);

            results = ResultsJSONParser.process(json);
        } catch (IOException e) {
            throw new DataAccessException(e);
        } catch (JSONException e) {
        	new SendEmailAsyncTask(e.getMessage(),"New "+JSONException.class.getName()+" in "+BoardManager.class.getName()).execute();
            throw new DataAccessException(e);
        }

        return results;
    }

    protected Board loadBoard(InputStream is) throws DataAccessException {

        String s;
        try {
            s = IOUtil.loadContent(is, ENCODING);
        } catch (IOException e) {
            throw new DataAccessException(e);
        }

        JSONArray array;
        try {
            if (!s.equals("")) {
                array = new JSONArray(s);
            } else {
                array = new JSONArray();
            }
        } catch (JSONException e) {
        	new SendEmailAsyncTask(e.getMessage(),"New "+JSONException.class.getName()+" in "+BoardManager.class.getName()).execute();
            throw new DataAccessException(e);
        }

        List<JSONObject> jsonArrayStudents = new ArrayList<JSONObject>();
        List<JSONObject> jsonArrayQuestions = new ArrayList<JSONObject>();
        List<JSONObject> jsonArrayAnswersAndRatings = new ArrayList<JSONObject>();

        for (int i = 0; i < array.length(); i++) {

            JSONObject object = array.optJSONObject(i);
            Type type = identifyType(object);

            if (type != null) {

                switch (type) {

                    case HAIL:
                        jsonArrayStudents.add(object);
                        break;

                    case QUESTION:
                    case QUESTION_PIC:
                        jsonArrayQuestions.add(object);
                        break;

                    case ANSWER:
                        jsonArrayAnswersAndRatings.add(object);
                        break;
                }
            } else {
                // Error
            }

        }

        Map<String, Student> mapStudents = processStudents(jsonArrayStudents);
        Map<Integer, Question> mapQuestions = processQuestions(jsonArrayQuestions, mapStudents);
        processAnswers(jsonArrayAnswersAndRatings, mapStudents, mapQuestions);

        return new Board(mapStudents.values(), mapQuestions.values());

    }

    private Map<String, Student> processStudents(List<JSONObject> array) {

        Map<String, Student> map = new HashMap<String, Student>();

        for (JSONObject object : array) {
            Student s = StudentJSONParser.process(object);
            map.put(s.getIp(), s);
        }

        return map;

    }

    private Map<Integer, Question> processQuestions(List<JSONObject> array,
        Map<String, Student> students) {

        Map<Integer, Question> map = new HashMap<Integer, Question>();

        int number = 0;
        for (JSONObject object : array) {
        	boolean duplicated = false;
            Question q = QuestionJSONParser.process(++number, object, students);
            for (Entry<Integer, Question> entry : map.entrySet()) {
            	Question value = entry.getValue();
            	if (value.equals(q)) {
            		duplicated = true;
            	}
            }
            if (!duplicated) {
            	map.put(number, q);            		
            }
        }

        return map;

    }

    private void processAnswers(List<JSONObject> array, Map<String, Student> students,
        Map<Integer, Question> questions) {

        for (JSONObject object : array) {
            AnswersAndRatingsJSONParser.process(object, students, questions);
        }

    }

    private Type identifyType(JSONObject object) {

        try {
            String type = object.getString("TYPE").toUpperCase();
            return Type.valueOf(type);
        } catch (Exception ex) {
            return null;
        }

    }

}
