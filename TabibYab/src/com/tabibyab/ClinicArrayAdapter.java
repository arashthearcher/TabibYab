package com.tabibyab;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class ClinicArrayAdapter extends ArrayAdapter<String> {
  private final Context context;
  private final ArrayList<Clinic> clinicList;
  private final ArrayList<Bitmap> images;
  public ClinicArrayAdapter(Context context, ArrayList<Clinic> clinicList,String[] names, ArrayList<Bitmap> images) {
	super(context, R.layout.doctor_list_view_row,names);
    this.context = context;
    this.clinicList = clinicList;
    this.images = images;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater = (LayoutInflater) context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View rowView = inflater.inflate(R.layout.doctor_list_view_row, parent, false);
    TextView doctorNameTV = (TextView) rowView.findViewById(R.id.doctor_list_view_name);
    TextView addressTV = (TextView) rowView.findViewById(R.id.doctor_list_view_address);
    TextView specialityTV = (TextView) rowView.findViewById(R.id.doctor_list_view_speciality);
    ImageView profilePicIV = (ImageView) rowView.findViewById(R.id.doctor_list_view_profile_pic);
    RatingBar rating = (RatingBar)rowView.findViewById(R.id.doctor_list_view_rating);
    Clinic clinic = clinicList.get(position);
    
    rating.setRating(Float.parseFloat(clinic.getRating()));
//    rating.setEnabled(false);
//    rating.setActivated(false);
    doctorNameTV.setText(clinic.getName());
    addressTV.setText(clinic.getAddress());
    specialityTV.setText(clinic.getSpeciality());

//    profilePic.setImageResource(R.);

    return rowView;
  }
} 