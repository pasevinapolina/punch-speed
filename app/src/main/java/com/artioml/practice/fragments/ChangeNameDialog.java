package com.artioml.practice.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
        final Dialog dialog = new Dialog(getActivity());
        if(dialog.getWindow() != null) {
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View contentView = inflater.inflate(R.layout.dialog_change_name, container, false);

        Button yesButton = (Button)contentView.findViewById(R.id.dialogYesButton);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText usernameEditText = (EditText) contentView.findViewById(R.id.usernameEditText);
                String newLogin = usernameEditText.getText().toString();
                changeLoginTask.execute(newLogin);
            }
        });

        Button noButton = (Button)contentView.findViewById(R.id.dialogNoButton);
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return contentView;
    }

    @Override
    public void onStart() {
        super.onStart();

        changeLoginTask = new ChangeLoginAsyncTask();
        changeLoginTask.addTaskListener(this);
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
        Toast.makeText(getDialog().getContext(), getString(R.string.community_error), Toast.LENGTH_LONG)
                .show();
    }

    public interface ChangeNameListener {
        void updateLogin(String username);
    }
}
