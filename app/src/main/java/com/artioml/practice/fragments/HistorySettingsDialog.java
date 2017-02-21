package com.artioml.practice.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.artioml.practice.R;
import com.artioml.practice.preferences.SettingsPreferenceManager;
import com.artioml.practice.utils.PunchType;

/**
 * Created by Polina P on 20.02.2017.
 */

public class HistorySettingsDialog extends SettingsDialog {

    public static final String TAG = HistorySettingsDialog.class.getSimpleName();
    private static final String HISTORY_SETTINGS = "historySettings";

    private Spinner spinner;

    private RadioButton leftHandButton;
    private RadioButton rightHandButton;
    private RadioButton dmHandButton;
    private RadioButton glovesOnButton;
    private RadioButton glovesOffButton;
    private RadioButton glovesDmButton;
    private RadioButton withStepButton;
    private RadioButton withoutStepButton;
    private RadioButton dmStepButton;

    private String hand;
    private String gloves;
    private String position;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.dialog_history_settings, container, false);
        init(contentView);
        setChecked();

        contentView.findViewById(R.id.menuTopHistoryButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commitChanges();
                getDialog().dismiss();
            }
        });
        return contentView;
    }

    @Override
    protected void init(View contentView) {
        SettingsPreferenceManager preferenceManager =
                new SettingsPreferenceManager(getActivity(), HISTORY_SETTINGS);
        hand = preferenceManager.getHandPreference(PunchType.DOESNT_MATTER);
        gloves = preferenceManager.getGlovesPreference(PunchType.DOESNT_MATTER);
        position = preferenceManager.getPositionPreference(PunchType.DOESNT_MATTER);

        leftHandButton = (RadioButton) contentView.findViewById(R.id.leftHandHistoryButton);
        rightHandButton = (RadioButton)contentView.findViewById(R.id.rightHandHistoryButton);
        dmHandButton = (RadioButton)   contentView.findViewById(R.id.dmHandHistoryButton);

        glovesOnButton = (RadioButton) contentView.findViewById(R.id.glovesOnHistoryButton);
        glovesOffButton = (RadioButton)contentView.findViewById(R.id.glovesOffHistoryButton);
        glovesDmButton = (RadioButton) contentView.findViewById(R.id.glovesDmHistoryButton);

        withStepButton = (RadioButton) contentView.findViewById(R.id.withStepHistoryButton);
        withoutStepButton = (RadioButton) contentView.findViewById(R.id.withoutStepHistoryButton);
        dmStepButton = (RadioButton)   contentView.findViewById(R.id.dmStepHistoryButton);

        spinner = (Spinner) contentView.findViewById(R.id.punchTypeHistorySpinner);
        spinner.setSelection(preferenceManager.getPunchTypePreference());
    }

    @Override
    protected void setChecked() {
        PunchType type = PunchType.getTypeByValue(hand);
        if (type == PunchType.LEFT_HAND)
            leftHandButton.setChecked(true);
        else if (type == PunchType.RIGHT_HAND)
            rightHandButton.setChecked(true);
        else dmHandButton.setChecked(true);

        type = PunchType.getTypeByValue(gloves);
        if (type == PunchType.GLOVES_ON)
            glovesOnButton.setChecked(true);
        else if (type == PunchType.GLOVES_OFF)
            glovesOffButton.setChecked(true);
        else glovesDmButton.setChecked(true);

        type = PunchType.getTypeByValue(position);
        if (type == PunchType.WITH_STEP)
            withStepButton.setChecked(true);
        else if (type == PunchType.WITHOUT_STEP)
            withoutStepButton.setChecked(true);
        else dmStepButton.setChecked(true);
    }

    @Override
    protected void commitChanges(){
        SettingsPreferenceManager preferenceManager =
                new SettingsPreferenceManager(getActivity(), HISTORY_SETTINGS);

        PunchType hand =  leftHandButton.isChecked() ? PunchType.LEFT_HAND :
                rightHandButton.isChecked() ? PunchType.RIGHT_HAND : PunchType.DOESNT_MATTER;

        PunchType gloves = glovesOnButton.isChecked() ? PunchType.GLOVES_ON :
                glovesOffButton.isChecked() ? PunchType.GLOVES_OFF : PunchType.DOESNT_MATTER;

        PunchType position =  withStepButton.isChecked() ? PunchType.WITH_STEP :
                withoutStepButton.isChecked() ? PunchType.WITHOUT_STEP : PunchType.DOESNT_MATTER;

        preferenceManager.setPunchPreferences(hand, gloves, position, spinner.getSelectedItemPosition());

        Log.i(TAG, "commitChanges");
    }

}
