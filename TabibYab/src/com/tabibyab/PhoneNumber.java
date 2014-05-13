package com.tabibyab;

import org.json.JSONException;
import org.json.JSONObject;

public class PhoneNumber {
	
	String title;
	String tel;
	
	public PhoneNumber(String title, String tel) {
		// TODO Auto-generated constructor stub
		
		this.title = title;
		this.tel = tel;
	}
	
	public PhoneNumber(JSONObject jo) {
		
		try {
			this.title = jo.getString(TAGS.TAG_TITLE);
		    this.tel = jo.getString(TAGS.TAG_TEL);
		    
        } catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
