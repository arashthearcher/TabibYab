package com.tabibyab;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.Marker;

public class Clinic {

	
	int id;
	String name;
	double rating;
	Marker marker = null;
	Coordinate coordinates;
	String type;
	String appointmentOnly;
	
	
	
	public Clinic(JSONObject jo) {
		
		try {
			this.id = Integer.parseInt(jo.getString(TAGS.TAG_ID));
		    this.name = jo.getString(TAGS.TAG_NAME);
		    this.coordinates = new Coordinate(jo.getString(TAGS.TAG_COORDINATES));
		    this.type = jo.getString(TAGS.TAG_TYPE);
		    this.appointmentOnly = jo.getString(TAGS.TAG_APPOINMENT);
		    
        } catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public Clinic(int id, String name, Coordinate coordinate, String type, String appointmentOnly) {
		// TODO Auto-generated constructor stub
		this.id = id ;
		this.name = name ;
		this.coordinates = coordinate ;
		this.type = type;
		this.appointmentOnly = appointmentOnly;
		
	}
	
	
	public Clinic(int id, String name, Coordinate coordinate) {
		// TODO Auto-generated constructor stub
		this.id = id ;
		this.name = name ;
		this.coordinates = coordinate ;
		
		
	}
	
	public void setMarker(Marker marker)
	{
		this.marker = marker;
	}
	
	
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public double getRating() {
		return rating;
	}


	public void setRating(double rating) {
		this.rating = rating;
	}


	public Coordinate getCoordinates() {
		return coordinates;
	}


	public void setCoordinates(Coordinate coordinate) {
		this.coordinates = coordinate;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getAppointmentOnly() {
		return appointmentOnly;
	}


	public void setAppointmentOnly(String appointmentOnly) {
		this.appointmentOnly = appointmentOnly;
	}


	public Marker getMarker() {
		return marker;
	}


	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		return ((Clinic)o).id == this.id ;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "( " +this.name +" , "+this.coordinates +" )";
	}
	
}
