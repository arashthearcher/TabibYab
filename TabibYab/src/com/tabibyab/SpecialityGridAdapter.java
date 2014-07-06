package com.tabibyab;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class SpecialityGridAdapter extends BaseAdapter {

	
	
	private static final int PADDING = 8;
	private static final int WIDTH = 250;
	private static final int HEIGHT = 250;
	private Context mContext;
	private List<Integer> mThumbIds;
	private String[] mStrings;

	public SpecialityGridAdapter(Context c, List<Integer> picIds, String[] mStrings) {
		mContext = c;
		this.mThumbIds = picIds;
		this.mStrings = mStrings;
	}

	@Override
	public int getCount() {
		return mThumbIds.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	// Will get called to provide the ID that
	// is passed to OnItemClickListener.onItemClick()
	@Override
	public long getItemId(int position) {
		return mThumbIds.get(position);
	}

	// create a new ImageView for each item referenced by the Adapter
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		
		LayoutInflater inflater = (LayoutInflater) mContext
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View itemView = inflater.inflate(R.layout.list_grid_view_item, parent, false);
		ImageView image = (ImageView) itemView.findViewById(R.id.image_list_grid_item);
		TextView tv = (TextView) itemView.findViewById(R.id.caption_list_grid_item);
		
		
//		itemView.setLayoutParams(new GridView.LayoutParams(WIDTH, HEIGHT));
//		itemView.setPadding(PADDING, PADDING, PADDING, PADDING);
		
		tv.setText(mStrings[position]);

//		image.setScaleType(ImageView.ScaleType.FIT_CENTER);
		image.setImageResource(mThumbIds.get(position));
		return itemView;
		
		
	}

}
