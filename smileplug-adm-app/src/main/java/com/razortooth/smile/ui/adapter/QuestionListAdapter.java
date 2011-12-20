package com.razortooth.smile.ui.adapter;

import java.util.List;

import org.json.JSONException;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.razortooth.smile.R;
import com.razortooth.smile.bu.BoardManager;
import com.razortooth.smile.bu.exception.DataAccessException;
import com.razortooth.smile.domain.Question;
import com.razortooth.smile.domain.Results;

public class QuestionListAdapter extends ArrayAdapter<Question> {
    private Context context;
    private String ip;

    public QuestionListAdapter(Context context, List<Question> items, String ip) {
        super(context, 0, items);

        this.context = context;
        this.ip = ip;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Question question = getItem(position);

        if (convertView == null) {
            Context context = getContext();
            LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.questions_item, parent, false);
        }

        TextView tvNumber = (TextView) convertView.findViewById(R.id.tv_number);
        tvNumber.setText(String.valueOf(question.getNumber()));

        TextView tvOwner = (TextView) convertView.findViewById(R.id.tv_owner);
        tvOwner.setText(question.getOwner());

        try {
            Results results = BoardManager.retrieveResults(ip, context);
            TextView tvHitAverage = (TextView) convertView.findViewById(R.id.tv_hit_average);
            tvHitAverage.setText(String.valueOf(results.getQuestionsCorrectPercentage().get(
                position)));
        } catch (NetworkErrorException e) {
            // TODO
        } catch (DataAccessException e) {
            // TODO
        } catch (JSONException e) {
            // TODO
        }

        TextView tvRating = (TextView) convertView.findViewById(R.id.tv_rating);
        tvRating.setText(String.valueOf(question.getRating()));

        return convertView;
    }
}
