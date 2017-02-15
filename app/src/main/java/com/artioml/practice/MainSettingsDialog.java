package com.artioml.practice;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatDelegate;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import com.artioml.practice.data.PunchType;

import static com.artioml.practice.data.PunchType.GLOVES_OFF;
import static com.artioml.practice.data.PunchType.GLOVES_ON;
import static com.artioml.practice.data.PunchType.LEFT_HAND;
import static com.artioml.practice.data.PunchType.RIGHT_HAND;
import static com.artioml.practice.data.PunchType.WITHOUT_STEP;
import static com.artioml.practice.data.PunchType.WITH_STEP;

/**
 * Created by Artiom L on 27.01.2017.
 */

public class MainSettingsDialog extends /*BottomSheet*/Dialog {

    private static final String MAIN_SETTINGS = "mainSettings";
    private static final String HAND = "pref_hand";
    private static final String GLOVES = "pref_gloves";
    private static final String GLOVES_WEIGHT = "pref_glovesWeight";
    private static final String POSITION = "pref_position";
    private static final String PUNCH_TYPE = "pref_punchType";

    private Context context;
    private EditText weightEditText;
    private View glovesWeight;
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

    static
    {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public MainSettingsDialog(final Context context) {
        //super(context, R.style.SettingsTheme);
        super(context);

        this.context = context;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View contentView = getLayoutInflater().inflate(R.layout.dialog_main_settings, null);
        setContentView(contentView);
        setCancelable(true);
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.TOP);

        init();

        glovesOnButton.setOnCheckedChangeListener(onCheckedChangeListener);
        glovesOffButton.setOnCheckedChangeListener(onCheckedChangeListener);

        rightHandButton.setOnCheckedChangeListener(onCheckedChangeListener);
        leftHandButton.setOnCheckedChangeListener(onCheckedChangeListener);

        withoutStepButton.setOnCheckedChangeListener(onCheckedChangeListener);
        withStepButton.setOnCheckedChangeListener(onCheckedChangeListener);

        setChecked();

        findViewById(R.id.menuTopButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commitChanges();
                dismiss();
            }
        });

        setOnDismissListener(new DialogInterface.OnDismissListener(){
            @Override
            public void onDismiss(DialogInterface dialog) {
                ((SettingsDialogListener) context).updateSettings();
            }
        });
    }

    final CompoundButton.OnCheckedChangeListener onCheckedChangeListener =
            new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    switch (compoundButton.getId()) {
                        case (R.id.leftHandButton):
                            changeFilter(leftHandButton, rightHandButton, !b);
                            break;
                        case (R.id.rightHandButton):
                            changeFilter(leftHandButton, rightHandButton, b);
                            break;
                        case (R.id.withStepButton):
                            changeFilter(withStepButton, withoutStepButton, !b);
                            break;
                        case (R.id.withoutStepButton):
                            changeFilter(withStepButton, withoutStepButton, b);
                            break;
                        case (R.id.glovesOnButton):
                            changeFilter(glovesOffButton, glovesOnButton, b);
                            glovesWeight.setVisibility(b ? View.VISIBLE : View.GONE);
                            break;
                        case (R.id.glovesOffButton):
                            changeFilter(glovesOffButton, glovesOnButton, !b);
                            glovesWeight.setVisibility(!b ? View.VISIBLE : View.GONE);
                            break;
                    }
                }
    };

    private void changeFilter(RadioButton firstButton, RadioButton secondButton, boolean b) {
        if (b) applyFilter(firstButton, secondButton);
        else applyFilter(secondButton, firstButton);
    }

    private void applyFilter(RadioButton firstButton, RadioButton secondButton) {
        Drawable drawable = firstButton.getCompoundDrawables()[1];
        int color = ContextCompat.getColor(context, R.color.colorGreyDark);
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        drawable = DrawableCompat.wrap(drawable);
        firstButton.setCompoundDrawables(null, drawable, null, null);

        drawable = secondButton.getCompoundDrawables()[1];
        drawable.clearColorFilter();
        secondButton.setCompoundDrawables(null, drawable, null, null);
    }

    private void init() {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(MAIN_SETTINGS, Context.MODE_PRIVATE);
        hand = sharedPreferences.getString(HAND, RIGHT_HAND.getValue());
        gloves = sharedPreferences.getString(GLOVES, GLOVES_ON.getValue());
        position = sharedPreferences.getString(POSITION, WITHOUT_STEP.getValue());

        leftHandButton = (RadioButton) findViewById(R.id.leftHandButton);
        rightHandButton = (RadioButton) findViewById(R.id.rightHandButton);
        glovesOnButton = (RadioButton) findViewById(R.id.glovesOnButton);
        glovesOffButton = (RadioButton) findViewById(R.id.glovesOffButton);
        withStepButton = (RadioButton) findViewById(R.id.withStepButton);
        withoutStepButton = (RadioButton) findViewById(R.id.withoutStepButton);

        weightEditText = (EditText) findViewById(R.id.weightEditText);
        weightEditText.setText(sharedPreferences.getString(GLOVES_WEIGHT, "80"));

        glovesWeight = findViewById(R.id.glovesWeight);

        spinner = (Spinner) findViewById(R.id.punchTypeSpinner);
        spinner.setSelection(sharedPreferences.getInt(PUNCH_TYPE, 0));
    }

    private void setChecked() {
        PunchType type = PunchType.getTypeByValue(hand);
        if (type == LEFT_HAND)
            leftHandButton.setChecked(true);
        else rightHandButton.setChecked(true);

        if ((type = PunchType.getTypeByValue(gloves)) == GLOVES_ON)
            glovesOnButton.setChecked(true);
        else glovesOffButton.setChecked(true);

        if ((type = PunchType.getTypeByValue(position)) == WITH_STEP)
            withStepButton.setChecked(true);
        else withoutStepButton.setChecked(true);
    }

    private void commitChanges(){
        context.getSharedPreferences(MAIN_SETTINGS, Context.MODE_PRIVATE)
                .edit()
                .putString(HAND, leftHandButton.isChecked() ? LEFT_HAND.getValue() : RIGHT_HAND.getValue())
                .putString(GLOVES, glovesOnButton.isChecked() ? GLOVES_ON.getValue() : GLOVES_OFF.getValue())
                .putString(POSITION, withStepButton.isChecked() ? WITH_STEP.getValue() : WITHOUT_STEP.getValue())
                .putString(GLOVES_WEIGHT, weightEditText.getText().toString())
                .putInt(PUNCH_TYPE, spinner.getSelectedItemPosition())
                .apply();
    }

    public interface SettingsDialogListener {
        void updateSettings();
    }

}
