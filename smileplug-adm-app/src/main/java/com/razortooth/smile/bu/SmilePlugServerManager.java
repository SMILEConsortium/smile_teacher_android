package com.razortooth.smile.bu;

import android.accounts.NetworkErrorException;
import android.content.Context;

import com.razortooth.smile.util.SmilePlugUtil;

public class SmilePlugServerManager extends AbstractBaseManager {

    @Override
    public void connect(String ip, Context context) throws NetworkErrorException {
        super.connect(ip, context);
    }

    public void startMakingQuestions(String ip, Context context) throws NetworkErrorException {
        String url = SmilePlugUtil.createUrl(ip, SmilePlugUtil.START_MAKING_QUESTIONS_URL);
        put(ip, context, url);
    }

    public void startSolvingQuestions(String ip, Context context) throws NetworkErrorException {
        String url = SmilePlugUtil.createUrl(ip, SmilePlugUtil.START_SOLVING_QUESTIONS_URL);
        put(ip, context, url);
    }

    public void showResults(String ip, Context context) throws NetworkErrorException {
        String url = SmilePlugUtil.createUrl(ip, SmilePlugUtil.SHOW_RESULTS_URL);
        put(ip, context, url);
    }

}
