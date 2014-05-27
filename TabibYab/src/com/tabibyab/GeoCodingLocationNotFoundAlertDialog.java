package com.tabibyab;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
//TODO to be completed !!
public class GeoCodingLocationNotFoundAlertDialog extends DialogFragment {
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_location_not_found_geocoding)
               
              .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // User cancelled the dialog
                	   GeoCodingLocationNotFoundAlertDialog.this.dismiss();
                   }
               });
        // Create the AlertDialog object and return it
        return builder.create();
    }

}
