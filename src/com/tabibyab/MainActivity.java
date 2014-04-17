package com.tabibyab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


import com.example.tabibyab.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
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

public class MainActivity extends Activity implements GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener, LocationListener {
	
	private static String url = "http://192.168.1.100:8000/search/";
	
	private GoogleMap gMap ;
	private ProgressDialog pDialog;
	
    
    
 
    // Hashmap for ListView
    ArrayList<HashMap<String, String>> clinicList;
    
    
    
    private static final long ONE_MIN = 1000 * 60;
	private static final long TWO_MIN = ONE_MIN * 2;
	private static final long FIVE_MIN = ONE_MIN * 5;
	private static final long MEASURE_TIME = 1000 * 30;
	private static final long POLLING_FREQ = 1000 * 10;
	private static final long FASTES_UPDATE_FREQ = 1000 * 2;
	private static final float MIN_ACCURACY = 500.0f;
	private static final float MIN_LAST_READ_ACCURACY = 1000.0f;
	
	// Define an object that holds accuracy and frequency parameters
	LocationRequest mLocationRequest;
	private final String TAG = "LocationGetLocationActivity";
	private Location mBestReading;
	private boolean mFirstUpdate = true;
	private LocationClient mLocationClient;
	private Location mCurrentLocation;
	
	private Circle circle = null ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		gMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		gMap.setMyLocationEnabled(true);
		gMap.getUiSettings().setCompassEnabled(true);
		gMap.getUiSettings().setMyLocationButtonEnabled(true);
		gMap.getUiSettings().setRotateGesturesEnabled(true);
		
		clinicList = new ArrayList<HashMap<String, String>>();
		
   
        
        
			// Create new Location Client. This class will handle callbacks
     		mLocationClient = new LocationClient(this, this, this);

     		// Create and define the LocationRequest
     		mLocationRequest = LocationRequest.create();

     		// Use high accuracy
     		mLocationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);

     		// Update every 10 seconds
     		mLocationRequest.setInterval(POLLING_FREQ);

     		// Recieve updates no more often than every 2 seconds
     		mLocationRequest.setFastestInterval(FASTES_UPDATE_FREQ);        
		
		
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
			Intent addClinicIntent = new Intent(MainActivity.this, AddClinicActivity.class);
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

		// Stop updates
		mLocationClient.removeLocationUpdates(this);

		// Disconnect from LocationServices
		mLocationClient.disconnect();

		super.onStop();
	}


	// Called back when location changes

	@Override
	public void onLocationChanged(Location location) {


		// Determine whether new location is better than current best
		// estimate

		if (null == mBestReading
				|| location.getAccuracy() < mBestReading.getAccuracy()) {

			// Update best estimate
			mBestReading = location;

			// Update display
			updateLocation(location);

			if (mBestReading.getAccuracy() < MIN_ACCURACY)
				mLocationClient.removeLocationUpdates(this);

		}
	}

	@Override
	public void onConnected(Bundle dataBundle) {

		// Get first reading. Get additional location updates if necessary

		if (servicesAvailable()) {
			// Get best last location measurement meeting criteria
			mBestReading = bestLastKnownLocation(MIN_LAST_READ_ACCURACY,
					FIVE_MIN);

			// Display last reading information
			if (null != mBestReading) {

				updateLocation(mBestReading);

			} else {

				Log.i(TAG, "No Initial Reading Available");

			}

			if (null == mBestReading
					|| mBestReading.getAccuracy() > MIN_LAST_READ_ACCURACY
					|| mBestReading.getTime() < System.currentTimeMillis()
							- TWO_MIN) {

				mLocationClient.requestLocationUpdates(mLocationRequest, this);
				
				// Schedule a runnable to unregister location listeners
				Executors.newScheduledThreadPool(1).schedule(new Runnable() {

					@Override
					public void run() {

						mLocationClient.removeLocationUpdates(MainActivity.this);

					}
				}, MEASURE_TIME, TimeUnit.MILLISECONDS);
			}

			}
		}

	// Get the last known location from all providers
	// return best reading is as accurate as minAccuracy and
	// was taken no longer then minTime milliseconds ago

	private Location bestLastKnownLocation(float minAccuracy, long minTime) {

		Location bestResult = null;
		float bestAccuracy = Float.MAX_VALUE;
		long bestTime = Long.MIN_VALUE;

		// Get the best most recent location currently available
		mCurrentLocation = mLocationClient.getLastLocation();

		if (mCurrentLocation != null) {

			float accuracy = mCurrentLocation.getAccuracy();
			long time = mCurrentLocation.getTime();

			if (accuracy < bestAccuracy) {

				bestResult = mCurrentLocation;
				bestAccuracy = accuracy;
				bestTime = time;

			}
		}

		// Return best reading or null
		if (bestAccuracy > minAccuracy || bestTime < minTime) {
			return null;
		} else {
			return bestResult;
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
	
	private void updateLocation(Location location) 
	{

		// Calling async task to get json
        new GetClinics().execute();
       
        if (circle != null)
        {
        	circle.setVisible(false);
        	circle.remove();
        	
        }
        circle = gMap.addCircle(new CircleOptions()
        .center(new LatLng(mBestReading.getLatitude(),mBestReading.getLongitude()))
        .radius(10000)
        .strokeWidth(3)
        .strokeColor(Color.BLUE));


	}
	
	public void showClinicsOnMap(ArrayList<HashMap<String, String>> clinicList)
	{
        if (gMap != null) {
        	
        	Coordinate c = null ;
        	for (int i = 0; i < clinicList.size(); i++) {
        		
				c = new Coordinate(clinicList.get(i).get(TAGS.TAG_COORDINATES));
				gMap.addMarker(new MarkerOptions().position(
    					new LatLng(c.lat, c.lng)).title(clinicList.get(i).get(TAGS.TAG_NAME)));
				if (c != null)
					gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(c.lat,c.lng), 0));
			}
        	
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
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
 
            Log.d("Response: ", "> " + jsonStr);
            
            clinicList = sh.parseClinicList(jsonStr);
 
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