package com.tabibyab;


import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
 
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
 
public class ServiceHandler {
 
    static String response = null;
    public final static int GET = 1;
    public final static int POST = 2;
 
    public ServiceHandler() {
 
    }
    
    public ArrayList<Clinic> parseClinics(String jsonStr,boolean detail)
    {
    	ArrayList<Clinic> clinicList = new ArrayList<Clinic>();
        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);

                
                // Getting JSON Array node
                 JSONArray clinics = jsonObj.getJSONArray(TAGS.TAG_RESULTS);

                // looping through All Contacts
                for (int i = 0; i < clinics.length(); i++) {
                    JSONObject c = clinics.getJSONObject(i);
                    Clinic cli = new Clinic(c,detail);
                    if(cli.profilePicAddress.length()!=0){
                    	cli.profilePic = downloadBitmap(URLs.url_doctor_media+cli.profilePicAddress);
                    }
                    clinicList.add(cli);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }
        return clinicList;
    }
    
    
    
    
    
    @Deprecated
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
                    
                    
                    // tmp hashmap for single contact
                    HashMap<String, String> clinic = new HashMap<String, String>();
                   

                    // adding each child node to HashMap key => value
                    clinic.put(TAGS.TAG_ID, id);
                    clinic.put(TAGS.TAG_NAME, name);
                    clinic.put(TAGS.TAG_TYPE, type);
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
    
    public ArrayList<Comment> parseCommentsList(String jsonStr)
    {
//    	ArrayList<HashMap<String, String>> commentsList = new ArrayList<HashMap<String, String>>();
    	ArrayList<Comment> commentsList = new ArrayList<Comment>();
        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                 JSONArray comments = jsonObj.getJSONArray(TAGS.TAG_RESULTS);

                for (int i = 0; i < comments.length(); i++) {
                    JSONObject c = comments.getJSONObject(i);
                    String id = c.getString(TAGS.TAG_ID);
                    String name = c.getString(TAGS.TAG_NAME);
                    String clinic = c.getString(TAGS.TAG_CLINIC);
                    String rating = c.getString(TAGS.TAG_RATING);
                    String commentText = c.getString(TAGS.TAG_COMMENT);
                    
                    commentsList.add(new Comment(Integer.parseInt(id), name, commentText, rating));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }
        return commentsList;
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
                	UrlEncodedFormEntity form = new UrlEncodedFormEntity(params,"UTF-8");
                	form.setContentEncoding(HTTP.UTF_8);
                    httpPost.setEntity(form);
                }
                httpResponse = httpClient.execute(httpPost);
            } else if (method == GET) {
                // appending params to url
                if (params != null) {
                	String paramString = URLEncodedUtils.format(params, "utf-8");
                	if(params.size()==1 && url.equals(URLs.url_doctor_info))    //TODO get request in server is not standard!!!
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
    
    public Clinic parseDoctorInfo(String jsonStr)
    {
    	HashMap<String, String> doctor = new HashMap<String, String>();
    	Clinic clinic = null;
        if (jsonStr != null) {
            try {
                	JSONObject c = new JSONObject(jsonStr);
                	clinic = new Clinic(c, true);
                	clinic.profilePic = downloadBitmap(URLs.url_doctor_media+clinic.profilePicAddress);
//                    String id = c.getString(TAGS.TAG_ID);
//                    String name = c.getString(TAGS.TAG_NAME);
//                    String coordinates = c.getString(TAGS.TAG_COORDINATES);
//                    String type = c.getString(TAGS.TAG_TYPE);
//                    String appointmentOnly = c.getString(TAGS.TAG_APPOINMENT);
//                    
//                    doctor.put(TAGS.TAG_ID, id);
//                    doctor.put(TAGS.TAG_NAME, name);
//                    doctor.put(TAGS.TAG_TYPE, type);
//                    doctor.put(TAGS.TAG_APPOINMENT, appointmentOnly);
//                    doctor.put(TAGS.TAG_COORDINATES, coordinates);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }
//        return doctor;
        return clinic;
    }
    
    public Bitmap downloadBitmap(String url) {
        // initilize the default HTTP client object
        final DefaultHttpClient client = new DefaultHttpClient();

        //forming a HttoGet request 
        final HttpGet getRequest = new HttpGet(url);
        try {

            HttpResponse response = client.execute(getRequest);

            //check 200 OK for success
            final int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode != HttpStatus.SC_OK) {
                Log.w("ImageDownloader", "Error " + statusCode + 
                        " while retrieving bitmap from " + url);
                return null;

            }

            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream inputStream = null;
                try {
                    // getting contents from the stream 
                    inputStream = entity.getContent();

                    // decoding stream data back into image Bitmap that android understands
                    final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                    return bitmap;
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    entity.consumeContent();
                }
            }
        } catch (Exception e) {
            // You Could provide a more explicit error message for IOException
            getRequest.abort();
            Log.e("ImageDownloader", "Something went wrong while" +
                    " retrieving bitmap from " + url + e.toString());
        } 

        return null;
    }
    
    
    
}