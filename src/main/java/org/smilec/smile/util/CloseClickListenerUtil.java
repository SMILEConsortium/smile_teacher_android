package org.smilec.smile.util;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;

public class CloseClickListenerUtil implements OnClickListener {
	
	private Object obj;
	
	public CloseClickListenerUtil(Object obj) {
		this.obj = obj;
	}

	@Override
	public void onClick(View v) {
		if (obj instanceof Activity) {
			((Activity) obj).onBackPressed();			
		} else if (obj instanceof Dialog) {
			((Dialog) obj).dismiss();			
		}
	}

}