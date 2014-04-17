package com.tabibyab;

import com.example.tabibyab.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class AddClinicActivity extends Activity {
	

	public static int LOCATION_REQUEST;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_clinic);
		
		
		Spinner spinner = (Spinner) findViewById(R.id.spinner);
		
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.clinic_types, R.layout.dropdown_item);
	
		spinner.setAdapter(adapter);
		
//		OnItemSelectedListener oisl = new OnItemSelectedListener() {
//			
//			public void onNothingSelected(AdapterView<?> parent) {
//			}
//
//			@Override
//			public void onItemSelected(AdapterView<?> parent, View view,
//					int pos, long id) {
//				
//			}
//		};
//		spinner.setOnItemSelectedListener(oisl);
		
		Button button = (Button) findViewById(R.id.select_point_button);
		
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
			}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	
	

}
