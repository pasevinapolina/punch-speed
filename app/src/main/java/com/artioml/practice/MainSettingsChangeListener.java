package com.artioml.practice;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.artioml.practice.data.PunchType;

/**
 * Created by Polina P on 13.02.2017.
 */

public class MainSettingsChangeListener implements SettingsChangeListener {

    private static final String MAIN_SETTINGS = "mainSettings";
    private static final String HAND = "pref_hand";
    private static final String GLOVES = "pref_gloves";
    private static final String GLOVES_WEIGHT = "pref_glovesWeight";
    private static final String POSITION = "pref_position";
    private static final String PUNCH_TYPE = "pref_punchType";

    private Activity activity;

    public MainSettingsChangeListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void fillSettingsPanel(){
        SharedPreferences sharedPreferences = activity.getSharedPreferences(MAIN_SETTINGS, Context.MODE_PRIVATE);

        ((TextView) activity.findViewById(R.id.punchTypeView)).setText(activity.getResources().getStringArray(
                R.array.punch_type_list)[sharedPreferences.getInt(PUNCH_TYPE, 0)]);


        String hand = "ic_" + sharedPreferences.getString(HAND, PunchType.RIGHT_HAND.getValue()) + "_hand";
        ((ImageView) activity.findViewById(R.id.handView)).setImageResource( //ContextCompat.getDrawable(this,
                activity.getResources().getIdentifier(hand, "drawable", activity.getPackageName()));

        String gloves = "ic_gloves_" + sharedPreferences.getString(GLOVES, PunchType.GLOVES_OFF.getValue());
        ((ImageView) activity.findViewById(R.id.glovesView)).setImageResource( //setImageDrawable(ContextCompat.getDrawable(this,
                activity.getResources().getIdentifier(gloves, "drawable", activity.getPackageName()));

        if (gloves.compareTo("ic_gloves_off") == 0)
            activity.findViewById(R.id.weightView).setVisibility(View.GONE);
        else {
            activity.findViewById(R.id.weightView).setVisibility(View.VISIBLE);
            ((TextView) activity.findViewById(R.id.weightView)).setText(sharedPreferences.getString(GLOVES_WEIGHT, "80"));
        }

        String position = "ic_punch_" +  sharedPreferences.getString(POSITION, PunchType.WITH_STEP.getValue()) + "_step";
        ((ImageView) activity.findViewById(R.id.positionView)).setImageResource( //setImageDrawable(ContextCompat.getDrawable(this,
                activity.getResources().getIdentifier(position, "drawable", activity.getPackageName()));
    }
}
