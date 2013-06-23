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
package org.smilec.smile.ui.adapter;

import java.util.List;

import org.smilec.smile.R;
import org.smilec.smile.domain.Student;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class StudentListAdapter extends ArrayAdapter<Student> {

    public StudentListAdapter(Context context, List<Student> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Student student = getItem(position);

        if (convertView == null) {
            Context context = getContext();
            LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.students_item, parent, false);
        }

        if (student != null) {
            TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
            tvName.setText(student.getName());

            TextView tvIp = (TextView) convertView.findViewById(R.id.tv_ip);
            tvIp.setText(student.getIp());

            CheckBox cbQuestion = (CheckBox) convertView.findViewById(R.id.tv_question);
            cbQuestion.setChecked(student.isMade());
            cbQuestion.setClickable(false);

            CheckBox tvAnswers = (CheckBox) convertView.findViewById(R.id.tv_answers);
            tvAnswers.setChecked(student.isSolved());
            tvAnswers.setClickable(false);

            TextView tvScore = (TextView) convertView.findViewById(R.id.tv_score);
            tvScore.setText(String.valueOf(student.getScore()));
        }

        return convertView;

    }

}
