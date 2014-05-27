package com.tabibyab;


import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class AddClinicActivity extends Activity {
	
	TextView messageText;
    Button uploadButton;
    int serverResponseCode = 0;
    ProgressDialog dialog = null;
        
    String upLoadServerUri = null;
     
    /**********  File Path *************/
    final String uploadFilePath = "/mnt/sdcard/";
    final String uploadFileName = "service_lifecycle.png";

	
	
	private ProgressDialog pDialog;
	public static int LOCATION_REQUEST;
	private Coordinate selectedLocation = null ;
	private Spinner spinnerClinicType = null ;
	private Spinner spinnerSpecialtyType = null ;
	private Spinner spinnerSpecialityLevelType = null ;
	private TextView nameTextView = null ;
	private TextView phoneTextView = null ;
	private TextView addressTextView = null ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_clinic);
		nameTextView = (TextView) findViewById(R.id.name_add_clinic);
		phoneTextView = (TextView) findViewById(R.id.phone_add_clinic);
		addressTextView = (TextView) findViewById(R.id.addreess_add_clinic);
		spinnerClinicType = (Spinner) findViewById(R.id.clinicType);
		spinnerSpecialtyType = (Spinner) findViewById(R.id.specialityTypes);
		spinnerSpecialityLevelType = (Spinner) findViewById(R.id.levelSpecialityTypes);
		ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
				this, R.array.clinic_types, R.layout.dropdown_item);
		spinnerClinicType.setAdapter(adapter1);
		ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
				this, R.array.speciality_types, R.layout.dropdown_item);
		spinnerSpecialtyType.setAdapter(adapter2);
		ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(
				this, R.array.speciality_level_types, R.layout.dropdown_item);
		spinnerSpecialityLevelType.setAdapter(adapter3);
		Button button = (Button) findViewById(R.id.add_point_on_map);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent selectLocationIntent = new Intent(AddClinicActivity.this, SelectLocationActivity.class);
				startActivityForResult(selectLocationIntent,LOCATION_REQUEST);
				}
		});
		
