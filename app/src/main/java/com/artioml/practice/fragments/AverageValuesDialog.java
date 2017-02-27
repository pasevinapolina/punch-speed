package com.artioml.practice.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.artioml.practice.asynctasks.AverageValuesAsyncTask;
import com.artioml.practice.interfaces.TaskExecutionListener;
import com.artioml.practice.interfaces.impl.MainSettingsChangeListener;
import com.artioml.practice.R;
import com.artioml.practice.interfaces.SettingsChangeListener;
import com.artioml.practice.models.AverageValuePair;
import com.artioml.practice.models.Result;
import com.artioml.practice.models.Settings;

/**
 * Created by Polina P on 06.02.2017.
 */
public class AverageValuesDialog extends AppCompatDialogFragment
        implements TaskExecutionListener {

    private static final String TAG = AverageValuesDialog.class.getSimpleName();

    private AverageValuesAsyncTask avgAsyncTask;
    private Settings settings;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.i(TAG, "onCreateDialog");
        final Dialog dialog = new Dialog(getActivity());
        if(dialog.getWindow() != null) {
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.dialog_average_values, container, false);

        Log.i(TAG, "onCreateView");
        Button okButton = (Button)contentView.findViewById(R.id.dialogOkButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        SettingsChangeListener settingsChangeListener =
                new MainSettingsChangeListener(getActivity(), contentView);
        settings = settingsChangeListener.fillSettingsPanel();

        return contentView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        Log.i(TAG, "onCreate");
    }

    @Override
    public void onStart() {
        super.onStart();

        Window window = getDialog().getWindow();
        if(window != null) {
            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        avgAsyncTask = new AverageValuesAsyncTask();
        avgAsyncTask.addTaskListener(this);
        avgAsyncTask.execute(settings);
    }

    @Override
    public void onDestroyView() {;
        Log.i(TAG, "onDestroyView");
        if (getDialog() != null && getRetainInstance())
            getDialog().setDismissMessage(null);
        super.onDestroyView();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.i(TAG, "onDismiss");
    }

    private void fillResultTable(AverageValuePair averageValues) {

        Result userResult = averageValues.getUserResult();
        Result communityResult = averageValues.getCommunityResult();

        //1 column
        TextView mySpeedTextView = (TextView)getView().findViewById(R.id.mySpeedTextView);
        mySpeedTextView.setText(getString(R.string.speed_result, userResult.getSpeed()));

        TextView myReactionTextView = (TextView)getView().findViewById(R.id.myReactionTextView);
        myReactionTextView.setText(getString(R.string.reaction_result, userResult.getReaction()));

        TextView myAccelerationTextView = (TextView)getView().findViewById(R.id.myAccelerationTextView);
        myAccelerationTextView.setText(Html.fromHtml(
                getString(R.string.acceleration_result, userResult.getAcceleration())));

        //2 column
        TextView avgSpeedTextView = (TextView)getView().findViewById(R.id.avgSpeedTextView);
        avgSpeedTextView.setText(getString(R.string.speed_result, communityResult.getSpeed()));

        TextView avgReactionTextView = (TextView)getView().findViewById(R.id.avgReactionTextView);
        avgReactionTextView.setText(getString(R.string.reaction_result, communityResult.getReaction()));

        TextView avgAccelerationTextView = (TextView)getView().findViewById(R.id.avgAccelerationTextView);
        avgAccelerationTextView.setText(Html.fromHtml(
                getString(R.string.acceleration_result, communityResult.getAcceleration())));

        boldBestValue(userResult.getSpeed(), communityResult.getSpeed(),
                mySpeedTextView, avgSpeedTextView);

        boldBestValue(userResult.getReaction(), communityResult.getReaction(),
            avgReactionTextView, myReactionTextView);

        boldBestValue(userResult.getAcceleration(), communityResult.getAcceleration(),
            myAccelerationTextView, avgAccelerationTextView);

    }

    private void boldBestValue(float firstValue, float secondValue,
                               TextView firstTextView, TextView secondTextView) {
        if(firstValue > secondValue)
            firstTextView.setTypeface(Typeface.DEFAULT_BOLD);
        if(secondValue > firstValue)
            secondTextView.setTypeface(Typeface.DEFAULT_BOLD);
    }

    @Override
    public void onStarted() {

    }

    @Override
    public void onCompleted(Object... result) {
        if(result != null && result[0] instanceof AverageValuePair) {
            fillResultTable((AverageValuePair) result[0]);
        }
    }

    @Override
    public void onError() {
        Toast.makeText(getDialog().getContext(),
                getString(R.string.community_error), Toast.LENGTH_LONG)
                .show();
        dismiss();
    }
}
