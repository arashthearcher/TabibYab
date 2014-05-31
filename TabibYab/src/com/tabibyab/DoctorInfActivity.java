package com.tabibyab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.QuickContactBadge;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

public class DoctorInfActivity extends Activity {

	Clinic doctor;
	
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
					URLs.url_doctor_info,
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
		TextView doc_name =(TextView) findViewById(R.id.doctor_name);
		RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
		TextView doc_speciality =(TextView) findViewById(R.id.docSpeciality);
		QuickContactBadge contact = (QuickContactBadge) findViewById(R.id.imgContact);
		TextView phone =(TextView) findViewById(R.id.phone);
		TextView days =(TextView) findViewById(R.id.days);
		TextView waiting_times =(TextView) findViewById(R.id.waiting_time);
		TextView address =(TextView) findViewById(R.id.address);
		TextView operating_hours =(TextView) findViewById(R.id.operating_hour);
		doc_name.setText(doctor.name);
		ratingBar.setRating(Float.parseFloat(doctor.getRating()));
		doc_speciality.setText(doctor.speciality);
		contact.setImageBitmap(doctor.profilePic);
		String phones="";
		for (int i = 0; i < doctor.phoneNumbers.size(); i++) {
			phones += doctor.phoneNumbers.get(i).tel+" ";
		}
		phone.setText(phones);
		days.setText(doctor.appointmentOnly);
		waiting_times.setText(doctor.appointmentOnly);
		address.setText(doctor.address);
		String hours="";
		for (int i = 0; i < doctor.operatingHours.size(); i++) {
			hours += doctor.operatingHours.get(i).from+"-"+doctor.operatingHours.get(i).to;
		}
		operating_hours.setText(hours);
		Button cmnt_button = (Button) findViewById(R.id.cmntBtn);
		cmnt_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent comments_view = new Intent(DoctorInfActivity.this, DoctorCommentsActivity.class);
				comments_view.putExtra("doctor_id", doctor_id);
				startActivity(comments_view);
				}
		});
	}
}
