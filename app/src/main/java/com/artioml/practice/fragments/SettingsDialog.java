package com.artioml.practice.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

/**
 * Created by Polina P on 21.02.2017.
 */

public abstract class SettingsDialog extends AppCompatDialogFragment {

    abstract protected void init(View contentView);
    abstract protected void setChecked();
    abstract protected void commitChanges();

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity());
        if(dialog.getWindow() != null) {
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();

        Window window = getDialog().getWindow();
        if(window != null) {
            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.TOP);
        }
    }

    @Override
    public void onDestroyView() {
        commitChanges();
        ((SettingsDialogListener)getActivity()).updateSettings();
        super.onDestroyView();
    }

    public interface SettingsDialogListener {
        void updateSettings();
    }
}
