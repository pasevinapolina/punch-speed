package com.artioml.practice.interfaces.impl;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.artioml.practice.R;
import com.artioml.practice.interfaces.SettingsChangeListener;
import com.artioml.practice.models.Result;
import com.artioml.practice.utils.PunchType;

/**
 * Created by user on 17.02.2017.
 */

public class HistorySettingsChangeListener implements SettingsChangeListener {

    private static final String HISTORY_SETTINGS = "historySettings";
    private static final String HAND = "pref_hand";
    private static final String GLOVES = "pref_gloves";
    private static final String POSITION = "pref_position";
    private static final String PUNCH_TYPE = "pref_punchType";

    private Activity activity;
    private View rootView;
    private Result currentSettings;

    public HistorySettingsChangeListener(Activity activity, View rootView) {
        this.activity = activity;
        this.rootView = rootView;
    }

    @Override
    public void fillSettingsPanel() {
        SharedPreferences sharedPreferences =
                activity.getSharedPreferences(HISTORY_SETTINGS, Context.MODE_PRIVATE);

        int punchType = sharedPreferences.getInt(PUNCH_TYPE, 0);
        ((TextView)rootView.findViewById(R.id.typeHistoryTextView)).setText(activity.getResources().getStringArray(
                R.array.punch_type_history_list)[punchType]);

        String hand = sharedPreferences.getString(HAND, PunchType.DOESNT_MATTER.getValue());
        ((ImageView)rootView.findViewById(R.id.handsHistoryImageView)).setImageResource(
                activity.getResources().getIdentifier(
                        "ic_" + hand + "_hand", "drawable", activity.getPackageName()));

        String gloves = sharedPreferences.getString(GLOVES, PunchType.DOESNT_MATTER.getValue());
        ((ImageView) rootView.findViewById(R.id.glovesHistoryImageView)).setImageResource(
                activity.getResources().getIdentifier(
                        "ic_gloves_" + gloves, "drawable", activity.getPackageName()));

        String position = sharedPreferences.getString(POSITION, PunchType.DOESNT_MATTER.getValue());
        ((ImageView) rootView.findViewById(R.id.positionHistoryImageView)).setImageResource(
                activity.getResources().getIdentifier(
                        "ic_punch_" + position + "_step", "drawable", activity.getPackageName()));

        currentSettings = new Result(punchType, hand, gloves, "", position, 0, 0, 0, "");
    }

    public Result getCurrentSettings() {
        return currentSettings;
    }

    public void setCurrentSettings(Result currentSettings) {
        this.currentSettings = currentSettings;
    }
}
