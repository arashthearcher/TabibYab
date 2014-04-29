package com.tabibyab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.Toast;

public class DoctorCommentsActivity extends Activity implements
		OnItemClickListener {

	ArrayList<String> commentsList;
	private int doctor_id;
	private ProgressDialog pDialog;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent CommentDocintent = getIntent();
		doctor_id = CommentDocintent.getIntExtra("doctor_id", 1);
		new GetComments().execute();
		setContentView(R.layout.doctor_comments);
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position,
			long id) {
		Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
				Toast.LENGTH_SHORT).show();
	}

	private class GetComments extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(DoctorCommentsActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			ServiceHandler sh = new ServiceHandler();
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("id", Integer
					.toString(doctor_id)));
			String jsonStr = sh.makeServiceCall(URLs.url_doctor_comments,
					ServiceHandler.GET, nameValuePairs);
			commentsList = sh.parseCommentsList(jsonStr);
			Log.d("Response: ", "> " + commentsList.get(0));
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			if (pDialog.isShowing())
				pDialog.dismiss();
			DoctorCommentsActivity.this.showComments();
		}

	}

	public void showComments() {
		ListView listComments;
		listComments = (ListView) findViewById(R.id.comments_list);
		listComments.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, commentsList));
		listComments.setOnItemClickListener(this);
	}

}
