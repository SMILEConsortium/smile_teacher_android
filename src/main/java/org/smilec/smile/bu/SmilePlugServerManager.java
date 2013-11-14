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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smilec.smile.bu.exception.DataAccessException;
import org.smilec.smile.bu.json.CurrentMessageJSONParser;
import org.smilec.smile.domain.Board;
import org.smilec.smile.domain.LocalQuestionWrapper;
import org.smilec.smile.domain.Question;
import org.smilec.smile.domain.ServerQuestionWrapper;
import org.smilec.smile.util.HttpUtil;
import org.smilec.smile.util.IOUtil;
import org.smilec.smile.util.SendEmailAsyncTask;
import org.smilec.smile.util.SmilePlugUtil;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

public class SmilePlugServerManager extends AbstractBaseManager {

    // XXX TODO: Add arguments to take Teacher Name, Session Name, and Group Name
    public void startMakingQuestions(String ip, Context context) throws NetworkErrorException {
        String url = SmilePlugUtil.createUrl(ip, SmilePlugUtil.START_MAKING_QUESTIONS_URL);
        put(ip, context, url, "{}");
    }

    public void startUsingPreparedQuestions(String ip, Context context,
        Collection<Question> questions) throws NetworkErrorException {
        String url = SmilePlugUtil.createUrl(ip, SmilePlugUtil.QUESTION_URL);
        if (questions != null) {
            for (Question question : questions) {
                LocalQuestionWrapper questionWrapper = new LocalQuestionWrapper(question);
                Gson gson = new Gson();
                String json = gson.toJson(questionWrapper);
                Log.d("SmilePlugServerManager", "serialized question as JSON, use prepared: " + json);
                put(ip, context, url, json);
            }
        }
        startMakingQuestions(ip, context);
    }

    public void getResults(String ip, Context context, Collection<Question> questions)
        throws NetworkErrorException {
        String url = SmilePlugUtil.createUrl(ip, SmilePlugUtil.RESULTS_URL);
        if (questions != null) {
            for (Question question : questions) {
                ServerQuestionWrapper questionWrapper = new ServerQuestionWrapper(question);
                Gson gson = new Gson();
                String json = gson.toJson(questionWrapper);
                Log.d("SmilePlugServerManager", "serialized question as JSON: " + json);
                put(ip, context, url, json);
            }
        }
        startMakingQuestions(ip, context);
    }

    public void resetGame(String ip, Context context) throws NetworkErrorException {
        String url = SmilePlugUtil.createUrl(ip, SmilePlugUtil.RESET_URL);

        put(ip, context, url, "{}");

    }

    public void createSession(String ip, String teacherName, String sessionTitle, String groupName, Context context) throws NetworkErrorException {
    	
    	String url = SmilePlugUtil.createUrl(ip, SmilePlugUtil.CREATE_SESSION);
    	JSONObject jsonSessionValues = new JSONObject();
    	
    	try {
    		jsonSessionValues.put("TEACHER", teacherName);
    		jsonSessionValues.put("SESSION", sessionTitle);
    		jsonSessionValues.put("GROUP", groupName);
	    	
    	} catch (Exception e) {
			Log.e("SMILE_TEACHER:SmilePlugServerManager", "ERROR, reason: " + e.getMessage());
			e.printStackTrace();
		}
    	
    	put(ip,context,url,jsonSessionValues.toString());
    }
    
    public String currentMessageGame(String ip, Context context) throws NetworkErrorException {
        String url = SmilePlugUtil.createUrl(ip, SmilePlugUtil.CURRENT_MESSAGE_URL);

        InputStream is = HttpUtil.executeGet(url);

        try {
            is = get(ip, context, url);
            return CurrentMessageJSONParser.getStatus(is);
        } catch (DataAccessException e) {
            Log.e(Constants.LOG_CATEGORY, "Error: ", e);
        } finally {
            IOUtil.silentClose(is);
        }

        return null;

    }

    public void startSolvingQuestions(String ip, Context context) throws NetworkErrorException {
        String url = SmilePlugUtil.createUrl(ip, SmilePlugUtil.START_SOLVING_QUESTIONS_URL);
        put(ip, context, url, "{}");
    }

    public void startRetakeQuestions(String ip, Context context, Board board) throws NetworkErrorException {
        String url = "http://" + ip + "/" + SmilePlugUtil.RETAKE_QUESTIONS_URL;

        try {
	        JSONObject retakeJson = new JSONObject();
			int questionsNumber = board.getQuestionsNumber();
			Collection<Question> questions = board.getQuestions();
			List<Integer> answersList = new ArrayList<Integer>();
			for (Iterator<Question> iterator = questions.iterator(); iterator.hasNext();) {
				Question question = (Question) iterator.next();
				answersList.add(question.getAnswer());
			}
			JSONArray answers = new JSONArray(answersList);

		    retakeJson.put("TIME_LIMIT", 10);
			retakeJson.put("NUMQ", questionsNumber);
			retakeJson.put("RANSWER", answers);

			post(ip, context, url, retakeJson.toString());

        } catch (JSONException e) {
        	new SendEmailAsyncTask(e.getMessage(),JSONException.class.getName(),SmilePlugServerManager.class.getName()).execute();
	    	e.printStackTrace();
	    }

    }

    public void showResults(String ip, Context context) throws NetworkErrorException {
        String url = SmilePlugUtil.createUrl(ip, SmilePlugUtil.SHOW_RESULTS_URL);
        put(ip, context, url, "{}");

        // Always store the results
        
    }

}
