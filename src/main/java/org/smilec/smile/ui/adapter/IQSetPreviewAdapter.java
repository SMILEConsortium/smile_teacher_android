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
import org.smilec.smile.bu.Constants;
import org.smilec.smile.bu.SmilePlugServerManager;
import org.smilec.smile.domain.CurrentMessageStatus;
import org.smilec.smile.domain.Question;
import org.smilec.smile.domain.Results;
import org.smilec.smile.ui.GeneralActivity;
import org.smilec.smile.ui.widget.checkbox.InertCheckBox;
import org.smilec.smile.util.ActivityUtil;
import org.smilec.smile.util.CloseClickListenerUtil;
import org.smilec.smile.util.ImageLoader;

import android.accounts.NetworkErrorException;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class IQSetPreviewAdapter extends ArrayAdapter<Question> {
    
	private Context context;

    public IQSetPreviewAdapter(Context context, List<Question> items) {
        super(context, android.R.layout.simple_list_item_multiple_choice, items);

        this.context = context;
    }

    // For each position (<=> question in the session), we prepare the row
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Question question = super.getItem(position);

        if (convertView == null) {
            Context context = getContext();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.preview_iqset_item, parent, false);
        }

        TextView tvQuestion = (TextView) convertView.findViewById(R.id.tv_question);
        if (!question.getQuestion().equals("")) { tvQuestion.setText(question.getQuestion()); }
        else		 							{ tvQuestion.setVisibility(View.GONE); }

        TextView tva1 = (TextView) convertView.findViewById(R.id.tv_alternative1);
        if (!question.getOption1().equals("")) 
        	{ tva1.setText(context.getString(R.string.alternative1) + " " + question.getOption1()); }
        else 
        	{ tva1.setVisibility(View.GONE); }

        TextView tva2 = (TextView) convertView.findViewById(R.id.tv_alternative2);
        if (!question.getOption1().equals("")) 
        	{ tva2.setText(context.getString(R.string.alternative2) + " " + question.getOption2()); }
        else 
        	{ tva2.setVisibility(View.GONE); }

        TextView tva3 = (TextView) convertView.findViewById(R.id.tv_alternative3);
        if (!question.getOption1().equals("")) 
        	{ tva3.setText(context.getString(R.string.alternative3) + " " + question.getOption3()); } 
        else 
        	{ tva3.setVisibility(View.GONE); }

        TextView tva4 = (TextView) convertView.findViewById(R.id.tv_alternative4);
        if (!question.getOption1().equals("")) 
        	{ tva4.setText(context.getString(R.string.alternative4) + " " + question.getOption4()); } 
        else 
        	{ tva4.setVisibility(View.GONE); }

        return convertView;
    }
}
