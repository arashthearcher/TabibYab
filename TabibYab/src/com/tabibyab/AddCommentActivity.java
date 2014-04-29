package com.tabibyab;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class AddCommentActivity extends Activity {
	private int doctor_id;
	private ProgressDialog pDialog;
	private EditText name = null;
	private EditText commText = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_comment);
		Intent AddCommentintent = getIntent();
		doctor_id = AddCommentintent.getIntExtra("doctor_id", 1);
		name = (EditText) findViewById(R.id.commenter_name);
		commText = (EditText) findViewById(R.id.comment_text);
		Button button = (Button) findViewById(R.id.add_main_comment);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new AddComment().execute();
				Intent backComments = new Intent(
						AddCommentActivity.this, DoctorCommentsActivity.class);
				backComments.putExtra("doctor_id", doctor_id);
				startActivity(backComments);
			}
		});
	}

	private class AddComment extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(AddCommentActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			ServiceHandler sh = new ServiceHandler();
			ArrayList<DetailNameValuePair> params = new ArrayList<DetailNameValuePair>();
			params.add(new DetailNameValuePair(TAGS.TAG_NAME, name.getText()
					.toString()));
			params.add(new DetailNameValuePair(TAGS.TAG_COMMENT, commText
					.getText().toString()));
			params.add(new DetailNameValuePair(TAGS.TAG_CLINIC, Integer.toString(doctor_id)));
			sh.makeServiceCall(URLs.url_doctor_comments, ServiceHandler.POST,
					(List) params);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			if (pDialog.isShowing())
				pDialog.dismiss();
			finish();
		}

	}

}
