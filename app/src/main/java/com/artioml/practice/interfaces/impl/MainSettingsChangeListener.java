package com.artioml.practice.interfaces.impl;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.artioml.practice.R;
import com.artioml.practice.models.Settings;
import com.artioml.practice.preferences.SettingsPreferenceManager;
import com.artioml.practice.utils.PunchType;
import com.artioml.practice.interfaces.SettingsChangeListener;

/**
 * Created by Polina P on 13.02.2017.
 */

public class MainSettingsChangeListener implements SettingsChangeListener {

    private static final String MAIN_SETTINGS = "mainSettings";

    private Activity activity;
    private View rootView;

    public MainSettingsChangeListener(Activity activity, View rootView) {
        this.activity = activity;
        this.rootView = rootView;
    }

    @Override
    public Settings fillSettingsPanel(){
        //SharedPreferences sharedPreferences = activity.getSharedPreferences(MAIN_SETTINGS, Context.MODE_PRIVATE);
        SettingsPreferenceManager preferenceManager = new SettingsPreferenceManager(activity, MAIN_SETTINGS);

        int punchType = preferenceManager.getPunchTypePreference();
        ((TextView) rootView.findViewById(R.id.punchTypeView)).setText(activity.getResources().getStringArray(
                R.array.punch_type_list)[punchType]);


        String hand = "ic_" + preferenceManager.getHandPreference() + "_hand";
        ((ImageView) rootView.findViewById(R.id.handView)).setImageResource( //ContextCompat.getDrawable(this,
                activity.getResources().getIdentifier(hand, "drawable", activity.getPackageName()));

        String gloves = "ic_gloves_" + preferenceManager.getGlovesPreference();
        ((ImageView) rootView.findViewById(R.id.glovesView)).setImageResource( //setImageDrawable(ContextCompat.getDrawable(this,
                activity.getResources().getIdentifier(gloves, "drawable", activity.getPackageName()));

        String glovesWeight = preferenceManager.getGlovesWeightPreference();
        if (gloves.compareTo("ic_gloves_off") == 0)
            rootView.findViewById(R.id.weightView).setVisibility(View.GONE);
        else {
            rootView.findViewById(R.id.weightView).setVisibility(View.VISIBLE);
            ((TextView) rootView.findViewById(R.id.weightView)).setText(glovesWeight);
        }

        String position = "ic_punch_" +  preferenceManager.getPositionPreference() + "_step";
        ((ImageView) rootView.findViewById(R.id.positionView)).setImageResource( //setImageDrawable(ContextCompat.getDrawable(this,
                activity.getResources().getIdentifier(position, "drawable", activity.getPackageName()));
        return new Settings(punchType, hand, gloves, glovesWeight, position);
    }
}
