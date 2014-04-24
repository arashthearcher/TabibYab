package com.tabibyab;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
 
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
 
public class ServiceHandler {
 
    static String response = null;
    public final static int GET = 1;
    public final static int POST = 2;
 
    public ServiceHandler() {
 
    }
    
    
    
    public ArrayList<HashMap<String, String>> parseClinicList(String jsonStr)
    {
    	ArrayList<HashMap<String, String>> clinicList = new ArrayList<HashMap<String, String>>();
        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);

                
                // Getting JSON Array node
                 JSONArray clinics = jsonObj.getJSONArray(TAGS.TAG_RESULTS);

                // looping through All Contacts
                for (int i = 0; i < clinics.length(); i++) {
                    JSONObject c = clinics.getJSONObject(i);
                     
                    String id = c.getString(TAGS.TAG_ID);
                    String name = c.getString(TAGS.TAG_NAME);
                    String coordinates = c.getString(TAGS.TAG_COORDINATES);
                    String type = c.getString(TAGS.TAG_TYPE);
                    String appointmentOnly = c.getString(TAGS.TAG_APPOINMENT);
                    
                    
                    // tmp hashmap for single contact
                    HashMap<String, String> clinic = new HashMap<String, String>();
                   

                    // adding each child node to HashMap key => value
                    clinic.put(TAGS.TAG_ID, id);
                    clinic.put(TAGS.TAG_NAME, name);
                    clinic.put(TAGS.TAG_TYPE, type);
                    clinic.put(TAGS.TAG_APPOINMENT, appointmentOnly);
                    clinic.put(TAGS.TAG_COORDINATES, coordinates);
                    
                    
                    // adding contact to contact list
                    clinicList.add(clinic);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }
        return clinicList;
    }
 
    /**
     * Making service call
     * @url - url to make request
     * @method - http request method
     * */
    public String makeServiceCall(String url, int method) {
        return this.makeServiceCall(url, method, null);
    }
 
    /**
     * Making service call
     * @url - url to make request
     * @method - http request method
     * @params - http request params
     * */
    public String makeServiceCall(String url, int method,
            List<NameValuePair> params) {
        try {
            // http client
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;
             
            // Checking http request method type
            if (method == POST) {
                HttpPost httpPost = new HttpPost(url);
                // adding post params
                if (params != null) {
                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                }
 
                httpResponse = httpClient.execute(httpPost);
 
            } else if (method == GET) {
                // appending params to url
                if (params != null) {
                	String paramString = URLEncodedUtils.format(params, "utf-8");
                	if(params.size()==1)    //TODO get request in server is not standard!!!
                		url+=paramString.substring(paramString.indexOf("=")+1);
                	else
                		url += "?" + paramString;
                }
                HttpGet httpGet = new HttpGet(url);
                httpResponse = httpClient.execute(httpGet);
            }
            httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);
            Log.d("buuuug", response);
 
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
         
        return response;
 
    }
    
    public HashMap<String, String> parseDoctorInfo(String jsonStr)
    {
    	HashMap<String, String> doctor = new HashMap<String, String>();
        if (jsonStr != null) {
            try {
                	JSONObject c = new JSONObject(jsonStr);
                    String id = c.getString(TAGS.TAG_ID);
                    String name = c.getString(TAGS.TAG_NAME);
                    String coordinates = c.getString(TAGS.TAG_COORDINATES);
                    String type = c.getString(TAGS.TAG_TYPE);
                    String appointmentOnly = c.getString(TAGS.TAG_APPOINMENT);
                    
                    doctor.put(TAGS.TAG_ID, id);
                    doctor.put(TAGS.TAG_NAME, name);
                    doctor.put(TAGS.TAG_TYPE, type);
                    doctor.put(TAGS.TAG_APPOINMENT, appointmentOnly);
                    doctor.put(TAGS.TAG_COORDINATES, coordinates);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }
        return doctor;
    }
}