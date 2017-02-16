package com.artioml.practice;

import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.artioml.practice.data.CommunityProvider;
import com.artioml.practice.data.Result;
import com.artioml.practice.inject.ServiceLocator;


/**
 * Created by Polina P on 06.02.2017.
 */
public class AverageValuesDialog extends AppCompatDialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_average_values, null);

        Button okButton = (Button)rootView.findViewById(R.id.dialogOkButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        SettingsChangeListener settingsChangeListener = new MainSettingsChangeListener(getActivity(), rootView);
        settingsChangeListener.fillSettingsPanel();

        fillResultTable(rootView);

        builder.setView(rootView);

        builder.setCancelable(true);
        return builder.create();
    }

    private void fillResultTable(View rootView) {
        CommunityProvider communityProvider = ServiceLocator.getCommunityProvider(null);

        //1 column My Values
        Result userResult = communityProvider.getAverageUserResult();

        TextView mySpeedTextView = (TextView)rootView.findViewById(R.id.mySpeedTextView);
        mySpeedTextView.setText(getResources().getString(R.string.speed_result, userResult.getSpeed()));

        TextView myReactionTextView = (TextView)rootView.findViewById(R.id.myReactionTextView);
        myReactionTextView.setText(getResources().getString(R.string.reaction_result, userResult.getReaction()));

        TextView myAccelerationTextView = (TextView)rootView.findViewById(R.id.myAccelerationTextView);
        myAccelerationTextView.setText(Html.fromHtml(
                getResources().getString(R.string.acceleration_result, userResult.getAcceleration())));

        //2 column Average Values
        Result avgResult = communityProvider.getAverageResults();

        TextView avgSpeedTextView = (TextView)rootView.findViewById(R.id.avgSpeedTextView);
        avgSpeedTextView.setText(getResources().getString(R.string.speed_result, avgResult.getSpeed()));

        TextView avgReactionTextView = (TextView)rootView.findViewById(R.id.avgReactionTextView);
        avgReactionTextView.setText(getResources().getString(R.string.reaction_result, avgResult.getReaction()));

        TextView avgAccelerationTextView = (TextView)rootView.findViewById(R.id.avgAccelerationTextView);
        avgAccelerationTextView.setText(Html.fromHtml(
                getResources().getString(R.string.acceleration_result, avgResult.getAcceleration())));

        boldBestValue(userResult.getSpeed(), avgResult.getSpeed(),
                mySpeedTextView, avgSpeedTextView);

        boldBestValue(userResult.getReaction(), avgResult.getReaction(),
            avgReactionTextView, myReactionTextView);

        boldBestValue(userResult.getAcceleration(), avgResult.getAcceleration(),
            myAccelerationTextView, avgAccelerationTextView);

    }

    private void boldBestValue(float firstValue, float secondValue,
                               TextView firstTextView, TextView secondTextView) {
        if(firstValue > secondValue)
            firstTextView.setTypeface(Typeface.DEFAULT_BOLD);
        if(secondValue > firstValue)
            secondTextView.setTypeface(Typeface.DEFAULT_BOLD);
    }
}
