package com.artioml.practice;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.AlertDialog;

import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Polina P on 06.02.2017.
 */

public class LogoutDialog extends AppCompatDialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater linf = LayoutInflater.from(getActivity());
        final View inflater = linf.inflate(R.layout.dialog_logout, null);

        Button yesButton = (Button)inflater.findViewById(R.id.dialogYesButton);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogoutListener activity = (LogoutListener)getActivity();
                activity.logout();
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

    public interface LogoutListener {
        void logout();
    }
}
