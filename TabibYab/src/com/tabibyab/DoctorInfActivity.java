package com.tabibyab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;


import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.QuickContactBadge;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

public class DoctorInfActivity extends Activity {

	Clinic doctor;
	private boolean isFaved = false;
	private ProgressDialog pDialog;
	private int doctor_id;
	ImageButton favoriteButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		new GetDoctor().execute();
		Intent DoctorInfintent = getIntent();
		doctor_id = DoctorInfintent.getIntExtra("doctor_id", 1);
		setContentView(R.layout.doctor_info);
		
		favoriteButton = (ImageButton) findViewById(R.id.favorite_button);
		
		// Create a new DatabaseHelper
		DatabaseOpenHelper mDbHelper = new DatabaseOpenHelper(DoctorInfActivity.this);

		// Get the underlying database for writing
		SQLiteDatabase mDB = mDbHelper.getWritableDatabase();
		
		Cursor c = mDB.query(mDbHelper.TABLE_NAME, new String[]{mDbHelper._ID}, mDbHelper._ID+"=?", new String[]{String.valueOf(doctor_id)}, null, null, null);
		
		if (c.getCount() > 0)
		{
			Log.d("DoctorInfActivity", "isfaved");
			isFaved = true;
			favoriteButton.setImageResource(R.drawable.favorites_icon);
		}
		
		c.close();
		mDB.close();
		mDbHelper.close();
		
		favoriteButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
			
				if(isFaved)
				{
					// Create a new DatabaseHelper
					DatabaseOpenHelper mDbHelper = new DatabaseOpenHelper(DoctorInfActivity.this);

					// Get the underlying database for writing
					SQLiteDatabase mDB = mDbHelper.getWritableDatabase();
					
					mDB.delete(DatabaseOpenHelper.TABLE_NAME,
							DatabaseOpenHelper._ID + "=?",
							new String[] { String.valueOf(doctor_id) });
					mDB.close();
					mDbHelper.close();
					favoriteButton.setImageResource(R.drawable.favorites_icon_gray);
				}
				else
				{
					
				
					// Create a new DatabaseHelper
					DatabaseOpenHelper mDbHelper = new DatabaseOpenHelper(DoctorInfActivity.this);
	
					// Get the underlying database for writing
					SQLiteDatabase mDB = mDbHelper.getWritableDatabase();
					
					ContentValues values = new ContentValues();
	
					values.put(mDbHelper._ID, doctor_id);
					values.put(TAGS.TAG_NAME, doctor.getName());
					values.put(TAGS.TAG_LONGITUDE, doctor.getCoordinates().getLng());
					values.put(TAGS.TAG_LATITUDE, doctor.getCoordinates().getLat());
					values.put(TAGS.TAG_ADDRESS, doctor.address);
					values.put(TAGS.TAG_TEL, doctor.getPhoneNumbersInString());
					values.put(TAGS.TAG_OPERATING_HOURS, doctor.getOperatingHoursInString());
					values.put(TAGS.TAG_RATING, doctor.getRating());
					values.put(TAGS.TAG_INSURANCES, doctor.getInsurancesInString());
					values.put(TAGS.TAG_SPECIALITY, doctor.getSpeciality());
					values.put(TAGS.TAG_SPECIALITY_LEVEL, doctor.getSpecialityLevel());
					values.put(TAGS.TAG_TYPE, doctor.getType());
					values.put(TAGS.TAG_WEBSITE_ADDRESS, doctor.getWebsiteAddress());
					values.put(TAGS.TAG_DESCRIPTION, doctor.getDescription());
					mDB.insert(DatabaseOpenHelper.TABLE_NAME, null, values);
					mDB.close();
					mDbHelper.close();
					favoriteButton.setImageResource(R.drawable.favorites_icon);
				}
			}
		});
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.doctor_detail_activity_menu, menu);
	    return true;

	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.addClinicButton:
			Intent addClinicIntent = new Intent(DoctorInfActivity.this,
					AddClinicActivity.class);
			startActivity(addClinicIntent);
			return true;
		case R.id.clinicMapView:
			Intent clinicMapIntent = new Intent(DoctorInfActivity.this,
					MainActivity.class);
			clinicMapIntent.putExtra("useExistingClinicList", true);
			ArrayList<Clinic> clinicList = new ArrayList<Clinic>();
			clinicList.add(doctor);
			((MyApplication) getApplicationContext()).setClinicList(clinicList);
			startActivity(clinicMapIntent);
			return true;
		case R.id.action_home_list_view:
			Intent homeIntent = new Intent(DoctorInfActivity.this, MainMenuActivity.class);
			startActivity(homeIntent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
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
			phones += doctor.phoneNumbers.get(i).tel+"\n";
		}
		phone.setText(phones);
		
		
		days.setText(doctor.appointmentOnly);
		
		waiting_times.setText(doctor.appointmentOnly);
		
		address.setText(doctor.address);
		
		String hours="";
		for (int i = 0; i < doctor.operatingHours.size(); i++) {
			hours += doctor.operatingHours.get(i).from+"-"+doctor.operatingHours.get(i).to+"\n";
		}
		operating_hours.setText(hours);
		
		TextView insurances = (TextView) findViewById(R.id.insurances_doctor_detail);
		insurances.setText(doctor.getInsurancesInString());
		
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
