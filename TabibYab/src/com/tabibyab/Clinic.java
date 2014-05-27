package com.tabibyab;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.util.Pair;

import com.google.android.gms.maps.model.Marker;

public class Clinic {

	
	int id;
	String name;
	double rating;
	Marker marker = null;
	Coordinate coordinates;
	String type;
	String appointmentOnly;
	String specialityLevel;
	String speciality;
	String websiteAddress;
	String address;
	String description;
	Bitmap profilePic;
	String profilePicAddress;
	ArrayList<PhoneNumber> phoneNumbers;
	ArrayList<OperatingHour> operatingHours;
	ArrayList<String> pictureURLs;
	ArrayList<Bitmap> pictures;
	ArrayList<String> insurances;
	boolean detail ; // determines if the clinic is constructed from a detail view JSON or list view JSON
	
	public Clinic(JSONObject jo,boolean detail) {
		
		this.detail = detail;
		try {
			this.id = Integer.parseInt(jo.getString(TAGS.TAG_ID));
		    this.name = jo.getString(TAGS.TAG_NAME);
		    this.coordinates = new Coordinate(jo.getString(TAGS.TAG_COORDINATES));
		    this.type = jo.getString(TAGS.TAG_TYPE);
		    this.rating = jo.getDouble(TAGS.TAG_RATING);
		    this.specialityLevel = jo.getString(TAGS.TAG_SPECIALITY_LEVEL);
		    this.speciality = jo.getString(TAGS.TAG_SPECIALITY);
		    this.address = jo.getString(TAGS.TAG_ADDRESS);
		    this.profilePicAddress = jo.getString(TAGS.TAG_PROFILE_IMAGE);
		    
		    if (detail)
		    {
		    	this.description = jo.getString(TAGS.TAG_DESCRIPTION);
		    	this.websiteAddress = jo.getString(TAGS.TAG_WEBSITE_ADDRESS);
		    	
		    	
		    	JSONArray phoneNumbersJSON = jo.getJSONArray(TAGS.TAG_PHONE_NUMBERS);
		    	parsePhoneNumbers(phoneNumbersJSON);
		    	
		    	JSONArray OHJSON = jo.getJSONArray(TAGS.TAG_OPERATING_HOURS);
		    	parseOperatingHours(OHJSON);
		    	
		    }
		    
        } catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public void parsePhoneNumbers(JSONArray phoneNumbersJSON) throws JSONException
	{
		if (phoneNumbersJSON.length()>0)
		{
			this.phoneNumbers = new ArrayList<PhoneNumber>();
			for (int i = 0; i < phoneNumbersJSON.length(); i++) {
	            JSONObject phoneNumber = phoneNumbersJSON.getJSONObject(i);
	            this.phoneNumbers.add(new PhoneNumber(phoneNumber));
	        }
		}
	}
	
	
	public void parseOperatingHours(JSONArray OHJSON) throws JSONException
	{
		if (OHJSON.length()>0)
		{
			this.operatingHours = new ArrayList<OperatingHour>();
			for (int i = 0; i < OHJSON.length(); i++) {
	            JSONObject operatingHour = OHJSON.getJSONObject(i);
	            this.operatingHours.add(new OperatingHour(operatingHour));
	        }
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


	public String getSpeciality() {
		return speciality;
	}


	public void setSpeciality(String speciality) {
		this.speciality = speciality;
	}


	public String getWebsiteAddress() {
		return websiteAddress;
	}


	public void setWebsiteAddress(String websiteAddress) {
		this.websiteAddress = websiteAddress;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public Bitmap getProfilePic() {
		return profilePic;
	}


	public void setProfilePic(Bitmap profilePic) {
		this.profilePic = profilePic;
	}


	public String getProfilePicAddress() {
		return profilePicAddress;
	}


	public void setProfilePicAddress(String profilePicAddress) {
		this.profilePicAddress = profilePicAddress;
	}


	public ArrayList<PhoneNumber> getPhoneNumbers() {
		return phoneNumbers;
	}


	public void setPhoneNumbers(ArrayList<PhoneNumber> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}


	public ArrayList<OperatingHour> getOperatingHours() {
		return operatingHours;
	}


	public void setOperatingHours(ArrayList<OperatingHour> operatingHours) {
		this.operatingHours = operatingHours;
	}


	public ArrayList<String> getPictureURLs() {
		return pictureURLs;
	}


	public void setPictureURLs(ArrayList<String> pictureURLs) {
		this.pictureURLs = pictureURLs;
	}


	public ArrayList<Bitmap> getPictures() {
		return pictures;
	}


	public void setPictures(ArrayList<Bitmap> pictures) {
		this.pictures = pictures;
	}


	public ArrayList<String> getInsurances() {
		return insurances;
	}


	public void setInsurances(ArrayList<String> insurances) {
		this.insurances = insurances;
	}


	public boolean isDetail() {
		return detail;
	}


	public void setDetail(boolean detail) {
		this.detail = detail;
	}
	
}