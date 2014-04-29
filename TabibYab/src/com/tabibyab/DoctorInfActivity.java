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
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class DoctorInfActivity extends Activity {

	HashMap<String, String> doctor;
	
	private ProgressDialog pDialog;
	private int doctor_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		new GetDoctor().execute();
		Intent DoctorInfintent = getIntent();
		doctor_id = DoctorInfintent.getIntExtra("doctor_id", 1);
		setContentView(R.layout.doctor_info);
	}

	private class GetDoctor extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(DoctorInfActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			ServiceHandler sh = new ServiceHandler();
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			 nameValuePairs.add(new BasicNameValuePair("id", Integer.toString(doctor_id)));
			String jsonStr = sh.makeServiceCall(
					getResources().getString(R.string.url_doctor_info),
					ServiceHandler.GET,nameValuePairs);
			doctor = sh.parseDoctorInfo(jsonStr);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			if (pDialog.isShowing())
				pDialog.dismiss();
			DoctorInfActivity.this.showDoctor();
		}

	}
	public void showDoctor()
	{
		TextView text_id = new TextView(this);
		text_id = (TextView) findViewById(R.id.doctor_id);
		text_id.setText("شماره = " + Integer.toString(doctor_id)+"\nنام="+doctor.get(TAGS.TAG_NAME)+"\nنوع="+doctor.get(TAGS.TAG_TYPE));
		Button cmnt_button = (Button) findViewById(R.id.cmntBtn);
		cmnt_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent comments_view = new Intent(DoctorInfActivity.this, DoctorCommentsActivity.class);
				comments_view.putExtra("doctor_id", 1);
				startActivity(comments_view);
				}
		});
	}
}
