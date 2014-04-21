package com.tabibyab;

import org.apache.http.NameValuePair;

public class DetailNameValuePair implements NameValuePair {

	
	private String name;
	private String value;
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return value;
	}
	
	public DetailNameValuePair(String name, String value) {
		// TODO Auto-generated constructor stub
		
		this.name = name;
		this.value = value;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return name +" : "+ value;
	}

}
