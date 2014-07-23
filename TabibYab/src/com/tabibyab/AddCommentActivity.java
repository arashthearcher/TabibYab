package com.tabibyab;

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
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.ToggleButton;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class AddCommentActivity extends Activity {
	private int doctor_id;
	private ProgressDialog pDialog;
	private EditText name = null;
	private EditText commText = null;
	private RatingBar totalRating = null;
	private SeekBar waitingTimeSeekBar, visitingFeeSeekBar, dayUntilAppoinmentSeekTime;
	private TextView waitingTimeTV, visitingFeeTV, dayUntilAppoinmentTV;
	private ToggleButton showMore;
	private RelativeLayout showMoreFrame;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_comment);
		Intent AddCommentintent = getIntent();
		doctor_id = AddCommentintent.getIntExtra("doctor_id", 1);
		name = (EditText) findViewById(R.id.commenter_name);
		commText = (EditText) findViewById(R.id.comment_text);
		totalRating = (RatingBar) findViewById(R.id.ratingTotal);
		Button button = (Button) findViewById(R.id.add_main_comment);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new AddComment().execute();
				Intent backComments = new Intent(
						AddCommentActivity.this, DoctorCommentsActivity.class);
				backComments.putExtra("doctor_id", doctor_id);
				startActivity(backComments);
			}
		});
		
		waitingTimeSeekBar = (SeekBar) findViewById(R.id.waiting_time_seekbar_add_comment);
		visitingFeeSeekBar = (SeekBar) findViewById(R.id.visiting_fee_seekbar_add_comment);
		dayUntilAppoinmentSeekTime = (SeekBar) findViewById(R.id.day_until_appointment_add_comment);
		
		waitingTimeTV = (TextView) findViewById(R.id.minutes_number_add_comment);
		visitingFeeTV = (TextView) findViewById(R.id.fee_text_view_add_comment);
		dayUntilAppoinmentTV = (TextView) findViewById(R.id.number_of_days_until_appointment_textview_add_comment);
		
		
		
	waitingTimeSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
		
		
    int progress = 0;
        @Override
      public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) 
      {
        	
        progress = progresValue;
        waitingTimeTV.setText(String.valueOf((int)(progress*1.2)));
      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {
        // Do something here, 
                      //if you want to do anything at the start of
        // touching the seekbar
      }

      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {
        // Display the value in textview
      }
  });
		
	dayUntilAppoinmentSeekTime.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
		
		
		int progress = 0;
		@Override
		public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) 
		{
			
			progress = progresValue;
			dayUntilAppoinmentTV.setText(String.valueOf((int)(progress*1.2)));
		}
		
		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// Do something here, 
			//if you want to do anything at the start of
			// touching the seekbar
		}
		
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// Display the value in textview
		}
	});
	
	visitingFeeSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
		
		
		int progress = 0;
		@Override
		public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) 
		{
			
			progress = progresValue;
			visitingFeeTV.setText(String.valueOf(progress*1000));
		}
		
		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// Do something here, 
			//if you want to do anything at the start of
			// touching the seekbar
		}
		
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// Display the value in textview
		}
	});
	

	
	showMoreFrame = (RelativeLayout) findViewById(R.id.show_more_frame);
	
	showMore = (ToggleButton) findViewById(R.id.toggle_show_more_less_comments);
	showMore.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (showMore.isChecked()) 
			{
				showMoreFrame.setVisibility(View.VISIBLE);
				
			} else 
			{
				showMoreFrame.setVisibility(View.INVISIBLE);
			}
		}
	});
		
		
		
		
		
		
		
		
	}

	private class AddComment extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(AddCommentActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			ServiceHandler sh = new ServiceHandler();
			ArrayList<DetailNameValuePair> params = new ArrayList<DetailNameValuePair>();
			params.add(new DetailNameValuePair(TAGS.TAG_NAME, name.getText()
					.toString()));
			params.add(new DetailNameValuePair(TAGS.TAG_COMMENT, commText
					.getText().toString()));
			params.add(new DetailNameValuePair(TAGS.TAG_CLINIC, Integer.toString(doctor_id)));
			params.add(new DetailNameValuePair(TAGS.TAG_RATING, Float.toString(totalRating.getRating())));
			
			if(showMore.isChecked())
			{
				params.add(new DetailNameValuePair(TAGS.TAG_WAITING_TIME, String.valueOf(waitingTimeTV.getText())));
				params.add(new DetailNameValuePair(TAGS.TAG_QUEUE_TIME, String.valueOf(dayUntilAppoinmentTV.getText())));
				params.add(new DetailNameValuePair(TAGS.TAG_PRICE, String.valueOf(visitingFeeTV.getText())));
			}
			
			
			sh.makeServiceCall(URLs.url_doctor_comments, ServiceHandler.POST,
					(List) params);
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

}
