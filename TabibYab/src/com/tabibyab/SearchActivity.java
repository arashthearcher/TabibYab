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
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SearchView;

import android.graphics.Bitmap;

public class SearchActivity extends Activity {

	ArrayList<NameValuePair> queryList = new ArrayList<NameValuePair>();
	ListView searchListView ;
	private ProgressDialog pDialog;
	ArrayList<Clinic> clinicList;
	private boolean useExistingClinicList = false;
	private Location mCurrentLocation;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.search_list);
	    searchListView = (ListView) findViewById(R.id.search_listview);

	    
	    searchListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent DoctorInfintent = new Intent(SearchActivity.this, DoctorInfActivity.class);
				DoctorInfintent.putExtra("doctor_id", clinicList.get(position).getId());
				startActivity(DoctorInfintent);
			}
		});
	    
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
		case R.id.action_refresh_list:
			refreshList();
			return true;
		case R.id.action_home_list_view:
			Intent homeIntent = new Intent(SearchActivity.this, MainMenuActivity.class);
			startActivity(homeIntent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	
	
	
	@Override
	protected void onNewIntent(Intent intent) {
	    useExistingClinicList = false;
		setIntent(intent);
	    handleIntent(intent);
	}

	private void handleIntent(Intent intent) {
	    
		if(useExistingClinicList)
		{
			clinicList = ((MyApplication) getApplicationContext()).getClinicList();
			setMyListAdapter();
		}
		else
		{
			if(intent.hasExtra(TAGS.TAG_SPECIALITY))
				queryList.add(new DetailNameValuePair(TAGS.TAG_SPECIALITY, intent.getStringExtra("speciality")));
			
			if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
		    	if(intent.hasExtra(SearchManager.QUERY))
		    		{
			    		String query = intent.getStringExtra(SearchManager.QUERY);
			    		if(query != null && !query.equals(""))
						{
							queryList.add(new DetailNameValuePair("name", query));
						}
		    		}
		    }
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
	
	private void refreshList()
	{
		useExistingClinicList = false;
		queryList.clear();
		new GetClinics().execute();
		
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

			String jsonStr;
			
			if(queryList != null && queryList.size()!= 0)
			{
				jsonStr = sh.makeServiceCall(URLs.url_list_doctor, ServiceHandler.GET, queryList);
			}
			else
			{
				jsonStr = sh.makeServiceCall(URLs.url_list_doctor,ServiceHandler.GET);
			}
				

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
