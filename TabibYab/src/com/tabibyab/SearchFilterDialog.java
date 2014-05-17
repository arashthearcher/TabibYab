package com.tabibyab;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.ToggleButton;

public class SearchFilterDialog extends DialogFragment {
	
	
	
	
	
    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }
    
    // Use this instance of the interface to deliver action events
    NoticeDialogListener mListener;
    ToggleButton[] toggleButtons ;
    Spinner clinicTypeSpinner;
    Spinner clinicSpecialitySpinner;
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
	    
	    toggleButtons = new ToggleButton[4];
	    toggleButtons[0] = (ToggleButton) inflator.findViewById(R.id.lessThan1kmButton);
	    toggleButtons[1] = (ToggleButton) inflator.findViewById(R.id.lessThan2kmButton);
	    toggleButtons[2] = (ToggleButton) inflator.findViewById(R.id.lessThan3kmButton);
	    toggleButtons[3] = (ToggleButton) inflator.findViewById(R.id.lessThan4kmButton);
	    
	    setMyButtonsClickListener();
	    
	    
	    clinicTypeSpinner = (Spinner) inflator.findViewById(R.id.clinicTypeSpinnerDialog);
		
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				inflator.getContext(), R.array.clinic_types_dialog_spinner, R.layout.dropdown_item);
		clinicTypeSpinner.setAdapter(adapter);
	    
	    
	    return builder.create();
	}
	
	private void unCheckAllButtons(View v)
	{
		for (ToggleButton tb : toggleButtons) {
			if(tb != v)
				tb.setChecked(false);
		}
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
