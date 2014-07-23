package com.tabibyab;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PhonePickerDialog extends DialogFragment {
	
	
	ListView phoneListView;
	List<String> phoneNumbers;
	LayoutInflater inflater;
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		 super.onCreateDialog(savedInstanceState);
		 AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		    // Get the layout inflater
		 inflater = getActivity().getLayoutInflater();

		  // Inflate and set the layout for the dialog
		    // Pass null as the parent view because its going in the dialog layout
		 View inflator = inflater.inflate(R.layout.phone_picker_layout, null);
		 
		 phoneListView = (ListView) inflator.findViewById(R.id.phone_picker_list_view);
		 
		 ArrayAdapter<String> adapter = new ArrayAdapter<String>(inflater.getContext(), R.layout.simple_list_item, phoneNumbers );
		 
		 phoneListView.setAdapter(adapter);
		 
		 phoneListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				 String phoneNumber = phoneNumbers.get(position);
				 String uri = "tel:" + phoneNumber.trim() ;
				 Intent intent = new Intent(Intent.ACTION_CALL);
				 intent.setData(Uri.parse(uri));
				 startActivity(intent);
			}
		});

		 
		 builder.setView(inflator).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
             public void onClick(DialogInterface dialog, int id) {
             }

         });
		 
		 
		 
		
		 return builder.create();
	}
	
	public void initialize(ArrayList<PhoneNumber> telNumbers)
	{
		phoneNumbers = new ArrayList<String>();
		
		for (PhoneNumber phoneNumber : telNumbers) {
			phoneNumbers.add(phoneNumber.tel);
		}
	}
	

}
