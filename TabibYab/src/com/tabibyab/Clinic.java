package com.tabibyab;

import com.google.android.gms.maps.model.Marker;

public class Clinic {

	
	int id;
	String name;
	double rating;
	Marker marker = null;
	Coordinate coordinate;
	String type;
	String appointmentOnly;
	
	
	
	
	
	public Clinic(int id, String name, Coordinate coordinate, String type, String appointmentOnly) {
		// TODO Auto-generated constructor stub
		this.id = id ;
		this.name = name ;
		this.coordinate = coordinate ;
		this.type = type;
		this.appointmentOnly = appointmentOnly;
		
	}
	
	
	public Clinic(int id, String name, Coordinate coordinate) {
		// TODO Auto-generated constructor stub
		this.id = id ;
		this.name = name ;
		this.coordinate = coordinate ;
		
		
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


	public Coordinate getCoordinate() {
		return coordinate;
	}


	public void setCoordinate(Coordinate coordinate) {
		this.coordinate = coordinate;
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
	
}
