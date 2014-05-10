package com.tabibyab;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;


public class MainActivity extends Activity implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener{

	private GoogleMap gMap;
	private ProgressDialog pDialog;

	// Hashmap for ListView
	ArrayList<Clinic> clinicList;
	HashMap<Marker, Clinic> markerClinicMap;



	// Define an object that holds accuracy and frequency parameters
	private final String TAG = "LocationGetLocationActivity";
	private LocationClient mLocationClient;
	private Location mCurrentLocation;
	private Circle circle = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		gMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		gMap.setMyLocationEnabled(true);
		gMap.getUiSettings().setCompassEnabled(true);
		gMap.getUiSettings().setMyLocationButtonEnabled(true);
		gMap.getUiSettings().setRotateGesturesEnabled(true);

		clinicList = new ArrayList<Clinic>();
		markerClinicMap = new HashMap<Marker, Clinic>();

		// Create new Location Client. This class will handle callbacks
		mLocationClient = new LocationClient(this, this, this);
		


	}

	
	
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.addClinicButton:
			Intent addClinicIntent = new Intent(MainActivity.this,
					AddClinicActivity.class);
			startActivity(addClinicIntent);

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();

		// Connect to LocationServices
		mLocationClient.connect();
	}

	@Override
	protected void onStop() {


		// Disconnect from LocationServices
		mLocationClient.disconnect();

		super.onStop();
	}

	// Called back when location changes

	@Override
	public void onConnected(Bundle dataBundle) {

		// Get first reading. Get additional location updates if necessary
		
		mCurrentLocation = mLocationClient.getLastLocation();
		if(mCurrentLocation != null)
		{
			updateClinicList();
		}
		else
		{
			new LocationNotFoundAlertDialog().show(getFragmentManager(), "LocationNotFound");
		}


	}

	@Override
	public void onDisconnected() {

		Log.i(TAG, "Disconnected. Try again later.");

	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		Log.i(TAG, "Connection Failed. Try again later.");
	}

	private boolean servicesAvailable() {

		// Check that Google Play Services are available
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);

		// If Google Play services is available

		return (ConnectionResult.SUCCESS == resultCode);

	}

	private void updateClinicList() {

		// Calling async task to get json
		new GetClinics().execute();

		if (circle != null) {
			circle.setVisible(false);
			circle.remove();

		}
		circle = gMap.addCircle(new CircleOptions()
				.center(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation
						.getLongitude())).radius(10000).strokeWidth(3)
				.strokeColor(Color.BLUE));

	}

	public void showClinicsOnMap(final ArrayList<Clinic> clinicList) {
		if (gMap != null) {


			for (int i = 0; i < clinicList.size(); i++) {

				Clinic clinic = clinicList.get(i);
				Marker marker = gMap.addMarker(new MarkerOptions().position(
						new LatLng(clinic.getCoordinates().getLat(), clinic.getCoordinates().getLng())).title(
						clinic.getName()));
				
				markerClinicMap.put(marker, clinic);
				
				
				if (mCurrentLocation != null)
					gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
							new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()), 11));
			}
			gMap.setInfoWindowAdapter(new InfoWindowAdapter() {
	            @Override
	            public View getInfoWindow(Marker arg0) {
	                return null;
	            }
	            @Override
	            public View getInfoContents(Marker marker) {
	                View v = getLayoutInflater().inflate(R.layout.info_window_layout, null);
	                Clinic clinic = markerClinicMap.get(marker);
	                TextView tvName = (TextView) v.findViewById(R.id.tv_name);
	                TextView tvType = (TextView) v.findViewById(R.id.tv_type);
	                RatingBar rating = (RatingBar)v.findViewById(R.id.rating);
	                rating.setRating(4);
	                tvName.setText( clinic.name);
	                tvType.setText( clinic.type);
	                return v;
	            }
	        });
			gMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

				@Override
				public void onInfoWindowClick(Marker marker) {
						Intent DoctorInfintent = new Intent(MainActivity.this, DoctorInfActivity.class);
						Clinic clinic = markerClinicMap.get(marker);
						DoctorInfintent.putExtra("doctor_id", clinic.getId());
						startActivity(DoctorInfintent);
				}
			});


		}
	}

	private class GetClinics extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected Void doInBackground(Void... arg0) {

			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();

			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(
					URLs.url_list_doctor,
					ServiceHandler.GET);

			Log.d("Response: ", "> " + jsonStr);

			clinicList = sh.parseClinics(jsonStr);

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			if (pDialog.isShowing())
				pDialog.dismiss();

			MainActivity.this.showClinicsOnMap(clinicList);

		}

	}

}