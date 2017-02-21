package com.artioml.practice.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.artioml.practice.R;
import com.artioml.practice.preferences.SettingsPreferenceManager;
import com.artioml.practice.utils.PunchType;

/**
 * Created by Polina P on 20.02.2017.
 */

public class MainSettingsDialog extends AppCompatDialogFragment {

    public static final String TAG = MainSettingsDialog.class.getSimpleName();
    private static final String MAIN_SETTINGS = "mainSettings";

    private EditText weightEditText;
    private Spinner spinner;

    private RadioButton leftHandButton;
    private RadioButton rightHandButton;
    private RadioButton glovesOnButton;
    private RadioButton glovesOffButton;
    private RadioButton withStepButton;
    private RadioButton withoutStepButton;

    private String hand;
    private String gloves;
    private String position;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
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
        View contentView = inflater.inflate(R.layout.dialog_main_settings, container, false);
        init(contentView);
        setChecked();

        contentView.findViewById(R.id.menuTopButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commitChanges();
                getDialog().dismiss();
            }
        });
        getDialog().setContentView(contentView);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        Window window = getDialog().getWindow();
        if(window != null) {
            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.TOP);
        }
    }

    @Override
    public void onDestroyView() {
        Log.i(TAG, "onDestroyView");
        commitChanges();
        ((SettingsDialogListener)getActivity()).updateSettings();
        super.onDestroyView();
    }

    //    public MainSettingsDialog(final Context context) {
//        //super(getContext, R.style.SettingsTheme);
//        super(context);
//
//        this.context = context;
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        View contentView = getLayoutInflater().inflate(R.layout.dialog_main_settings, null);
//        setContentView(contentView);
//        setCancelable(true);
//        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        getWindow().setGravity(Gravity.TOP);
//
//        init();
//
//        glovesOnButton.setOnCheckedChangeListener(onCheckedChangeListener);
//        glovesOffButton.setOnCheckedChangeListener(onCheckedChangeListener);
//
//        rightHandButton.setOnCheckedChangeListener(onCheckedChangeListener);
//        leftHandButton.setOnCheckedChangeListener(onCheckedChangeListener);
//
//        withoutStepButton.setOnCheckedChangeListener(onCheckedChangeListener);
//        withStepButton.setOnCheckedChangeListener(onCheckedChangeListener);
//
//        setChecked();
//
//        findViewById(R.id.menuTopButton).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                commitChanges();
//                dismiss();
//            }
//        });
//
//        setOnDismissListener(new DialogInterface.OnDismissListener(){
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                ((SettingsDialogListener) context).updateSettings();
//            }
//        });
//    }
//
//    final CompoundButton.OnCheckedChangeListener onCheckedChangeListener =
//            new CompoundButton.OnCheckedChangeListener() {
//
//                @Override
//                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                    switch (compoundButton.getId()) {
//                        case (R.id.leftHandButton):
//                            changeFilter(leftHandButton, rightHandButton, !b);
//                            break;
//                        case (R.id.rightHandButton):
//                            changeFilter(leftHandButton, rightHandButton, b);
//                            break;
//                        case (R.id.withStepButton):
//                            changeFilter(withStepButton, withoutStepButton, !b);
//                            break;
//                        case (R.id.withoutStepButton):
//                            changeFilter(withStepButton, withoutStepButton, b);
//                            break;
//                        case (R.id.glovesOnButton):
//                            changeFilter(glovesOffButton, glovesOnButton, b);
//                            glovesWeight.setVisibility(b ? View.VISIBLE : View.GONE);
//                            break;
//                        case (R.id.glovesOffButton):
//                            changeFilter(glovesOffButton, glovesOnButton, !b);
//                            glovesWeight.setVisibility(!b ? View.VISIBLE : View.GONE);
//                            break;
//                    }
//                }
//            };
//
//    private void changeFilter(RadioButton firstButton, RadioButton secondButton, boolean b) {
//        if (b) applyFilter(firstButton, secondButton);
//        else applyFilter(secondButton, firstButton);
//    }
//
//    private void applyFilter(RadioButton firstButton, RadioButton secondButton) {
//        Drawable drawable = firstButton.getCompoundDrawables()[1];
//        int color = ContextCompat.getColor(context, R.color.colorGreyDark);
//        drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
//        drawable = DrawableCompat.wrap(drawable);
//        firstButton.setCompoundDrawables(null, drawable, null, null);
//
//        drawable = secondButton.getCompoundDrawables()[1];
//        drawable.clearColorFilter();
//        secondButton.setCompoundDrawables(null, drawable, null, null);
//    }

    private void init(View contentView) {
        SettingsPreferenceManager preferenceManager =
                new SettingsPreferenceManager(getActivity(), MAIN_SETTINGS);
        hand = preferenceManager.getHandPreference();
        gloves = preferenceManager.getGlovesPreference();
        position = preferenceManager.getPositionPreference();

        leftHandButton = (RadioButton)  contentView.findViewById(R.id.leftHandButton);
        rightHandButton = (RadioButton) contentView.findViewById(R.id.rightHandButton);
        glovesOnButton = (RadioButton)  contentView.findViewById(R.id.glovesOnButton);
        glovesOffButton = (RadioButton) contentView.findViewById(R.id.glovesOffButton);
        withStepButton = (RadioButton)  contentView.findViewById(R.id.withStepButton);
        withoutStepButton = (RadioButton) contentView.findViewById(R.id.withoutStepButton);

        weightEditText = (EditText) contentView.findViewById(R.id.weightEditText);
        weightEditText.setText(preferenceManager.getGlovesWeightPreference());

        spinner = (Spinner) contentView.findViewById(R.id.punchTypeSpinner);
        spinner.setSelection(preferenceManager.getPunchTypePreference());
    }

    private void setChecked() {
        if (PunchType.getTypeByValue(hand) == PunchType.LEFT_HAND)
            leftHandButton.setChecked(true);
        else rightHandButton.setChecked(true);

        if (PunchType.getTypeByValue(gloves) == PunchType.GLOVES_ON)
            glovesOnButton.setChecked(true);
        else glovesOffButton.setChecked(true);

        if (PunchType.getTypeByValue(position) == PunchType.WITH_STEP)
            withStepButton.setChecked(true);
        else withoutStepButton.setChecked(true);
    }

    private void commitChanges(){
        SettingsPreferenceManager preferenceManager =
                new SettingsPreferenceManager(getActivity(), MAIN_SETTINGS);

        PunchType hand = leftHandButton.isChecked() ? PunchType.LEFT_HAND : PunchType.RIGHT_HAND;
        PunchType gloves = glovesOnButton.isChecked() ? PunchType.GLOVES_ON : PunchType.GLOVES_OFF;
        PunchType position = withStepButton.isChecked() ? PunchType.WITH_STEP : PunchType.WITHOUT_STEP;

        preferenceManager.setPunchPreferences(hand, gloves,
                weightEditText.getText().toString(), position,
                spinner.getSelectedItemPosition());
    }

    public interface SettingsDialogListener {
        void updateSettings();
    }
}