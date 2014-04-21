package com.tabibyab;

public class Coordinate {
	
	public double lng;
	public double lat;

	public Coordinate(String c) {
		// TODO Auto-generated constructor stub
		
		c = c.replace("POINT (", "");
		c = c.replace(")", "");
		int index = c.indexOf(" ", 0);
		String lat = c.substring(0, index);
		String lng = c.substring(index+1);
		this.lng = Double.parseDouble(lng);
		this.lat = Double.parseDouble(lat);
		
	}
	
	public Coordinate(double lng, double lat)
	{
		this.lng = lng;
		this.lat = lat;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		
		return "POINT (" + lat + " " + lng + ")" ;
	}

}