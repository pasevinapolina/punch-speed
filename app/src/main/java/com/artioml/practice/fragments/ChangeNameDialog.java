package com.artioml.practice.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
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

    public static final String TAG = ChangeNameDialog.class.getSimpleName();

    private ChangeLoginAsyncTask changeLoginTask;
    private Button yesButton;
    private ProgressDialog progressDialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(changeLoginTask != null) {
            Log.i(TAG, "Task restored");
            changeLoginTask.addTaskListener(this);
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

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

        yesButton = (Button)contentView.findViewById(R.id.dialogYesButton);
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

        if(changeLoginTask == null) {
            changeLoginTask = new ChangeLoginAsyncTask();
            changeLoginTask.addTaskListener(this);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(changeLoginTask != null) {
            changeLoginTask.removeTaskListener();
        }
        if(progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onStarted() {
        yesButton.setEnabled(false);
        if(progressDialog == null || !progressDialog.isShowing()) {
            progressDialog = ProgressDialog.show(getActivity(),getString(R.string.collecting_data),
                    getString(R.string.collecting_data));
        }
    }

    @Override
    public void onCompleted(Object... result) {
        if(progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        ChangeNameListener activity = (ChangeNameListener)getActivity();
        activity.updateLogin((String)result[0]);
        changeLoginTask = null;
        dismiss();
    }

    @Override
    public void onError() {
        if(progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        Toast.makeText(getDialog().getContext(), getString(R.string.community_error), Toast.LENGTH_LONG)
                .show();
        yesButton.setEnabled(true);
        changeLoginTask = null;
    }

    public interface ChangeNameListener {
        void updateLogin(String username);
    }
}
