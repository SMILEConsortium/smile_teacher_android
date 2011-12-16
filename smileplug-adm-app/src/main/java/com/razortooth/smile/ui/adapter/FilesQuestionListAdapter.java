package com.razortooth.smile.ui.adapter;

import java.io.File;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.razortooth.smile.R;

public class FilesQuestionListAdapter extends ArrayAdapter<File> {

    public FilesQuestionListAdapter(Context context, File[] items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final File fileQuestion = getItem(position);

        if (convertView == null) {
            Context context = getContext();
            LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.use_prepared_questions_item, parent, false);
        }

        final TextView tvFileName = (TextView) convertView.findViewById(R.id.tv_file_name);
        tvFileName.setText(String.valueOf(fileQuestion.getName()));

        return convertView;
    }
}
