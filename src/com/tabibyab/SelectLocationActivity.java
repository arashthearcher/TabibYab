package com.tabibyab;

import com.example.tabibyab.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class SelectLocationActivity extends Activity {

	private GoogleMap gMap ;
	private LatLng selectedLocation;
	private Marker selectedMarker;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_location);
		gMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		gMap.setMyLocationEnabled(true);
		gMap.getUiSettings().setCompassEnabled(true);
		gMap.getUiSettings().setMyLocationButtonEnabled(true);
		gMap.getUiSettings().setRotateGesturesEnabled(true);
		OnMapLongClickListener omlcl = new OnMapLongClickListener() {
			
			@Override
			public void onMapLongClick(LatLng pnt) {
				// TODO Auto-generated method stub
				if (selectedMarker != null)
					selectedMarker.remove();
				selectedMarker = gMap.addMarker(new MarkerOptions().position(pnt));
				selectedLocation = pnt;
			}
		};
		gMap.setOnMapLongClickListener(omlcl);
	}
	
	
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.select_location, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.select_ok:
			if (selectedLocation != null)
			{
				Intent returnResultIntent = new Intent();
				returnResultIntent.putExtra("Lat", selectedLocation.latitude + "");
				returnResultIntent.putExtra("Lng", selectedLocation.longitude + "");
				setResult(Activity.RESULT_OK, returnResultIntent);
				finish();
				
			}
		case R.id.select_cancel:
			
				Intent returnResultIntent = new Intent();
				setResult(Activity.RESULT_CANCELED, returnResultIntent);
				finish();
			
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	
	
	
}
