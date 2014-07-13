package com.tabibyab;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class FavoritesActivity extends Activity {

	ListView favoriteListView;
	ArrayList<Clinic> clinicList ;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.favorites_list_activity);
	    favoriteListView = (ListView) findViewById(R.id.favorite_listview);

		// Create a new DatabaseHelper
		DatabaseOpenHelper mDbHelper = new DatabaseOpenHelper(this);

		// Get the underlying database for writing
		SQLiteDatabase mDB = mDbHelper.getWritableDatabase();
	    
	    
	    Cursor c = mDB.query(DatabaseOpenHelper.TABLE_NAME,
				new String[]{mDbHelper._ID, TAGS.TAG_NAME, TAGS.TAG_RATING, TAGS.TAG_ADDRESS, TAGS.TAG_SPECIALITY}, null, new String[] {}, null, null,
				null);
	    int numberOfClinics = c.getCount();
	    clinicList = new ArrayList<Clinic>(numberOfClinics);
	    c.moveToFirst();
	    for (int i = 0; i < numberOfClinics; i++) 
	    {
	    	clinicList.add(new Clinic(c));
	    	c.moveToNext();
		}
	    c.close();
	    mDB.close();
	    mDbHelper.close();
	    
	    
	    String[] names = new String[clinicList.size()] ;
		  for (int i = 0; i < clinicList.size(); i++) {
			names[i] = clinicList.get(i).getName();
		}
		favoriteListView.setAdapter(new ClinicArrayAdapter(this, clinicList, names, new ArrayList<Bitmap>()));
	    
	    
	    
	    
	    favoriteListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent DoctorInfintent = new Intent(FavoritesActivity.this, DoctorInfActivity.class);
				DoctorInfintent.putExtra("doctor_id", clinicList.get(position).getId());
				startActivity(DoctorInfintent);
			}
		});
	}
}
