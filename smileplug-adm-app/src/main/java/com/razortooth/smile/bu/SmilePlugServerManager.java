package com.razortooth.smile.bu;

import java.util.Collection;

import android.accounts.NetworkErrorException;
import android.content.Context;

import com.google.gson.Gson;
import com.razortooth.smile.domain.Question;
import com.razortooth.smile.domain.QuestionWrapper;
import com.razortooth.smile.util.SmilePlugUtil;

public class SmilePlugServerManager extends AbstractBaseManager {

    public void startMakingQuestions(String ip, Context context) throws NetworkErrorException {
        String url = SmilePlugUtil.createUrl(ip, SmilePlugUtil.START_MAKING_QUESTIONS_URL);
        put(ip, context, url, "{}");
    }

    public void startUsingPreparedQuestions(String ip, Context context,
        Collection<Question> questions) throws NetworkErrorException {
        String url = SmilePlugUtil.createUrl(ip, SmilePlugUtil.QUESTION_URL);
        if (questions != null) {
            for (Question question : questions) {
                QuestionWrapper questionWrapper = new QuestionWrapper(question);
                Gson gson = new Gson();
                String json = gson.toJson(questionWrapper);
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
                QuestionWrapper questionWrapper = new QuestionWrapper(question);
                Gson gson = new Gson();
                String json = gson.toJson(questionWrapper);
                put(ip, context, url, json);
            }
        }
        startMakingQuestions(ip, context);
    }

    public void startSolvingQuestions(String ip, Context context) throws NetworkErrorException {
        String url = SmilePlugUtil.createUrl(ip, SmilePlugUtil.START_SOLVING_QUESTIONS_URL);
        put(ip, context, url, "{}");
    }

    public void showResults(String ip, Context context) throws NetworkErrorException {
        String url = SmilePlugUtil.createUrl(ip, SmilePlugUtil.SHOW_RESULTS_URL);
        put(ip, context, url, "{}");
    }

}
