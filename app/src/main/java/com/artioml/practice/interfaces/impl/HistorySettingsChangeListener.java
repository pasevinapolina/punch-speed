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
import com.artioml.practice.models.Settings;
import com.artioml.practice.preferences.SettingsPreferenceManager;
import com.artioml.practice.utils.PunchType;
import com.artioml.practice.utils.SortOrder;

/**
 * Created by Polina P on 17.02.2017.
 */

public class HistorySettingsChangeListener implements SettingsChangeListener {

    private static final String HISTORY_SETTINGS = "historySettings";
    private static final String DRAWABLE = "drawable";

    private Activity activity;
    private View rootView;
    private Settings currentSettings;

    public HistorySettingsChangeListener(final Activity activity, View rootView) {
        this.activity = activity;
        this.rootView = rootView;
    }

    @Override
    public Settings fillSettingsPanel() {
        SettingsPreferenceManager preferenceManager =
                new SettingsPreferenceManager(activity, HISTORY_SETTINGS);

        int punchType = preferenceManager.getPunchTypePreference();
        ((TextView)rootView.findViewById(R.id.typeHistoryTextView)).setText(activity.getResources().getStringArray(
                R.array.punch_type_history_list)[punchType]);

        String hand = preferenceManager.getHandPreference(PunchType.DOESNT_MATTER);
        ((ImageView)rootView.findViewById(R.id.handsHistoryImageView)).setImageResource(
                activity.getResources().getIdentifier(
                        "ic_" + hand + "_hand", DRAWABLE, activity.getPackageName()));

        String gloves = preferenceManager.getGlovesPreference(PunchType.DOESNT_MATTER);
        ((ImageView) rootView.findViewById(R.id.glovesHistoryImageView)).setImageResource(
                activity.getResources().getIdentifier(
                        "ic_gloves_" + gloves, DRAWABLE, activity.getPackageName()));

        String position = preferenceManager.getPositionPreference(PunchType.DOESNT_MATTER);
        ((ImageView) rootView.findViewById(R.id.positionHistoryImageView)).setImageResource(
                activity.getResources().getIdentifier(
                        "ic_punch_" + position + "_step", DRAWABLE, activity.getPackageName()));

        String sortColumn = preferenceManager.getSortOrderPreference();
        SortOrder orderType = preferenceManager.getSortTypePreference();

        currentSettings = new Settings(punchType, hand, gloves, position, sortColumn, orderType);
        return currentSettings;
    }

    public Settings getCurrentSettings() {
        return currentSettings;
    }

    public void setCurrentSettings(Settings currentSettings) {
        this.currentSettings = currentSettings;
    }
}
