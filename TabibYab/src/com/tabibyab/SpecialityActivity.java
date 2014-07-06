package com.tabibyab;

import java.util.ArrayList;
import java.util.Arrays;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import android.view.View;

public class SpecialityActivity extends Activity {

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.speciality_list_menu_activity);
		GridView gridview = (GridView) findViewById(R.id.gridview);

		
		ArrayList<Integer> pics = new ArrayList(Arrays.asList(R.drawable.psychology,R.drawable.radiology,R.drawable.pregnant,R.drawable.nose,R.drawable.surgery,R.drawable.baby,R.drawable.stomach,R.drawable.lung,R.drawable.heart, R.drawable.internist, R.drawable.eye, R.drawable.skin, R.drawable.physical, R.drawable.dentist, R.drawable.other));
		
		
		gridview.setAdapter(new SpecialityGridAdapter(this, pics, getResources().getStringArray(R.array.speciality_types)));

		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				Intent intent = new Intent(SpecialityActivity.this,
						SearchActivity.class);
				intent.putExtra(TAGS.TAG_SPECIALITY, getResources().getStringArray(R.array.speciality_types)[position]);
				startActivity(intent);
			}
		});
	}
}
