package com.tabibyab;

import java.util.ArrayList;

import org.apache.http.NameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;
public class SearchFilterDialog extends DialogFragment {
	
	
	
	
	
    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }
    
    // Use this instance of the interface to deliver action events
    NoticeDialogListener mListener;
    ToggleButton[] toggleButtons ;
    TextView vicinityTextView ;
    CheckBox currentLocationCheckBox;
    Spinner clinicTypeSpinner;
    Spinner clinicSpecialitySpinner;
    Spinner clinicSpecialityLevelSpinner;
    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString() + " must implement NoticeDialogListener");
        }
    }

	
	
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    // Get the layout inflater
	    LayoutInflater inflater = getActivity().getLayoutInflater();

	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    View inflator = inflater.inflate(R.layout.filter_dialog, null);
	    // Add action buttons
	       builder.setView(inflator).setPositiveButton(R.string.find, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // Send the positive button event back to the host activity
                	   
                	   
                       mListener.onDialogPositiveClick(SearchFilterDialog.this);
                   }
               })
               .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // Send the negative button event back to the host activity
                       mListener.onDialogNegativeClick(SearchFilterDialog.this);
                   }

	           });
	       
	       
	       
	       currentLocationCheckBox = (CheckBox) inflator.findViewById(R.id.search_current_location_checkbox);
	        currentLocationCheckBox.setOnClickListener(new android.view.View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (currentLocationCheckBox.isChecked()) {
						vicinityTextView.setEnabled(false);
					} else {
						vicinityTextView.setEnabled(true);
					}
				}
			});
	       
	    vicinityTextView = (TextView) inflator.findViewById(R.id.vicinityOfAddress);
	       
	    
	    toggleButtons = new ToggleButton[4];
	    toggleButtons[0] = (ToggleButton) inflator.findViewById(R.id.lessThan2point5kmButton);
	    toggleButtons[1] = (ToggleButton) inflator.findViewById(R.id.lessThan5kmButton);
	    toggleButtons[2] = (ToggleButton) inflator.findViewById(R.id.lessThan10kmButton);
	    toggleButtons[3] = (ToggleButton) inflator.findViewById(R.id.lessThan20kmButton);
	    toggleButtons[2].setChecked(true);
	    
	    setMyButtonsClickListener();
	    
	    
	    clinicTypeSpinner = (Spinner) inflator.findViewById(R.id.clinicTypeSpinnerDialog);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				inflator.getContext(), R.array.clinic_types_dialog_spinner, R.layout.dropdown_item);
		clinicTypeSpinner.setAdapter(adapter);
		
		
		
		clinicSpecialitySpinner = (Spinner) inflator.findViewById(R.id.specialitySpinnerFilterDialog);
		ArrayAdapter<CharSequence> adapterSpeciality = ArrayAdapter.createFromResource(
				inflator.getContext(), R.array.speciality_types_search, R.layout.dropdown_item);
		clinicSpecialitySpinner.setAdapter(adapterSpeciality);
		
		
		
		
		clinicSpecialityLevelSpinner = (Spinner) inflator.findViewById(R.id.specialityLevelSpinnerFilterDialog);
		ArrayAdapter<CharSequence> adapterSpecialityLevel = ArrayAdapter.createFromResource(
				inflator.getContext(), R.array.speciality_level_types_search, R.layout.dropdown_item);
		clinicSpecialityLevelSpinner.setAdapter(adapterSpecialityLevel);
		clinicSpecialityLevelSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				if (pos == 1)
				{
					clinicSpecialitySpinner.setSelection(0);
					clinicSpecialitySpinner.setEnabled(false);
					
				}
				else
				{
					clinicSpecialitySpinner.setEnabled(true);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			});

		
	    
	    
	    return builder.create();
	}
	
	private void unCheckAllButtons(View v)
	{
		for (ToggleButton tb : toggleButtons) {
			if(tb != v)
				tb.setChecked(false);
		}
	}
	
	public double getSelectedDistance()
	{
		for (int i = 0; i < toggleButtons.length; i++) {
			if(toggleButtons[i].isChecked())
			{
				switch (i) {
				case 0:
					return 2.5;
				case 1:
					return 5;
				case 2:
					return 10;
				case 3:
					return 20;
				
				}
			}
		}
		return -1;
		
	}
	
	
	
	
	public ArrayList<NameValuePair> makeQuery()
	{
		double distance = getSelectedDistance();
		String type = null;
		String speciality = null;
		String specialityLevel = null;
		
		
		if(clinicTypeSpinner.getSelectedItemPosition() != 0)
			type = clinicTypeSpinner.getSelectedItem().toString();
		
		
		if(clinicSpecialityLevelSpinner.getSelectedItemPosition() != 0)
			specialityLevel = clinicSpecialityLevelSpinner.getSelectedItem().toString();
		
		if(clinicSpecialitySpinner.getSelectedItemPosition() != 0)
			speciality = clinicSpecialitySpinner.getSelectedItem().toString();
		
		return new QueryBuilder(distance,type,specialityLevel,speciality).makeQuery();
		

		
		
	}
	
	public String getVicinityAddress()
	{
		return vicinityTextView.getText().toString();
	}
	
	public boolean useCurrentLocation()
	{
		return currentLocationCheckBox.isChecked();
	}
	
	
	private void setMyButtonsClickListener()
	{
		
		android.view.View.OnClickListener tbOnClickListener = new android.view.View.OnClickListener() {
			@Override
			public void onClick(View v) {
				unCheckAllButtons(v);
			}
	    };
	    
		for (ToggleButton tb : toggleButtons) {
			tb.setOnClickListener(tbOnClickListener);
		}
	}

}
