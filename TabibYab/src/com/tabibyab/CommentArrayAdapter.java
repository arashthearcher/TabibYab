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

public class CommentArrayAdapter extends ArrayAdapter<String> {
  private final Context context;
  private final ArrayList<Comment> commentList;
  public CommentArrayAdapter(Context context, ArrayList<Comment> commentList,String[] names) {
	super(context, R.layout.doctor_list_view_row,names);
    this.context = context;
    this.commentList = commentList;
  }

  @Override
	public long getItemId(int position) {

	  return position;
  
  }
  
  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater = (LayoutInflater) context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View rowView = inflater.inflate(R.layout.comment_list_view_row, parent, false);
    TextView commentNameTV = (TextView) rowView.findViewById(R.id.comment_list_view_name);
    TextView commentTV = (TextView) rowView.findViewById(R.id.comment_list_view_comment);
    RatingBar rating = (RatingBar)rowView.findViewById(R.id.comment_list_view_rating);
    Comment comment = commentList.get(position);
    
    rating.setRating(Float.parseFloat(comment.getRating()));
//    rating.setEnabled(false);
//    rating.setActivated(false);
    commentNameTV.setText(comment.getName());
    commentTV.setText(comment.getComment());

//    profilePic.setImageResource(R.);

    return rowView;
  }
} 