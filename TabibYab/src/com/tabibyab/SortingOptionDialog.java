package com.tabibyab;

import com.tabibyab.SearchFilterDialog.NoticeDialogListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioGroup;

public class SortingOptionDialog extends DialogFragment {

	String currentOrderBy;
	
    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }
    
    // Use this instance of the interface to deliver action events
    NoticeDialogListener mListener;
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
	
	
	
	
	RadioGroup sortOptionRadioGroup;
	View inflator;
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		 super.onCreateDialog(savedInstanceState);
		 AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		    // Get the layout inflater
		 LayoutInflater inflater = getActivity().getLayoutInflater();

		  // Inflate and set the layout for the dialog
		    // Pass null as the parent view because its going in the dialog layout
		 inflator = inflater.inflate(R.layout.sort_option, null);
		 
		 sortOptionRadioGroup = (RadioGroup) inflator.findViewById(R.id.sort_option_radio_group);

		 
		 builder.setView(inflator).setPositiveButton(R.string.action_sorting, new DialogInterface.OnClickListener() {
             public void onClick(DialogInterface dialog, int id) {
                 // Send the positive button event back to the host activity
                 mListener.onDialogPositiveClick(SortingOptionDialog.this);
             }
         });
		 
		 
		if(currentOrderBy.equals("r"))
				((RadioButton) inflator.findViewById(R.id.sort_by_rating_choice)).setChecked(true);
		else
				((RadioButton) inflator.findViewById(R.id.sort_by_distance_choice)).setChecked(true);
		 
		 return builder.create();
	}
	
	
	
	
	public void setCurrentOrderBy(String currentOrderBy) {
		this.currentOrderBy = currentOrderBy;
	}




	public String getSelectedSortingOption()
	{
		int radioButtonID = sortOptionRadioGroup.getCheckedRadioButtonId();
		View radioButton = sortOptionRadioGroup.findViewById(radioButtonID);
		int idx = sortOptionRadioGroup.indexOfChild(radioButton);
		return idx == 0 ? "r" : "d" ;
	}
}
