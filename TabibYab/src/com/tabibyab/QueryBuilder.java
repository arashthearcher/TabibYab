package com.tabibyab;

import java.util.ArrayList;

import org.apache.http.NameValuePair;

import com.google.android.gms.maps.model.Marker;

public class QueryBuilder {
	
	double distance = -1;
	String name = null;
	Coordinate coordinates = null;
	String type = null;
	String appointmentOnly = null;
	String specialityLevel = null;
	String speciality = null;
	String address = null;

	public QueryBuilder() {
		// TODO Auto-generated constructor stub
	}
	
	
	public QueryBuilder(double distance, String type, String specialityLevel, String speciality) {
		// TODO Auto-generated constructor stub
		this.distance = distance;
		this.type = type ;
		this.speciality = speciality;
		this.specialityLevel = specialityLevel;
	}
	
	public ArrayList<NameValuePair> makeQuery()
	{
		ArrayList<NameValuePair> queryList = new ArrayList<NameValuePair>();
		if(distance != -1)
			queryList.add(new DetailNameValuePair(TAGS.TAG_DISTANCE,Double.toString(distance)));
		if(name != null)
			queryList.add(new DetailNameValuePair(TAGS.TAG_NAME,name));
		
		if(coordinates != null)
		{
			queryList.add(new DetailNameValuePair(TAGS.TAG_LATITUDE,Double.toString(coordinates.getLat())));
			queryList.add(new DetailNameValuePair(TAGS.TAG_LONGITUDE,Double.toString(coordinates.getLng())));
			
		}
		
		if(type != null)
			queryList.add(new DetailNameValuePair(TAGS.TAG_TYPE,type));
		
		if(appointmentOnly != null)
			queryList.add(new DetailNameValuePair(TAGS.TAG_APPOINMENT,appointmentOnly));
		
		if(speciality != null)
			queryList.add(new DetailNameValuePair(TAGS.TAG_SPECIALITY,speciality));
		
		if(specialityLevel != null)
			queryList.add(new DetailNameValuePair(TAGS.TAG_SPECIALITY_LEVEL,specialityLevel));
		
		
		return queryList;
	}
	
	
	


}
