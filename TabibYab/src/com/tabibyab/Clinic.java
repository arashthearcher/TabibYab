package com.tabibyab;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.string;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.Pair;

import com.google.android.gms.maps.model.Marker;

public class Clinic {

	int id;
	String name;
	String rating ;
	Marker marker = null;
	Coordinate coordinates;
	String type;
	String appointmentOnly;
	String specialityLevel;
	String speciality;
	String websiteAddress;
	String address;
	String description;
	Bitmap profilePic ;
	String profilePicAddress;
	String waitingTime;
	String queueTime;
	String visitingFee;
	ArrayList<PhoneNumber> phoneNumbers = new ArrayList<PhoneNumber>();
	ArrayList<OperatingHour> operatingHours = new ArrayList<OperatingHour>();
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
		    this.rating = jo.getString(TAGS.TAG_RATING);
		    //this.specialityLevel = jo.getString(TAGS.TAG_SPECIALITY_LEVEL);
		    this.speciality = jo.getString(TAGS.TAG_SPECIALITY);
		    this.address = jo.getString(TAGS.TAG_ADDRESS);
		    this.profilePicAddress = jo.getString(TAGS.TAG_PROFILE_IMAGE);
		    
		    if (detail)
		    {
		    	this.description = jo.getString(TAGS.TAG_DESCRIPTION);
		    	this.websiteAddress = jo.getString(TAGS.TAG_WEBSITE_ADDRESS);
		    	this.waitingTime = jo.getString(TAGS.TAG_WAITING_TIME);
		    	this.queueTime = jo.getString(TAGS.TAG_QUEUE_TIME);
		    	this.visitingFee = jo.getString(TAGS.TAG_VISITING_FEE);
		    	
		    	JSONArray phoneNumbersJSON = jo.getJSONArray(TAGS.TAG_PHONE_NUMBERS);
		    	parsePhoneNumbers(phoneNumbersJSON);
		    	
		    	JSONArray OHJSON = jo.getJSONArray(TAGS.TAG_OPERATING_HOURS);
		    	parseOperatingHours(OHJSON);
		    	
		    	JSONArray insurancesJSON = jo.getJSONArray(TAGS.TAG_INSURANCES);
		    	parseinsurances(insurancesJSON);
		    	
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
	
	
	public void parseinsurances(JSONArray insuranceJSON) throws JSONException
	{
		if (insuranceJSON.length()>0)
		{
			this.insurances = new ArrayList<String>();
			for (int i = 0; i < insuranceJSON.length(); i++) {
	            JSONObject insurance = insuranceJSON.getJSONObject(i);
	            this.insurances.add(insurance.getString(TAGS.TAG_TITLE));
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

	public Clinic(Cursor c)
	{
		this.id = c.getInt(c.getColumnIndexOrThrow(DatabaseOpenHelper._ID));
		this.name = c.getString(c.getColumnIndexOrThrow(TAGS.TAG_NAME));
		this.address = c.getString(c.getColumnIndexOrThrow(TAGS.TAG_ADDRESS));
		this.speciality = c.getString(c.getColumnIndexOrThrow(TAGS.TAG_SPECIALITY));
//		this.specialityLevel = c.getString(c.getColumnIndexOrThrow(TAGS.TAG_SPECIALITY_LEVEL));
//		this.coordinates = new Coordinate(c.getDouble(c.getColumnIndexOrThrow(TAGS.TAG_LONGITUDE)), c.getDouble(c.getColumnIndexOrThrow(TAGS.TAG_LATITUDE)));
		this.rating = c.getString(c.getColumnIndexOrThrow(TAGS.TAG_RATING));
//		this.type = this.speciality = c.getString(c.getColumnIndexOrThrow(TAGS.TAG_TYPE));
//		this.websiteAddress = this.speciality = c.getString(c.getColumnIndexOrThrow(TAGS.TAG_WEBSITE_ADDRESS));
//		this.description = this.speciality = c.getString(c.getColumnIndexOrThrow(TAGS.TAG_DESCRIPTION));
		
	}
	
	public Clinic(int id, String name, String address, String speciality) 
	{
		this.id = id ;
		this.name = name;
		this.address = address;
		this.speciality = speciality;
	}
	
	public String getSpecialityLevel() {
		return specialityLevel;
	}


	public void setSpecialityLevel(String specialityLevel) {
		this.specialityLevel = specialityLevel;
	}


	public String getPhoneNumbersInString()
	{
		String result = "";
		if(this.phoneNumbers != null)
		{
			
			for (PhoneNumber pn : phoneNumbers) {
				result += pn.title + " : " + pn.tel + "\n" ;
			}
		}
		return result;
	}
	
	public String getInsurancesInString()
	{
		String result = "";
		if(this.insurances != null)
		{
			
			for (String ins : insurances) {
				result += ins+ "\n" ;
			}
		}
		return result;
	}

	public String getOperatingHoursInString()
	{
		String result = "";
		if(this.operatingHours != null)
		{
			
			for (OperatingHour oh : operatingHours) {
				result += oh.from +" - "+ oh.to + "\n" ;
			}
		}
		return result;
	}

		
	
	
	
	public String getWaitingTime() {
		return waitingTime;
	}


	public void setWaitingTime(String waitingTime) {
		this.waitingTime = waitingTime;
	}


	public String getQueueTime() {
		return queueTime;
	}


	public void setQueueTime(String queueTime) {
		this.queueTime = queueTime;
	}


	public String getVisitingFee() {
		return visitingFee;
	}


	public void setVisitingFee(String visitingFee) {
		this.visitingFee = visitingFee;
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


	public String getRating() {
		if(rating == null || rating.equals("null"))
			return "0";
		return rating;
	}


	public void setRating(String rating) {
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