//		uploadButton = (Button)findViewById(R.id.uploadButton);
//        messageText  = (TextView)findViewById(R.id.messageText);
//        messageText.setText("Uploading file path :- '/mnt/sdcard/"+uploadFileName+"'");
//        /************* Php script path ****************/
//        upLoadServerUri = "http://www.androidexample.com/media/UploadToServer.php";
//         
//        uploadButton.setOnClickListener(new OnClickListener() {            
//            @Override
//            public void onClick(View v) {
//                 
//                dialog = ProgressDialog.show(AddClinicActivity.this, "", "Uploading file...", true);
//                 
//                new Thread(new Runnable() {
//                        public void run() {
//                             runOnUiThread(new Runnable() {
//                                    public void run() {
//                                        messageText.setText("uploading started.....");
//                                    }
//                                });                      
//                           
//                             uploadFile(uploadFilePath + "" + uploadFileName);
//                                                      
//                        }
//                      }).start();        
               
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == LOCATION_REQUEST)
		{
			if(resultCode == RESULT_OK)
			{
				String lat = data.getExtras().getString("Lat");
				String lng = data.getExtras().getString("Lng");
				Log.i("Lat", lat);
				Log.i("Lng", lng);
				double latd = Double.parseDouble(lat);
				double lngd = Double.parseDouble(lng);
				selectedLocation = new Coordinate(lngd, latd);
			}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_clinic_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.select_ok:
			(new AddClinic()).execute();
			return true;
		case R.id.select_cancel:
				Intent returnResultIntent = new Intent();
				setResult(Activity.RESULT_CANCELED, returnResultIntent);
				finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private class AddClinic extends AsyncTask<Void, Void, Void> {
		 
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(AddClinicActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            ArrayList<DetailNameValuePair> params = new ArrayList<DetailNameValuePair>();
            params.add(new DetailNameValuePair(TAGS.TAG_NAME, nameTextView.getText().toString()));
            //params.add(new DetailNameValuePair(TAGS.TAG_PHONE_NUMBERS, phoneTextView.getText().toString()));
            params.add(new DetailNameValuePair(TAGS.TAG_ADDRESS, addressTextView.getText().toString()));
            params.add(new DetailNameValuePair(TAGS.TAG_COORDINATES, selectedLocation.toString()));
            params.add(new DetailNameValuePair(TAGS.TAG_TYPE, spinnerClinicType.getSelectedItem().toString()));
            params.add(new DetailNameValuePair(TAGS.TAG_SPECIALITY, spinnerSpecialtyType.getSelectedItem().toString()));
            params.add(new DetailNameValuePair(TAGS.TAG_SPECIALITY_LEVEL, spinnerSpecialityLevelType.getSelectedItem().toString()));
            String response = sh.makeServiceCall(URLs.url_list_doctor, ServiceHandler.POST,(List) params);
            Clinic doctor_new = sh.parseDoctorInfo(response);
            ArrayList<DetailNameValuePair> phone_params = new ArrayList<DetailNameValuePair>();
            phone_params.add(new DetailNameValuePair(TAGS.TAG_TEL, phoneTextView.getText().toString()));
            phone_params.add(new DetailNameValuePair(TAGS.TAG_ID, Integer.toString(doctor_new.id)));
            sh.makeServiceCall(URLs.url_doctor_phonenumber, ServiceHandler.POST,(List) phone_params);
            
            
			return null;
        }
 
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            finish();
        }
    }
	 public int uploadFile(String sourceFileUri) {
         
         
         String fileName = sourceFileUri;
 
         HttpURLConnection conn = null;
         DataOutputStream dos = null;  
         String lineEnd = "\r\n";
         String twoHyphens = "--";
         String boundary = "*****";
         int bytesRead, bytesAvailable, bufferSize;
         byte[] buffer;
         int maxBufferSize = 1 * 1024 * 1024; 
         File sourceFile = new File(sourceFileUri); 
          
         if (!sourceFile.isFile()) {
              
              dialog.dismiss(); 
               
              Log.e("uploadFile", "Source File not exist :"
                                  +uploadFilePath + "" + uploadFileName);
               
              runOnUiThread(new Runnable() {
                  public void run() {
                      messageText.setText("Source File not exist :"
                              +uploadFilePath + "" + uploadFileName);
                  }
              }); 
              return 0;
         }
         else
         {
              try { 
                   
                    // open a URL connection to the Servlet
                  FileInputStream fileInputStream = new FileInputStream(sourceFile);
                  URL url = new URL(upLoadServerUri);
                   
                  // Open a HTTP  connection to  the URL
                  conn = (HttpURLConnection) url.openConnection(); 
                  conn.setDoInput(true); // Allow Inputs
                  conn.setDoOutput(true); // Allow Outputs
                  conn.setUseCaches(false); // Don't use a Cached Copy
                  conn.setRequestMethod("POST");
                  conn.setRequestProperty("Connection", "Keep-Alive");
                  conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                  conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                  conn.setRequestProperty("uploaded_file", fileName); 
                   
                  dos = new DataOutputStream(conn.getOutputStream());
         
                  dos.writeBytes(twoHyphens + boundary + lineEnd); 
                  dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                                            + fileName + "\"" + lineEnd);
                  dos.writeBytes(lineEnd);
         
                  // create a buffer of  maximum size
                  bytesAvailable = fileInputStream.available(); 
         
                  bufferSize = Math.min(bytesAvailable, maxBufferSize);
                  buffer = new byte[bufferSize];
         
                  // read file and write it into form...
                  bytesRead = fileInputStream.read(buffer, 0, bufferSize);  
                     
                  while (bytesRead > 0) {
                       
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);   
                     
                   }
         
                  // send multipart form data necesssary after file data...
                  dos.writeBytes(lineEnd);
                  dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
         
                  // Responses from the server (code and message)
                  serverResponseCode = conn.getResponseCode();
                  String serverResponseMessage = conn.getResponseMessage();
                    
                  Log.i("uploadFile", "HTTP Response is : "
                          + serverResponseMessage + ": " + serverResponseCode);
                   
                  if(serverResponseCode == 200){
                       
                      runOnUiThread(new Runnable() {
                           public void run() {
                                
                               String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
                                             +" http://www.androidexample.com/media/uploads/"
                                             +uploadFileName;
                                
                               messageText.setText(msg);
                               Toast.makeText(AddClinicActivity.this, "File Upload Complete.", 
                                            Toast.LENGTH_SHORT).show();
                           }
                       });       
                  }
                   
                  //close the streams //
                  fileInputStream.close();
                  dos.flush();
                  dos.close();
                    
             } catch (MalformedURLException ex) {
                  
                 dialog.dismiss();  
                 ex.printStackTrace();
                  
                 runOnUiThread(new Runnable() {
                     public void run() {
                         messageText.setText("MalformedURLException Exception : check script url.");
                         Toast.makeText(AddClinicActivity.this, "MalformedURLException", 
                                                             Toast.LENGTH_SHORT).show();
                     }
                 });
                  
                 Log.e("Upload file to server", "error: " + ex.getMessage(), ex);  
             } catch (Exception e) {
                  
                 dialog.dismiss();  
                 e.printStackTrace();
                  
                 runOnUiThread(new Runnable() {
                     public void run() {
                         messageText.setText("Got Exception : see logcat ");
                         Toast.makeText(AddClinicActivity.this, "Got Exception : see logcat ", 
                                 Toast.LENGTH_SHORT).show();
                     }
                 });
                 Log.e("Upload file to server Exception", "Exception : "
                                                  + e.getMessage(), e);  
             }
             dialog.dismiss();       
             return serverResponseCode; 
              
          } // End else block 
        } 
}
