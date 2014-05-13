package com.tabibyab;

import org.json.JSONException;
import org.json.JSONObject;

public class OperatingHour {
	
	String from;
	String to;
	public OperatingHour(String from, String to) {
		// TODO Auto-generated constructor stub
		this.from = from ;
		this.to = to ;
	}
	
public OperatingHour(JSONObject jo) {
		
		try {
			this.from = jo.getString(TAGS.TAG_FROM_TIME);
		    this.to = jo.getString(TAGS.TAG_TO_TIME);
		    
        } catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
