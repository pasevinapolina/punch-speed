package com.artioml.practice;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.artioml.practice.data.HistoryDatabaseProvider;
import com.artioml.practice.data.HistoryProvider;
import com.artioml.practice.data.DatabaseDescription.History;
import com.artioml.practice.data.PunchType;
import com.artioml.practice.data.Result;
import com.artioml.practice.inject.ServiceLocator;

import java.util.ArrayList;

/**
 * Created by Artiom L on 30.01.2017.
 */

public class HistoryActivity extends AppCompatActivity {

    private static final String HISTORY_SETTINGS = "historySettings";
    private static final String HAND = "pref_hand";
    private static final String GLOVES = "pref_gloves";
    private static final String POSITION = "pref_position";
    private static final String PUNCH_TYPE = "pref_punchType";
    private static final String SORT_ORDER = "pref_sortOrder";
    private static final String _DESC = " DESC";

    private ArrayList<Result> history;
    private HistoryProvider historyProvider;
    private HistoryAdapter adapter;
    //private SQLiteDatabase db;

//    private String hand;
//    private String gloves;
//    private String position;
//    private int punchType;
//    private String sortOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        historyProvider = ServiceLocator.getHistoryProvider(null); //new HistoryDatabaseProvider(this);
        history = new ArrayList<>();

        fillSettingsPanel();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.historyRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HistoryAdapter(this, history);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new ItemDivider(this));

        //db = (PracticeDatabaseHelper.getInstance(this)).getReadableDatabase();
        initHistory();

        ImageButton settingsHistoryButton = (ImageButton) findViewById(R.id.settingsHistoryButton);
        settingsHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new HistorySettingsDialog(HistoryActivity.this)).show();
            }
        });

        //MenuItem speedMenuItem = (MenuItem) findViewById(R.id.speed_sort);
        //Drawable icon = speedMenuItem.getIcon();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_history, menu);
        //selectedItem = 0;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        SharedPreferences sharedPreferences =
                getSharedPreferences(HISTORY_SETTINGS, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        switch (item.getItemId()) {
            case (R.id.speed_sort):
                edit.putString(SORT_ORDER, History.COLUMN_SPEED + _DESC);
                break;
            case (R.id.reaction_sort):
                edit.putString(SORT_ORDER, History.COLUMN_REACTION);
                break;
            case (R.id.acceleration_sort):
                edit.putString(SORT_ORDER, History.COLUMN_ACCELERATION + _DESC);
                break;
            case (R.id.date_sort):
                edit.putString(SORT_ORDER, History.COLUMN_DATE + _DESC);
                break;
        }
        edit.apply();

        //sortOrder =
        historyProvider.setSortOrder(
                sharedPreferences.getString(SORT_ORDER, History.COLUMN_DATE + _DESC));
        initHistory();
        /*if (item.getItemId() == R.id.speed_sort ||
                item.getItemId() == R.id.reaction_sort ||
                item.getItemId() == R.id.acceleration_sort ||
                item.getItemId() == R.id.date_sort) {
            if (item.isChecked()) {
                Drawable drawable = item.getIcon();
                drawable.clearColorFilter();
                item.setIcon(drawable);
                item.setIcon(drawable);
                item.setChecked(false);
            }
        }*//*} else {
                Drawable drawable = item.getIcon();
                int color = ContextCompat.getColor(this, R.color.colorGreyDark);
                drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                drawable = DrawableCompat.wrap(drawable);
                item.setIcon(drawable);
                item.setChecked(true);
            }*/
        /*switch (item.getItemId()) {
            case R.id.vibrate:
            case R.id.dont_vibrate:
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        fillSettingsPanel();
        initHistory();
        super.onResume();
    }

    private void initHistory () {

        historyProvider.clearHistory();
        historyProvider.addDataSet();

        history.clear();
        history.addAll(historyProvider.getHistoryList());

        adapter.notifyDataSetChanged();
    }

    private void fillSettingsPanel(){
        SharedPreferences sharedPreferences =
                getSharedPreferences(HISTORY_SETTINGS, Context.MODE_PRIVATE);

        historyProvider.setSortOrder(
                sharedPreferences.getString(SORT_ORDER, History.COLUMN_DATE + _DESC));

        int punchType = sharedPreferences.getInt(PUNCH_TYPE, 0);
        ((TextView) findViewById(R.id.typeHistoryTextView)).setText(getResources().getStringArray(
                R.array.punch_type_history_list)[punchType]);

        String hand = sharedPreferences.getString(HAND, PunchType.DOESNT_MATTER.getValue());
        ((ImageView) findViewById(R.id.handsHistoryImageView)).setImageDrawable(
                ContextCompat.getDrawable(this, getResources().getIdentifier(
                        "ic_" + hand + "_hand", "drawable", getPackageName())));

        String gloves = sharedPreferences.getString(GLOVES, PunchType.DOESNT_MATTER.getValue());
        ((ImageView) findViewById(R.id.glovesHistoryImageView)).setImageDrawable(
                ContextCompat.getDrawable(this, getResources().getIdentifier(
                        "ic_gloves_" + gloves, "drawable", getPackageName())));

        String position = sharedPreferences.getString(POSITION, PunchType.DOESNT_MATTER.getValue());
        ((ImageView) findViewById(R.id.positionHistoryImageView)).setImageDrawable(
                ContextCompat.getDrawable(this, getResources().getIdentifier(
                        "ic_punch_" + position + "_step", "drawable", getPackageName())));

        if(historyProvider instanceof HistoryDatabaseProvider) {
            ((HistoryDatabaseProvider) historyProvider)
                    .setPunchParameters(punchType, hand, gloves, position);
        }
    }
}

