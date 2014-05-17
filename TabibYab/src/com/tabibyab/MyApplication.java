package com.tabibyab;

import java.util.ArrayList;

import android.app.Application;
import android.location.Location;

public class MyApplication extends Application {

	Location currentLocation;
	String searchText;
	ArrayList<Clinic> clinicList;

	
	public ArrayList<Clinic> getClinicList() {
		return clinicList;
	}

	public void setClinicList(ArrayList<Clinic> clinicList) {
		this.clinicList = clinicList;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public Location getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(Location currentLocation) {
		this.currentLocation = currentLocation;
	}
	
}
