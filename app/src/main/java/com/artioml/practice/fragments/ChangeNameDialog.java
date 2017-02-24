package com.artioml.practice.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.artioml.practice.R;
import com.artioml.practice.asynctasks.ChangeLoginAsyncTask;
import com.artioml.practice.interfaces.TaskExecutionListener;

/**
 * Created by Polina P on 05.02.2017.
 */

public class ChangeNameDialog extends AppCompatDialogFragment implements TaskExecutionListener {

    private ChangeLoginAsyncTask changeLoginTask;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater linf = LayoutInflater.from(getActivity());
        final View inflater = linf.inflate(R.layout.dialog_change_name, null);

        Button yesButton = (Button)inflater.findViewById(R.id.dialogYesButton);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText usernameEditText = (EditText) inflater.findViewById(R.id.usernameEditText);
                String newLogin = usernameEditText.getText().toString();
                changeLoginTask.execute(newLogin);
            }
        });

        Button noButton = (Button)inflater.findViewById(R.id.dialogNoButton);
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        changeLoginTask = new ChangeLoginAsyncTask();
        changeLoginTask.addTaskListener(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(inflater);

        builder.setCancelable(true);

        return builder.create();
    }

    @Override
    public void onStarted() {

    }

    @Override
    public void onCompleted(Object... result) {
        ChangeNameListener activity = (ChangeNameListener)getActivity();
        activity.updateLogin((String)result[0]);
        dismiss();
    }

    @Override
    public void onError() {
        //Toast.makeText(getDialog().getContext(), getString(R.string.community_error), )
    }

    public interface ChangeNameListener {
        void updateLogin(String username);
    }
}
