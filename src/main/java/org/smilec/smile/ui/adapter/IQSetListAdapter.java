package org.smilec.smile.ui.adapter;

import java.util.Collection;
import java.util.List;

import org.smilec.smile.R;
import org.smilec.smile.bu.Constants;
import org.smilec.smile.bu.SmilePlugServerManager;
import org.smilec.smile.domain.CurrentMessageStatus;
import org.smilec.smile.domain.IQSet;
import org.smilec.smile.domain.Question;
import org.smilec.smile.ui.GeneralActivity;
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
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

/*
 * This class is only used to fill the ListView in use_prepared_questions_dialog.xml
 * with the following values: http://<ip_smileplug>/smile/iqsets
 *  
 * */
public class IQSetListAdapter extends ArrayAdapter<IQSet> {
	
	private Context context;
	private String ip;
	
	// We load the list of iqsets to retrieve the values in getView(,,)  
	public IQSetListAdapter(Context context,List<IQSet> iqsets, String ip) {
		
		super(context, 0, iqsets);
		this.context = context;
		this.ip = ip;
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
            tvDate.setText(String.valueOf(iqset.getDate()));
            
            // Here we get the list of questions...
            String idIQSet;
            int size = 0;
            Collection<Question> questionsOfIQSet = null;
			try {
				
				idIQSet = new SmilePlugServerManager().getIdIQSetByPosition(ip, getContext(), position);
				questionsOfIQSet = new SmilePlugServerManager().getListOfQuestions(ip, getContext(), idIQSet);
				size = questionsOfIQSet.size();
			} 
			catch (NetworkErrorException e) { e.printStackTrace(); }
            
			// ...to count the number of questions 
            TextView tvSize = (TextView) convertView.findViewById(R.id.tv_size);
            if(size < 10) tvSize.setText("0"+String.valueOf(questionsOfIQSet.size()));
            else		  tvSize.setText(String.valueOf(questionsOfIQSet.size()));
            
            // ...and get values for the preview if listener is called
            ImageView ivDetails = (ImageView) convertView.findViewById(R.id.iv_details);
            iqset.setQuestions(questionsOfIQSet);
        	ivDetails.setOnClickListener(new OpenIQSetDetailsListener(iqset));
        }

        return convertView;
	}
	
	/**
     * Listener called to display the preview of an IQSet 
     */
    private class OpenIQSetDetailsListener implements OnClickListener {

        private IQSet iqsetToPreview;

        public OpenIQSetDetailsListener(IQSet iqset) {
            this.iqsetToPreview = iqset;
        }

        @Override
        public void onClick(View v) {
            
        	// Preparing "Details" view
        	Dialog detailsDialog = new Dialog(context, R.style.Dialog);
            detailsDialog.setContentView(R.layout.iqset_details);
            Display displaySize = ActivityUtil.getDisplaySize(getContext());
            detailsDialog.getWindow().setLayout(displaySize.getWidth(), displaySize.getHeight());
            detailsDialog.show();

            // Preparing the values in the "Details" view
            loadDetailOfIQSet(detailsDialog, iqsetToPreview);
        }
    }
    
/**
 * Preparing the Dialog to show a preview of IQSet
 */
private void loadDetailOfIQSet(final Dialog detailsDialog, IQSet iqsetToPreview) {
    	
    	ImageButton btClose = (ImageButton) detailsDialog.findViewById(R.id.bt_close);
    	btClose.setOnClickListener(new CloseClickListenerUtil(detailsDialog));
    	
        TextView tvTitle = (TextView) detailsDialog.findViewById(R.id.tv_title);
        tvTitle.setText(iqsetToPreview.getSessionTitle());

        ListView lvQuestionsPreviewed = (ListView) detailsDialog.findViewById(R.id.lv_questions_preview);
        ArrayAdapter<Question> data = new IQSetPreviewAdapter(context, (List<Question>) iqsetToPreview.getQuestions());
        lvQuestionsPreviewed.setAdapter(data);
    }
}
