package com.artioml.practice.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.IdRes;

import com.artioml.practice.data.DatabaseDescription;
import com.artioml.practice.utils.PunchType;
import com.artioml.practice.utils.SortOrder;

/**
 * Created by Polina P on 17.02.2017.
 */

public class SettingsPreferenceManager {

    private static final String HAND = "pref_hand";
    private static final String GLOVES = "pref_gloves";
    private static final String GLOVES_WEIGHT = "pref_glovesWeight";
    private static final String POSITION = "pref_position";
    private static final String PUNCH_TYPE = "pref_punchType";

    private static final String SORT_ORDER = "pref_sortOrder";
    private static final String SORT_TYPE = "pref_sortType";
    private static final String SORT_ID = "pref_sortId";

    private static SharedPreferences sharedPreferences;

    public SettingsPreferenceManager(final Context context, String preferenceFileKey) {
        sharedPreferences = context.getSharedPreferences(preferenceFileKey, Context.MODE_PRIVATE);
    }

    public int getPunchTypePreference() {
        return sharedPreferences.getInt(PUNCH_TYPE, 0);
    }

    public String getHandPreference() {
        return sharedPreferences.getString(HAND, PunchType.RIGHT_HAND.getValue());
    }

    public String getHandPreference(PunchType defaultValue) {
        return sharedPreferences.getString(HAND, defaultValue.getValue());
    }

    public String getGlovesPreference() {
        return sharedPreferences.getString(GLOVES, PunchType.GLOVES_ON.getValue());
    }

    public String getGlovesPreference(PunchType defaultValue) {
        return sharedPreferences.getString(GLOVES, defaultValue.getValue());
    }

    public String getGlovesWeightPreference() {
        return sharedPreferences.getString(GLOVES_WEIGHT, "14");
    }

    public String getPositionPreference() {
        return sharedPreferences.getString(POSITION, PunchType.WITHOUT_STEP.getValue());
    }

    public String getPositionPreference(PunchType defaultValue) {
        return sharedPreferences.getString(POSITION, defaultValue.getValue());
    }

    public String getSortOrderPreference() {
        return sharedPreferences.getString(SORT_ORDER, DatabaseDescription.History.COLUMN_SPEED);
    }

    public int getSortOrderIdPreference() {
        return sharedPreferences.getInt(SORT_ID, 0);
    }

    public SortOrder getSortTypePreference() {
        String pref = sharedPreferences.getString(SORT_TYPE, SortOrder.DESC.getValue());
        return SortOrder.getTypeByValue(pref);
    }

    public void setSortOrderPreference(String sortOrder, SortOrder sortType, int sortOrderId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SORT_ORDER, sortOrder)
                .putString(SORT_TYPE, sortType.getValue())
                .putInt(SORT_ID, sortOrderId);
        editor.apply();
    }

    public void setPunchPreferences(PunchType hand, PunchType gloves,
                                    PunchType position, int punchType) {
        sharedPreferences.edit()
                .putString(HAND, hand.getValue())
                .putString(GLOVES, gloves.getValue())
                .putString(POSITION, position.getValue())
                .putInt(PUNCH_TYPE, punchType)
                .apply();
    }

    public void setPunchPreferences(PunchType hand, PunchType gloves, String glovesWeight,
                                    PunchType position, int punchType) {
        sharedPreferences.edit()
                .putString(HAND, hand.getValue())
                .putString(GLOVES, gloves.getValue())
                .putString(GLOVES_WEIGHT, glovesWeight)
                .putString(POSITION, position.getValue())
                .putInt(PUNCH_TYPE, punchType)
                .apply();
    }
}
