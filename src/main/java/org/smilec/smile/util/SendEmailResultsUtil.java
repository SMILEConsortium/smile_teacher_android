package org.smilec.smile.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.smilec.smile.R;
import org.smilec.smile.bu.BoardManager;
import org.smilec.smile.bu.Constants;
import org.smilec.smile.domain.Board;
import org.smilec.smile.domain.Question;
import org.smilec.smile.domain.Results;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class SendEmailResultsUtil {

	@SuppressWarnings("deprecation")
	@SuppressLint("UseSparseArrays")
	public static void send(Board board, final String ip, final Activity activity) {
		final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND_MULTIPLE);

		final StringBuilder body = new StringBuilder();
		body.append("\nQuestions:");
		body.append("\n================================");

		final Map<Integer, String> listImages = new HashMap<Integer, String>();
		Collection<Question> questions = board.getQuestions();
		int questionNumber = 1;
		for (Question question : questions) {
			body.append("\n\n(" + questionNumber + ") Question: "
					+ question.getQuestion());
			body.append("\n\nAlternative 1: " + question.getOption1());
			body.append("\nAlternative 2: " + question.getOption2());
			body.append("\nAlternative 3: " + question.getOption3());
			body.append("\nAlternative 4: " + question.getOption4());
			body.append("\n\nCorrect Answer: " + question.getAnswer());
			if (StringUtils.isNotBlank(question.getImageUrl())) {
				listImages.put(questionNumber, question.getImageUrl());
				body.append("\n\nAttach image: " + "question_" + questionNumber
						+ ".jpg");
			}
			body.append("\n\n================================");
			questionNumber++;
		}

		try {

			Results retrieveResults = new BoardManager().retrieveResults(ip,
					activity);
			String sBestScoredStudentNames = retrieveResults.getBestScoredStudentNames();
			JSONArray bestScoredStudentNames = new JSONArray(sBestScoredStudentNames == null ? ""
							: sBestScoredStudentNames);

			body.append("\n\nTop Scorer:");
			body.append("\n\nStudent: " + bestScoredStudentNames.join(", ").replaceAll("\"", ""));
			body.append("\nHighest Rating: " + retrieveResults.getWinnerRating());
			body.append("\n\n================================");
			body.append("\n\n");

		} catch (Exception e) {
			Log.e(Constants.LOG_CATEGORY, "Error: ", e);
		}

		final Dialog previewDialog = new Dialog(activity, R.style.Dialog);
		previewDialog.setContentView(R.layout.preview_send_email);
		Display displaySize = ActivityUtil.getDisplaySize(activity
				.getApplicationContext());
		previewDialog.getWindow().setLayout(displaySize.getWidth(),
				displaySize.getHeight());

		final EditText etEmails = (EditText) previewDialog.findViewById(R.id.et_email);
		final EditText etSubject = (EditText) previewDialog.findViewById(R.id.et_subject);
		TextView etBody = (TextView) previewDialog.findViewById(R.id.tv_body);
		TextView etSend = (TextView) previewDialog.findViewById(R.id.bt_send);
		final ImageButton btClose = (ImageButton) previewDialog
				.findViewById(R.id.bt_close);

		etBody.setText(body.toString());
		etSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String emails = etEmails.getText().toString();
				String[] arrayEmails = emails.split(",");

				if (StringUtils.isNotBlank(emails)) {

					emailIntent.setType("plain/text");
					emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, arrayEmails);
					emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, etSubject.getText().toString());
					emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, body.toString());
					if (!listImages.isEmpty()) {
						emailIntent.setType("application/image");
						ArrayList<Uri> uris = new ArrayList<Uri>();
						for (Map.Entry<Integer, String> entry : listImages
								.entrySet()) {
							Uri uri = getImageUrl(Constants.HTTP + ip + entry.getValue(), entry.getKey());
							uris.add(uri);
						}
						emailIntent.putExtra(Intent.EXTRA_STREAM, uris);
					}
					activity.startActivity(emailIntent);
					previewDialog.dismiss();

				} else {
					ActivityUtil.showLongToast(activity, "Enter an e-mail!");
				}

			}
		});
		btClose.setOnClickListener(new CloseClickListenerUtil(previewDialog));

		previewDialog.show();

	}

	private static Uri getImageUrl(String urlPath, Integer integer) {
		try {
			File rootSdDirectory = Environment.getExternalStorageDirectory();

			File pictureFile = new File(rootSdDirectory, "question_" + integer + ".jpg");
			if (pictureFile.exists()) {
				pictureFile.delete();
			}
			pictureFile.createNewFile();
			byte[] loadBitmap = ImageLoader.loadBitmap(urlPath);
			IOUtil.saveBytes(pictureFile, loadBitmap);
			Uri pictureUri = Uri.fromFile(pictureFile);

			return pictureUri;

		} catch (Exception e) {
			Log.e(Constants.LOG_CATEGORY, "Error: ", e);
		}
		return null;
	}

}
