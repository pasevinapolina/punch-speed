package com.artioml.practice.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.artioml.practice.R;
import com.artioml.practice.preferences.SettingsPreferenceManager;
import com.artioml.practice.utils.PunchType;

/**
 * Created by Polina P on 20.02.2017.
 */

public class MainSettingsDialog extends SettingsDialog {

    public static final String TAG = MainSettingsDialog.class.getSimpleName();
    private static final String MAIN_SETTINGS = "mainSettings";

    private EditText weightEditText;
    private Spinner spinner;

    private View glovesWeight;

    private RadioButton leftHandButton;
    private RadioButton rightHandButton;
    private RadioButton glovesOnButton;
    private RadioButton glovesOffButton;
    private RadioButton withStepButton;
    private RadioButton withoutStepButton;

    private String hand;
    private String gloves;
    private String position;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.dialog_main_settings, container, false);
        init(contentView);
        setChecked();

        contentView.findViewById(R.id.menuTopButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commitChanges();
                getDialog().dismiss();
            }
        });

        final CompoundButton.OnCheckedChangeListener onCheckedChangeListener =
            new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    switch (buttonView.getId()) {
                        case (R.id.glovesOnButton):
                            glovesWeight.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                            break;
                        case (R.id.glovesOffButton):
                            glovesWeight.setVisibility(!isChecked ? View.VISIBLE : View.GONE);
                            break;
                    }
                }
            };

        glovesOnButton.setOnCheckedChangeListener(onCheckedChangeListener);
        glovesOffButton.setOnCheckedChangeListener(onCheckedChangeListener);

        return contentView;
    }


    @Override
    protected void init(View contentView) {
        SettingsPreferenceManager preferenceManager =
                new SettingsPreferenceManager(getActivity(), MAIN_SETTINGS);
        hand = preferenceManager.getHandPreference();
        gloves = preferenceManager.getGlovesPreference();
        position = preferenceManager.getPositionPreference();

        leftHandButton = (RadioButton)  contentView.findViewById(R.id.leftHandButton);
        rightHandButton = (RadioButton) contentView.findViewById(R.id.rightHandButton);
        glovesOnButton = (RadioButton)  contentView.findViewById(R.id.glovesOnButton);
        glovesOffButton = (RadioButton) contentView.findViewById(R.id.glovesOffButton);
        withStepButton = (RadioButton)  contentView.findViewById(R.id.withStepButton);
        withoutStepButton = (RadioButton) contentView.findViewById(R.id.withoutStepButton);

        weightEditText = (EditText) contentView.findViewById(R.id.weightEditText);
        weightEditText.setText(preferenceManager.getGlovesWeightPreference());

        glovesWeight = (LinearLayout)contentView.findViewById(R.id.glovesWeight);

        spinner = (Spinner) contentView.findViewById(R.id.punchTypeSpinner);
        spinner.setSelection(preferenceManager.getPunchTypePreference());
    }

    @Override
    protected void setChecked() {
        if (PunchType.getTypeByValue(hand) == PunchType.LEFT_HAND)
            leftHandButton.setChecked(true);
        else rightHandButton.setChecked(true);

        if (PunchType.getTypeByValue(gloves) == PunchType.GLOVES_ON) {
            glovesOnButton.setChecked(true);
            glovesWeight.setVisibility(View.VISIBLE);
        } else {
            glovesOffButton.setChecked(true);
            glovesWeight.setVisibility(View.GONE);
        }

        if (PunchType.getTypeByValue(position) == PunchType.WITH_STEP)
            withStepButton.setChecked(true);
        else withoutStepButton.setChecked(true);
    }

    @Override
    protected void commitChanges(){
        SettingsPreferenceManager preferenceManager =
                new SettingsPreferenceManager(getActivity(), MAIN_SETTINGS);

        PunchType hand = leftHandButton.isChecked() ? PunchType.LEFT_HAND : PunchType.RIGHT_HAND;
        PunchType gloves = glovesOnButton.isChecked() ? PunchType.GLOVES_ON : PunchType.GLOVES_OFF;
        PunchType position = withStepButton.isChecked() ? PunchType.WITH_STEP : PunchType.WITHOUT_STEP;

        preferenceManager.setPunchPreferences(hand, gloves,
                weightEditText.getText().toString(), position,
                spinner.getSelectedItemPosition());
    }
}