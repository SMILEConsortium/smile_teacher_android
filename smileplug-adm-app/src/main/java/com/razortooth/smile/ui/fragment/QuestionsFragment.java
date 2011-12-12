package com.razortooth.smile.ui.fragment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.razortooth.smile.R;
import com.razortooth.smile.domain.Board;
import com.razortooth.smile.domain.Question;
import com.razortooth.smile.ui.QuestionStatusDetailsActivity;
import com.razortooth.smile.ui.adapter.QuestionListAdapter;

public class QuestionsFragment extends AbstractFragment implements OnClickListener {

    private final List<Question> questions = new ArrayList<Question>();
    private ArrayAdapter<Question> adapter;

    private Button save;

    @Override
    protected int getLayout() {
        return R.layout.questions;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        adapter = new QuestionListAdapter(getActivity(), questions);
        ListView list = (ListView) getActivity().findViewById(R.id.list_questions);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new OpenItemDetailsListener());

        save = (Button) getActivity().findViewById(R.id.bt_save);
        save.setOnClickListener(this);
    }

    private class OpenItemDetailsListener implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
            Question question = questions.get(position);

            Intent intent = new Intent(getActivity(), QuestionStatusDetailsActivity.class);
            intent.putExtra(QuestionStatusDetailsActivity.PARAM_QUESTION, question);
            startActivity(intent);
        }
    }

    @Override
    public void updateFragment(final Board board) {

        questions.clear();

        Collection<Question> newQuestions = board.getQuestions();
        if (newQuestions != null) {
            questions.addAll(newQuestions);
        }

        adapter.notifyDataSetChanged();

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_save) {
            // save questions
        }
    }
}
