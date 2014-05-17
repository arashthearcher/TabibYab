package com.tabibyab;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;

import android.graphics.Bitmap;

public class SearchActivity extends Activity {

	
	ListView searchListView ;
	private ProgressDialog pDialog;
	ArrayList<Clinic> clinicList;
	String query = null ;
	private boolean useExistingClinicList = false;
	private Location mCurrentLocation;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.search_list);
	    searchListView = (ListView) findViewById(R.id.search_listview);

	    mCurrentLocation = ((MyApplication) getApplicationContext()).getCurrentLocation();
	    
	    Intent intent = getIntent();
		useExistingClinicList = intent.getBooleanExtra("useExistingClinicList", false) ;
	    handleIntent(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_view_menu, menu);
		

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
			Intent addClinicIntent = new Intent(SearchActivity.this,
					AddClinicActivity.class);
			startActivity(addClinicIntent);
			return true;
		case R.id.clinicMapView:
			Intent clinicMapIntent = new Intent(SearchActivity.this,
					MainActivity.class);
			clinicMapIntent.putExtra("useExistingClinicList", true);
			((MyApplication) getApplicationContext()).setClinicList(clinicList);
			startActivity(clinicMapIntent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	
	
	
	@Override
	protected void onNewIntent(Intent intent) {
	    setIntent(intent);
	    handleIntent(intent);
	}

	private void handleIntent(Intent intent) {
	    
		if(useExistingClinicList)
		{
			clinicList = ((MyApplication) getApplicationContext()).getClinicList();
			setMyListAdapter();
		}
		else if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	    	if(intent.hasExtra(SearchManager.QUERY))
	    		query = intent.getStringExtra(SearchManager.QUERY);
	    	new GetClinics().execute();
	    }
	}
	
	public void setMyListAdapter()
	{
		String[] names = new String[clinicList.size()] ;
		  for (int i = 0; i < clinicList.size(); i++) {
			names[i] = clinicList.get(i).getName();
		}
		searchListView.setAdapter(new ClinicArrayAdapter(SearchActivity.this, clinicList, names, new ArrayList<Bitmap>()));
	}
	
	private class GetClinics extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(SearchActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected Void doInBackground(Void... arg0) {

			// Creating service handler class instance
			
			ServiceHandler sh = new ServiceHandler();
			ArrayList<NameValuePair> queryList = null;
			if(getIntent().hasExtra(SearchManager.QUERY) && !query.equals(""))
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
			  setMyListAdapter();

		}

	}
	
	
}
