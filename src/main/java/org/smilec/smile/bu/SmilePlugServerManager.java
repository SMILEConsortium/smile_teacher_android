package org.smile.smilec.bu;

import java.io.InputStream;
import java.util.Collection;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import org.smile.smilec.bu.exception.DataAccessException;
import org.smile.smilec.bu.json.CurrentMessageJSONParser;
import org.smile.smilec.domain.LocalQuestionWrapper;
import org.smile.smilec.domain.Question;
import org.smile.smilec.domain.ServerQuestionWrapper;
import org.smile.smilec.util.HttpUtil;
import org.smile.smilec.util.IOUtil;
import org.smile.smilec.util.SmilePlugUtil;

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
                LocalQuestionWrapper questionWrapper = new LocalQuestionWrapper(question);
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
                ServerQuestionWrapper questionWrapper = new ServerQuestionWrapper(question);
                Gson gson = new Gson();
                String json = gson.toJson(questionWrapper);
                put(ip, context, url, json);
            }
        }
        startMakingQuestions(ip, context);
    }

    public void resetGame(String ip, Context context) throws NetworkErrorException {
        String url = SmilePlugUtil.createUrl(ip, SmilePlugUtil.RESET_URL);

        put(ip, context, url, "{}");

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

    public void showResults(String ip, Context context) throws NetworkErrorException {
        String url = SmilePlugUtil.createUrl(ip, SmilePlugUtil.SHOW_RESULTS_URL);
        put(ip, context, url, "{}");
    }

}
