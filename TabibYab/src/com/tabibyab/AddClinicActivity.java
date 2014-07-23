package com.tabibyab;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.QuickContactBadge;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class AddClinicActivity extends Activity {
	private ProgressDialog pDialog;
	public static int LOCATION_REQUEST = 0;
	private static final int SELECT_PICTURE = 1;
	private Coordinate selectedLocation = null;
	private Spinner spinnerClinicType = null;
	private Spinner spinnerSpecialtyType = null;
	private Spinner spinnerSpecialityLevelType = null;
	private TextView nameTextView = null;
	private TextView addressTextView = null;
	private ArrayList<EditText> phoneTextViews = new ArrayList<EditText>();
	private String selectedImagePath;
	private QuickContactBadge profileImg;
	private ImageButton addPhoneButton, removePhoneButton;
	
	private LinearLayout phoneNumbersLayout;
	private ArrayList<EditText> insurancesTextViews = new ArrayList<EditText>();
	private LinearLayout insurancesLayout;
	private ImageButton addInsuranceButton, removeInsuranceButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_clinic);
		
		
		nameTextView = (TextView) findViewById(R.id.name_add_clinic);
		phoneTextViews.add((EditText) findViewById(R.id.phone_add_clinic));
		addressTextView = (TextView) findViewById(R.id.addreess_add_clinic);
		spinnerClinicType = (Spinner) findViewById(R.id.clinicType);
		spinnerSpecialtyType = (Spinner) findViewById(R.id.specialityTypes);
		spinnerSpecialityLevelType = (Spinner) findViewById(R.id.levelSpecialityTypes);
		ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
				this, R.array.clinic_types, R.layout.dropdown_item);
		spinnerClinicType.setAdapter(adapter1);
		ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
				this, R.array.speciality_types_add_clinic, R.layout.dropdown_item);
		spinnerSpecialtyType.setAdapter(adapter2);
		ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(
				this, R.array.speciality_level_types, R.layout.dropdown_item);
		spinnerSpecialityLevelType.setAdapter(adapter3);
		
		
		spinnerSpecialityLevelType.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				// TODO Auto-generated method stub
				
				if (pos == 0)
				{
					spinnerSpecialtyType.setSelection(0);
					spinnerSpecialtyType.setEnabled(false);
					
				}
				else
				{
					spinnerSpecialtyType.setEnabled(true);
				}
				
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		
		
		
		
		Button add_profile = (Button) findViewById(R.id.add_profile_picture);
		profileImg = (QuickContactBadge) findViewById(R.id.image_Contact);
		add_profile.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(
						Intent.createChooser(intent, "Select Picture"),
						SELECT_PICTURE);
			}
		});
		Button add_point = (Button) findViewById(R.id.add_point_on_map);
		add_point.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent selectLocationIntent = new Intent(
						AddClinicActivity.this, SelectLocationActivity.class);
				startActivityForResult(selectLocationIntent, LOCATION_REQUEST);
			}
		});
		
		
		phoneNumbersLayout = (LinearLayout) findViewById(R.id.phone_number_linear_layout);
		addPhoneButton = (ImageButton) findViewById(R.id.add_phone_plus_icon);
		addPhoneButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				EditText tv = new EditText(getApplicationContext());
				tv.setLayoutParams(phoneTextViews.get(0).getLayoutParams());
				tv.setInputType(InputType.TYPE_CLASS_PHONE);
				tv.setEms(10);
				tv.setBackgroundDrawable(phoneTextViews.get(0).getBackground());
				tv.setHighlightColor(Color.BLACK);
				tv.setTextColor(Color.BLACK);
				phoneTextViews.add(tv);
				phoneNumbersLayout.addView(tv);
			}
		});
		
		removePhoneButton = (ImageButton) findViewById(R.id.remove_phone_button);
		removePhoneButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				if(phoneTextViews.size() > 1)
				{
					phoneNumbersLayout.removeView(phoneTextViews.get(phoneTextViews.size()-1));
					phoneTextViews.remove(phoneTextViews.size()-1);
				}
				
			}
		}); 
		
		
		
		
		
		
		insurancesTextViews.add((EditText) findViewById(R.id.add_clinic_insurance));
		insurancesLayout = (LinearLayout) findViewById(R.id.insurances_layout);
		addInsuranceButton = (ImageButton) findViewById(R.id.add_insurance_add_clinic);
		addInsuranceButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				EditText tv = new EditText(getApplicationContext());
				tv.setLayoutParams(insurancesTextViews.get(0).getLayoutParams());
				tv.setEms(10);
				tv.setBackgroundDrawable(insurancesTextViews.get(0).getBackground());
				tv.setHighlightColor(Color.BLACK);
				tv.setTextColor(Color.BLACK);
				insurancesTextViews.add(tv);
				insurancesLayout.addView(tv);
			}
		});
		
		removeInsuranceButton = (ImageButton) findViewById(R.id.remove_insurance_add_clinic);
		removeInsuranceButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				if(insurancesTextViews.size() > 1)
				{
					insurancesLayout.removeView(insurancesTextViews.get(insurancesTextViews.size()-1));
					insurancesTextViews.remove(insurancesTextViews.size()-1);
				}
				
			}
		}); 
		
		
		
		
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == LOCATION_REQUEST) {
				String lat = data.getExtras().getString("Lat");
				String lng = data.getExtras().getString("Lng");
				Log.i("Lat", lat);
				Log.i("Lng", lng);
				double latd = Double.parseDouble(lat);
				double lngd = Double.parseDouble(lng);
				selectedLocation = new Coordinate(lngd, latd);
			}
			if (requestCode == SELECT_PICTURE) {
				Uri selectedImageUri = data.getData();
				selectedImagePath = getPath(selectedImageUri);
				profileImg.setImageURI(selectedImageUri);
			}
		}
	}

	@SuppressWarnings("deprecation")
	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
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
			// Sending Clinic Attributes
			ServiceHandler sh = new ServiceHandler();
			ArrayList<DetailNameValuePair> params = new ArrayList<DetailNameValuePair>();
			params.add(new DetailNameValuePair(TAGS.TAG_NAME, nameTextView
					.getText().toString()));
			params.add(new DetailNameValuePair(TAGS.TAG_ADDRESS,
					addressTextView.getText().toString()));
			params.add(new DetailNameValuePair(TAGS.TAG_COORDINATES,
					selectedLocation.toString()));
			params.add(new DetailNameValuePair(TAGS.TAG_TYPE, spinnerClinicType
					.getSelectedItem().toString()));
			params.add(new DetailNameValuePair(TAGS.TAG_SPECIALITY,
					spinnerSpecialtyType.getSelectedItem().toString()));
			params.add(new DetailNameValuePair(TAGS.TAG_SPECIALITY_LEVEL,
					spinnerSpecialityLevelType.getSelectedItem().toString()));
			String response = sh.makeServiceCall(URLs.url_list_doctor,
					ServiceHandler.POST, (List) params);
			
			
			Clinic doctor_new = sh.parseDoctorInfo(response);
			
			
			// Sending Tel Numbers
			ArrayList<DetailNameValuePair> phone_params = new ArrayList<DetailNameValuePair>();
			
			for (EditText phoneTextView : phoneTextViews) {
				phone_params.add(new DetailNameValuePair(TAGS.TAG_TEL,
						phoneTextView.getText().toString()));
				phone_params.add(new DetailNameValuePair(TAGS.TAG_CLINIC, Integer
						.toString(doctor_new.id)));
				phone_params.add(new DetailNameValuePair(TAGS.TAG_TITLE, "phone"));
				sh.makeServiceCall(URLs.url_doctor_phonenumber,
						ServiceHandler.POST, (List) phone_params);
				phone_params.clear();
			}
			
			
			// Sending Insurances
			ArrayList<DetailNameValuePair> insurance_params = new ArrayList<DetailNameValuePair>();
			
			for (EditText insuranceTextView : insurancesTextViews) {
				insurance_params.add(new DetailNameValuePair(TAGS.TAG_TITLE,
						insuranceTextView.getText().toString()));
				insurance_params.add(new DetailNameValuePair(TAGS.TAG_CLINIC, Integer
						.toString(doctor_new.id)));
				sh.makeServiceCall(URLs.url_list_insurances,
						ServiceHandler.POST, (List) insurance_params);
				insurance_params.clear();
			}
			
			
			
			

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
