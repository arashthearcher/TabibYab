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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class AddClinicActivity extends Activity {
	

	
	
	private ProgressDialog pDialog;
	public static int LOCATION_REQUEST;
	private Coordinate selectedLocation = null ;
	private Spinner spinnerClinicType = null ;
	private Spinner spinnerSpecialtyType = null ;
	private Spinner spinnerSpecialityLevelType = null ;
	private TextView nameTextView = null ;
	private TextView phoneTextView = null ;
	private TextView addressTextView = null ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_clinic);
		nameTextView = (TextView) findViewById(R.id.name_add_clinic);
		phoneTextView = (TextView) findViewById(R.id.phone_add_clinic);
		addressTextView = (TextView) findViewById(R.id.addreess_add_clinic);
		spinnerClinicType = (Spinner) findViewById(R.id.clinicType);
		spinnerSpecialtyType = (Spinner) findViewById(R.id.specialityTypes);
		spinnerSpecialityLevelType = (Spinner) findViewById(R.id.levelSpecialityTypes);
		ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
				this, R.array.clinic_types, R.layout.dropdown_item);
		spinnerClinicType.setAdapter(adapter1);
		ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
				this, R.array.speciality_types, R.layout.dropdown_item);
		spinnerSpecialtyType.setAdapter(adapter2);
		ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(
				this, R.array.speciality_level_types, R.layout.dropdown_item);
		spinnerSpecialityLevelType.setAdapter(adapter3);
		Button button = (Button) findViewById(R.id.add_point_on_map);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent selectLocationIntent = new Intent(AddClinicActivity.this, SelectLocationActivity.class);
				startActivityForResult(selectLocationIntent,LOCATION_REQUEST);
				
				}
		});
		
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == LOCATION_REQUEST)
		{
			if(resultCode == RESULT_OK)
			{
				String lat = data.getExtras().getString("Lat");
				String lng = data.getExtras().getString("Lng");
				Log.i("Lat", lat);
				Log.i("Lng", lng);
				double latd = Double.parseDouble(lat);
				double lngd = Double.parseDouble(lng);
				selectedLocation = new Coordinate(lngd, latd);
			}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_clinic_menu, menu);
		return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.select_ok:
			(new AddClinic()).execute();
			return true;
			
			
		case R.id.select_cancel:
			
				Intent returnResultIntent = new Intent();
				setResult(Activity.RESULT_CANCELED, returnResultIntent);
				finish();
			
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	
	
	private class AddClinic extends AsyncTask<Void, Void, Void> {
		 
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(AddClinicActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
 
        }
 
        @Override
        protected Void doInBackground(Void... arg0) {
        	
        	
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            
            ArrayList<DetailNameValuePair> params = new ArrayList<DetailNameValuePair>();
            
            params.add(new DetailNameValuePair(TAGS.TAG_NAME, nameTextView.getText().toString()));
            params.add(new DetailNameValuePair(TAGS.TAG_PHONE_NUMBERS, phoneTextView.getText().toString()));
            params.add(new DetailNameValuePair(TAGS.TAG_ADDRESS, addressTextView.getText().toString()));
            params.add(new DetailNameValuePair(TAGS.TAG_COORDINATES, selectedLocation.toString()));
            params.add(new DetailNameValuePair(TAGS.TAG_TYPE, spinnerClinicType.getSelectedItem().toString()));
            params.add(new DetailNameValuePair(TAGS.TAG_SPECIALITY, spinnerSpecialtyType.getSelectedItem().toString()));
            params.add(new DetailNameValuePair(TAGS.TAG_SPECIALITY_LEVEL, spinnerSpecialityLevelType.getSelectedItem().toString()));
            
            sh.makeServiceCall(URLs.url_list_doctor, ServiceHandler.POST,(List) params);
            
            
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
