package com.artioml.practice.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.artioml.practice.data.DatabaseDescription;
import com.artioml.practice.utils.PunchType;

/**
 * Created by user on 17.02.2017.
 */

public class SettingsPreferenceManager {

    private static final String HAND = "pref_hand";
    private static final String GLOVES = "pref_gloves";
    private static final String GLOVES_WEIGHT = "pref_glovesWeight";
    private static final String POSITION = "pref_position";
    private static final String PUNCH_TYPE = "pref_punchType";
    private static final String SORT_ORDER = "pref_sortOrder";

    private static SharedPreferences sharedPreferences;

    public SettingsPreferenceManager(Context context, String preferenceFileKey) {
        sharedPreferences = context.getSharedPreferences(preferenceFileKey, Context.MODE_PRIVATE);
    }

    public int getPunchTypePreference() {
        return sharedPreferences.getInt(PUNCH_TYPE, 0);
    }

    public String getHandPreference() {
        return sharedPreferences.getString(HAND, PunchType.RIGHT_HAND.getValue());
    }

    public String getGlovesPreference() {
        return sharedPreferences.getString(GLOVES, PunchType.GLOVES_OFF.getValue());
    }

    public String getGlovesWeightPreference() {
        return sharedPreferences.getString(GLOVES_WEIGHT, "80");
    }

    public String getPositionPreference() {
        return sharedPreferences.getString(POSITION, PunchType.WITH_STEP.getValue());
    }

    public String getSortOrderPreference() {
        return sharedPreferences.getString(SORT_ORDER, DatabaseDescription.History.COLUMN_DATE + " DESC");
    }

    public void setSortOrderPreference(String sortOrder) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SORT_ORDER, sortOrder);
        editor.apply();
    }
}
