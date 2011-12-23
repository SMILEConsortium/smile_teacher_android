package com.razortooth.smile.bu;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.NetworkErrorException;
import android.content.Context;

import com.razortooth.smile.bu.exception.DataAccessException;
import com.razortooth.smile.bu.json.AnswersAndRatingsJSONParser;
import com.razortooth.smile.bu.json.QuestionJSONParser;
import com.razortooth.smile.bu.json.ResultsJSONParser;
import com.razortooth.smile.bu.json.StudentJSONParser;
import com.razortooth.smile.domain.Board;
import com.razortooth.smile.domain.Question;
import com.razortooth.smile.domain.Results;
import com.razortooth.smile.domain.Student;
import com.razortooth.smile.util.HttpUtil;
import com.razortooth.smile.util.IOUtil;
import com.razortooth.smile.util.SmilePlugUtil;

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
            array = new JSONArray(s);
        } catch (JSONException e) {
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
            Question q = QuestionJSONParser.process(++number, object, students);
            map.put(number, q);
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
