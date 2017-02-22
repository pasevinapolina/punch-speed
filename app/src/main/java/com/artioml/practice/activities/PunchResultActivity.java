package com.artioml.practice.activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.artioml.practice.R;
import com.artioml.practice.data.PracticeDatabaseHelper;
import com.artioml.practice.data.DatabaseDescription.History;
import com.artioml.practice.views.ProgressItem;
import com.artioml.practice.views.ResultSeekBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class PunchResultActivity extends AppCompatActivity {

    private static final String MAIN_SETTINGS = "mainSettings";
    private static final String HAND = "pref_hand";
    private static final String GLOVES = "pref_gloves";
    private static final String GLOVES_WEIGHT = "pref_glovesWeight";
    private static final String POSITION = "pref_position";
    private static final String PUNCH_TYPE = "pref_punchType";

    private ArrayList<ProgressItem> progressItemList;

    private int punchType;
    private String hand;
    private String gloves;
    private String position;
    private String weight;

    float speed;
    float reaction;
    float acceleration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_punch_result);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        speed = getIntent().getFloatExtra("speed", 45);
        reaction  = getIntent().getFloatExtra("reaction", 20);
        acceleration = getIntent().getFloatExtra("acceleration", 30);

        fillSettingsPanel();
        initSeekBars();
        insertHistory();

        findViewById(R.id.resultOkButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavUtils.navigateUpFromSameTask(PunchResultActivity.this);
            }
        });
    }

    private void initSeekBars() {

        LinearLayout speedResultItem = (LinearLayout) findViewById(R.id.speedResultItem);
        LinearLayout reactionResultItem = (LinearLayout) findViewById(R.id.reactionResultItem);
        LinearLayout accelerationResultItem = (LinearLayout) findViewById(R.id.accelerationResultItem);

        ResultSeekBar punchSpeedSeekBar = (ResultSeekBar) speedResultItem.getChildAt(2);
        punchSpeedSeekBar.setProgress((int) getProgress(speed, History.COLUMN_SPEED));

        ResultSeekBar reactionSpeedSeekBar = (ResultSeekBar) reactionResultItem.getChildAt(2);
        reactionSpeedSeekBar.setProgress(
                (int) (100 - getProgress(reaction, History.COLUMN_REACTION)));

        ResultSeekBar accelerationSeekBar = (ResultSeekBar) accelerationResultItem.getChildAt(2);
        accelerationSeekBar.setProgress((int) getProgress(acceleration, History.COLUMN_ACCELERATION));

        ((TextView) ((LinearLayout) speedResultItem.getChildAt(0)).getChildAt(0))
                .setText(getString(R.string.speed_title));
        ((TextView) ((LinearLayout) speedResultItem.getChildAt(0)).getChildAt(1))
                .setText(getString(R.string.speed_result, speed));

        ((TextView) ((LinearLayout) reactionResultItem.getChildAt(0)).getChildAt(0))
                .setText(getString(R.string.reaction_title));
        ((TextView) ((LinearLayout) reactionResultItem.getChildAt(0)).getChildAt(1))
                .setText(getString(R.string.reaction_result, reaction));

        ((TextView) ((LinearLayout) accelerationResultItem.getChildAt(0)).getChildAt(0))
                .setText(getString(R.string.acceleration_title));
        ((TextView) ((LinearLayout) accelerationResultItem.getChildAt(0)).getChildAt(1))
                .setText(Html.fromHtml((getString(R.string.acceleration_result, acceleration))));

        initData();
        initSeekBarData(punchSpeedSeekBar);
        initSeekBarData(reactionSpeedSeekBar);
        initSeekBarData(accelerationSeekBar);
    }

    private float getProgress(float value, String column) {
        SQLiteDatabase db = (PracticeDatabaseHelper.getInstance(this)).getReadableDatabase();

        Cursor cursor = db.query(
                History.TABLE_NAME,
                new String[] {column},
                column + " < ? ", new String[] {Float.toString(value)},
                null, null, null);

        float progress = cursor.getCount();

        if (progress == 0)
            return 50;

        cursor = db.query(
                History.TABLE_NAME,
                new String[] {column},
                null, null, null, null, null);

        progress /= cursor.getCount();
        cursor.close();
        return progress * 100;
    }

    private float getAccelerationProgress(float acceleration) {
        SQLiteDatabase db = (PracticeDatabaseHelper.getInstance(this)).getReadableDatabase();

        Cursor cursor = db.query(
                History.TABLE_NAME,
                new String[] {History.COLUMN_ACCELERATION},
                History.COLUMN_ACCELERATION + " < ? ", new String[] {Float.toString(speed)},
                null, null, null);

        float progress = cursor.getCount();

        cursor = db.query(
                History.TABLE_NAME,
                new String[] {History.COLUMN_SPEED},
                null, null, null, null, null);

        progress /= cursor.getCount();
        cursor.close();
        return progress * 100;
    }

    private void insertHistory() {
        //SimpleDateFormat sdf = new SimpleDateFormat("ss.mm.HH.dd.MM.yy", Locale.ROOT);
        SimpleDateFormat sdf = new SimpleDateFormat("yy.MM.dd HH.mm.ss", Locale.ROOT);
        Calendar calendar = Calendar.getInstance();

        SQLiteDatabase db = (PracticeDatabaseHelper.getInstance(this)).getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(History.COLUMN_PUNCH_TYPE, punchType);
        cv.put(History.COLUMN_HAND, hand);
        cv.put(History.COLUMN_GLOVES, gloves);
        cv.put(History.COLUMN_GLOVES_WEIGHT, weight);
        cv.put(History.COLUMN_POSITION, position);
        cv.put(History.COLUMN_SPEED, speed);
        cv.put(History.COLUMN_REACTION, reaction);
        cv.put(History.COLUMN_ACCELERATION, acceleration);
        cv.put(History.COLUMN_DATE, sdf.format(calendar.getTime()));
        long id = db.insert(History.TABLE_NAME, null, cv);
        Log.i("INSERT", "" + id);

    }

    private void fillSettingsPanel(){
        SharedPreferences sharedPreferences = getSharedPreferences(MAIN_SETTINGS, Context.MODE_PRIVATE);

        punchType = sharedPreferences.getInt(PUNCH_TYPE, 0);
        ((TextView) findViewById(R.id.punchTypeResultTextView)).setText(getResources().getStringArray(
                R.array.punch_type_list)[punchType]);

        hand = sharedPreferences.getString(HAND, "right");
        ((ImageView) findViewById(R.id.handsResultImageView)).setImageDrawable(
                ContextCompat.getDrawable(PunchResultActivity.this, getResources().
                        getIdentifier("ic_" + hand + "_hand", "drawable", getPackageName())));

        gloves = sharedPreferences.getString(GLOVES, "off");
        ((ImageView) findViewById(R.id.glovesResultImageView)).setImageDrawable(
                ContextCompat.getDrawable(PunchResultActivity.this, getResources().
                        getIdentifier("ic_gloves_" + gloves, "drawable", getPackageName())));

        weight = sharedPreferences.getString(GLOVES_WEIGHT, "80");
        if (gloves.compareTo("off") == 0)
            findViewById(R.id.glovesResultTextView).setVisibility(View.GONE);
        else {
            ((TextView) findViewById(R.id.glovesResultTextView)).setText(weight);
            findViewById(R.id.glovesResultTextView).setVisibility(View.VISIBLE);
        }

        position = sharedPreferences.getString(POSITION, "with") ;
        ((ImageView) findViewById(R.id.positionResultImageView)).setImageDrawable(
                ContextCompat.getDrawable(PunchResultActivity.this, getResources().
                        getIdentifier("ic_punch_" + position + "_step", "drawable", getPackageName())));
    }

    private void initData(){
        float redSpan = 0.25f;
        float greenSpan = 0.25f;
        float yellowSpan = 0.5f;
        progressItemList = new ArrayList<>();
        // red span
        ProgressItem mProgressItem = new ProgressItem();
        mProgressItem.setProgressItemPercentage(redSpan * 100);
        mProgressItem.setColor(R.color.colorSeekBarRed);
        progressItemList.add(mProgressItem);
        // yellow span
        mProgressItem = new ProgressItem();
        mProgressItem.setProgressItemPercentage(yellowSpan * 100);
        mProgressItem.setColor(R.color.colorSeekBarYellow);
        progressItemList.add(mProgressItem);
        // green span
        mProgressItem = new ProgressItem();
        mProgressItem.setProgressItemPercentage(greenSpan * 100);
        mProgressItem.setColor(R.color.colorSeekBarGreen);
        progressItemList.add(mProgressItem);
    }

    private void initSeekBarData(final ResultSeekBar seekBar){
        seekBar.post(new Runnable() {
            @Override
            public void run() {
                seekBar.setThumb(drawThumb(seekBar.getWidth()));
            }
        });
        seekBar.initData(progressItemList);
        seekBar.invalidate();
    }

    public BitmapDrawable drawThumb(int width){
        Path path = new Path();
        path.moveTo(width / 24 / 2, 0);
        path.lineTo(width / 24, 3 * width / 24 / 4);
        path.lineTo(width / 24, 9 * width / 24 / 4);
        path.lineTo(0, 9 * width / 24 / 4);
        path.lineTo(0, 3 * width / 24 / 4);
        path.close();

        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setStyle(Paint.Style.FILL);
        p.setColor(ContextCompat.getColor(this, R.color.colorGreyDarkest));

        Bitmap bitmap = Bitmap.createBitmap(width / 24, width / 12, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawPath(path, p);

        return new BitmapDrawable(getResources(), bitmap);
    }
}