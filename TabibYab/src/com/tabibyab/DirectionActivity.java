package com.tabibyab;

import org.w3c.dom.Document;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DirectionActivity extends FragmentActivity {
	LatLng start = new LatLng(13.744246499553903, 100.53428772836924);
	LatLng end = new LatLng(13.751279688694071, 100.54316081106663);

	Button buttonAnimate;
	
	GoogleMap mMap;
    GoogleDirection gd;
    Document mDoc;
    
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction_1);
        
        Intent intent = getIntent();
        start = new LatLng(intent.getDoubleExtra(TAGS.TAG_START_LATITUDE, 31), intent.getDoubleExtra(TAGS.TAG_START_LONGITUDE, 51));
        end = new LatLng(intent.getDoubleExtra(TAGS.TAG_END_LATITUDE, 31), intent.getDoubleExtra(TAGS.TAG_END_LONGITUDE, 51));
        
        mMap = ((SupportMapFragment)getSupportFragmentManager()
                .findFragmentById(R.id.map)).getMap();
        
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(start, 15));
        
        gd = new GoogleDirection(this);
        gd.setOnDirectionResponseListener(new GoogleDirection.OnDirectionResponseListener() {
			public void onResponse(String status, Document doc, GoogleDirection gd) {
				mDoc = doc;
				mMap.addPolyline(gd.getPolyline(doc, 3, Color.RED));				
		        mMap.addMarker(new MarkerOptions().position(start)
		        	    .icon(BitmapDescriptorFactory.defaultMarker(
		        	    BitmapDescriptorFactory.HUE_GREEN)));
        
		        mMap.addMarker(new MarkerOptions().position(end)
		        	    .icon(BitmapDescriptorFactory.defaultMarker(
		        	    BitmapDescriptorFactory.HUE_GREEN)));
		        
		        buttonAnimate.setVisibility(View.VISIBLE);
			}
		});
        
        gd.setOnAnimateListener(new GoogleDirection.OnAnimateListener() {
			public void onStart() {
			}
			
			public void onProgress(int progress, int total) {
			}
			
			public void onFinish() {
		        buttonAnimate.setVisibility(View.VISIBLE);
			}
		});
        
        
		gd.setLogging(true);
		gd.request(start, end, GoogleDirection.MODE_DRIVING);
        
        buttonAnimate = (Button)findViewById(R.id.buttonAnimate);
        buttonAnimate.setVisibility(View.GONE);
        buttonAnimate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				v.setVisibility(View.GONE);
				gd.animateDirection(mMap, gd.getDirection(mDoc), GoogleDirection.SPEED_FAST
        				, true, false, true, false, null, false, true, null);
			}
		});
	}
	
    public void onPause() {
    	super.onPause();
    	gd.cancelAnimated();
    }
}
