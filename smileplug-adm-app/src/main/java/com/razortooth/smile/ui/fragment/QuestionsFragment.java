package com.razortooth.smile.ui.fragment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.razortooth.smile.R;
import com.razortooth.smile.domain.Board;
import com.razortooth.smile.domain.Question;
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

        save = (Button) getActivity().findViewById(R.id.bt_save);
        save.setOnClickListener(this);
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
