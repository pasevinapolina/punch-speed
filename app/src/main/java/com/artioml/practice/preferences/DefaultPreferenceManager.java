package com.artioml.practice.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by user on 20.02.2017.
 */

public class DefaultPreferenceManager {

    private static final String IS_FIRST_TIME = "pref_isFirstTime";
    private SharedPreferences sharedPreferences;

    public DefaultPreferenceManager(final Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean getFirstTimePreference() {
        return sharedPreferences.getBoolean(IS_FIRST_TIME, true);
    }

    public void setFirstTimePreference(boolean isFirstTime) {
        sharedPreferences.edit()
                .putBoolean(IS_FIRST_TIME, isFirstTime)
                .apply();
    }
}
