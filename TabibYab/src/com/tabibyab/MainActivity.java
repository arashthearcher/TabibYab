package com.tabibyab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;

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
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;


public class MainActivity extends Activity implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener,SearchFilterDialog.NoticeDialogListener{

	
	private SearchFilterDialog searchFilterDialog;
	private ProgressDialog pDialog;

	private double searchDistant;
	
	
	private GoogleMap gMap;
	String query = null ;
	// Hashmap for ListView
	ArrayList<Clinic> clinicList;
	HashMap<Marker, Clinic> markerClinicMap;



	// Define an object that holds accuracy and frequency parameters
	private final String TAG = "LocationGetLocationActivity";
	private LocationClient mLocationClient;
	private Location mCurrentLocation;
	private Circle circle = null;

	
	private boolean useExistingClinicList = false;
	
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
		
		Intent intent = getIntent();
		useExistingClinicList = intent.getBooleanExtra("useExistingClinicList", false) ;
		
		if(useExistingClinicList)
		{
			clinicList = ((MyApplication) getApplicationContext()).getClinicList();
			mCurrentLocation = ((MyApplication) getApplicationContext()).getCurrentLocation();
			this.showClinicsOnMap(clinicList);
		}
		


	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		

	    // Get the SearchView and set the searchable configuration
	    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	    SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
	    // Assumes current activity is the searchable activity
	    if (searchManager != null) {
            List<SearchableInfo> searchables = searchManager.getSearchablesInGlobalSearch();

            SearchableInfo info = searchManager.getSearchableInfo(getComponentName());
            if(info != null)
            	searchView.setSearchableInfo(info);
	    }
	    searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

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
		case R.id.clinicListView:
			Intent clinicListIntent = new Intent(MainActivity.this,
					SearchActivity.class);
			clinicListIntent.putExtra("useExistingClinicList", true);
			((MyApplication) getApplicationContext()).setClinicList(clinicList);
			startActivity(clinicListIntent);
			return true;
		case R.id.searchFilterButton:
			searchFilterDialog = new SearchFilterDialog();
			searchFilterDialog.show(getFragmentManager(), "Filter Search Result");
			
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // User touched the dialog's positive button
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
    }

	
	
	
	
	@Override
	protected void onNewIntent(Intent intent) {
	    setIntent(intent);
	    clinicList.clear();
	    markerClinicMap.clear();
	    gMap.clear();
	    handleIntent(intent);
	}
	
	private void handleIntent(Intent intent) {
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	    	if(intent.hasExtra(SearchManager.QUERY))
	    		query = intent.getStringExtra(SearchManager.QUERY);
	    }
	    new GetClinics().execute();
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
			((MyApplication) getApplicationContext()).setCurrentLocation(mCurrentLocation);
			if(!useExistingClinicList)
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
	                rating.setRating( (float) clinic.getRating());
	                tvName.setText( clinic.getName());
	                tvType.setText( clinic.getType());
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
						ArrayList<NameValuePair> queryList = null;
						if(getIntent().hasExtra(SearchManager.QUERY) && query != null && !query.equals(""))
						{
							queryList = new ArrayList<NameValuePair>();
							queryList.add(new DetailNameValuePair("name", query));
						}
						// Making a request to url and getting response
						String jsonStr = sh.makeServiceCall(
								URLs.url_list_doctor,
								ServiceHandler.GET,queryList);

						Log.d("Response: ", "> " + jsonStr);

						clinicList = sh.parseClinics(jsonStr,false);

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
