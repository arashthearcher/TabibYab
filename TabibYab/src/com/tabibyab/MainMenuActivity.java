package com.tabibyab;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.view.View.OnClickListener;

public class MainMenuActivity extends Activity {
	
	ImageButton viewMapButton, viewListButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu_activity);
		
		viewMapButton = (ImageButton) findViewById(R.id.show_map_icon_main_menu);
		viewMapButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent showMap = new Intent(MainMenuActivity.this, MainActivity.class);
				startActivity(showMap);
			}
		});
		viewListButton = (ImageButton) findViewById(R.id.show_list_main_menu);
		viewListButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent showList = new Intent(MainMenuActivity.this, SpecialityActivity.class);
				startActivity(showList);
			}
		});
		
		
		
		
		
		
	}
	
	
	
	

}
