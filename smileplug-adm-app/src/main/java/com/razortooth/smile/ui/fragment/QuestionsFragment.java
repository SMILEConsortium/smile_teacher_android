package com.razortooth.smile.ui.fragment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.razortooth.smile.R;
import com.razortooth.smile.bu.QuestionsManager;
import com.razortooth.smile.bu.exception.DataAccessException;
import com.razortooth.smile.domain.Board;
import com.razortooth.smile.domain.Question;
import com.razortooth.smile.ui.QuestionStatusDetailsActivity;
import com.razortooth.smile.ui.adapter.QuestionListAdapter;
import com.razortooth.smile.util.ActivityUtil;

public class QuestionsFragment extends AbstractFragment {

    private final List<Question> questions = new ArrayList<Question>();
    private ArrayAdapter<Question> adapter;

    private Button btSave;

    @Override
    protected int getLayout() {
        return R.layout.questions;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        adapter = new QuestionListAdapter(getActivity(), questions);
        ListView lvListQuestions = (ListView) getActivity().findViewById(R.id.lv_questions);
        lvListQuestions.setAdapter(adapter);
        lvListQuestions.setOnItemClickListener(new OpenItemDetailsListener());

        btSave = (Button) getActivity().findViewById(R.id.bt_save);
        btSave.setOnClickListener(new SaveButtonListener());
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

    private class SaveButtonListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            Dialog saveDialog = new Dialog(QuestionsFragment.this.getActivity(), R.style.Dialog);
            saveDialog.setContentView(R.layout.save);
            saveDialog.show();

            Button save = (Button) saveDialog.findViewById(R.id.bt_save_file);
            save.setOnClickListener(new SaveFileDialogListener(saveDialog));
        }

        public class SaveFileDialogListener implements OnClickListener {
            private Dialog aboutDialog;

            public SaveFileDialogListener(Dialog aboutDialog) {
                this.aboutDialog = aboutDialog;
            }

            @Override
            public void onClick(View v) {
                TextView name = (TextView) aboutDialog.findViewById(R.id.et_name_file);
                try {
                    if (name.getText().toString().equals("")) {
                        name.setText("Questions_file");
                    }
                    new QuestionsManager().saveQuestions(name.getText().toString(), questions);
                    aboutDialog.dismiss();
                } catch (DataAccessException e) {
                    Log.e("QuestionsFragment", e.getMessage());
                }
                ActivityUtil.showLongToast(QuestionsFragment.this.getActivity(), R.string.saved);
            }
        }
    }
}
