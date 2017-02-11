package com.artioml.practice;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Polina P on 05.02.2017.
 */

public class ChangeNameDialog extends AppCompatDialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater linf = LayoutInflater.from(getActivity());
        final View inflater = linf.inflate(R.layout.dialog_change_name, null);

        Button yesButton = (Button)inflater.findViewById(R.id.dialogYesButton);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "CHANGED", Toast.LENGTH_LONG).show();
                dismiss();
            }
        });

        Button noButton = (Button)inflater.findViewById(R.id.dialogNoButton);
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(inflater);

        builder.setCancelable(true);

        return builder.create();
    }

}
