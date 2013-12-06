package org.smilec.smile.ui.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.smilec.smile.R;
import org.smilec.smile.domain.IQSet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/*
 * This class is only used to fill the ListView in use_prepared_questions_dialog.xml
 * with the following values: http://<ip_smileplug>/smile/iqsets
 *  
 * */
public class IQSetListAdapter extends ArrayAdapter<IQSet> {
	
	// We load the list of iqsets to retrieve the values in getView(,,)  
	public IQSetListAdapter(Context context,List<IQSet> iqsets) {
		
		super(context, 0, iqsets);
	}

	// This method will be executed for each row
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		final IQSet iqset = getItem(position);

        if (convertView == null) {
            Context context = getContext();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.use_prepared_questions_item, parent, false);
        }

        if (iqset != null) {
            TextView tvSessionTitle = (TextView) convertView.findViewById(R.id.tv_file_name);
            tvSessionTitle.setText(iqset.getSessionTitle());

            TextView tvTeacherName = (TextView) convertView.findViewById(R.id.tv_teacher_name);
            tvTeacherName.setText(iqset.getTeacherName());

            TextView tvGroupName = (TextView) convertView.findViewById(R.id.tv_group_name);
            tvGroupName.setText(String.valueOf(iqset.getGroupName()));
            
            TextView tvDate = (TextView) convertView.findViewById(R.id.tv_date);
            tvDate.setText(String.valueOf(iqset.getKey()));
        }

        return convertView;
	}
}
