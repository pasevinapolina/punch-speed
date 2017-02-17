package com.artioml.practice.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.AsyncTaskLoader;
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

import com.artioml.practice.data.DatabaseDescription;
import com.artioml.practice.interfaces.SettingsChangeListener;
import com.artioml.practice.interfaces.impl.HistorySettingsChangeListener;
import com.artioml.practice.views.ItemDivider;
import com.artioml.practice.R;
import com.artioml.practice.adapters.HistoryAdapter;
import com.artioml.practice.data.HistoryDatabaseProvider;
import com.artioml.practice.data.HistoryProvider;
import com.artioml.practice.data.DatabaseDescription.History;
import com.artioml.practice.utils.PunchType;
import com.artioml.practice.models.Result;
import com.artioml.practice.fragments.HistorySettingsDialog;
import com.artioml.practice.inject.ServiceLocator;

import java.util.ArrayList;

/**
 * Created by Artiom L on 30.01.2017.
 */

public class HistoryActivity extends AppCompatActivity {

    private static final String HISTORY_SETTINGS = "historySettings";
    private static final String SORT_ORDER = "pref_sortOrder";
    private static final String _DESC = " DESC";

    private ArrayList<Result> history;
    private HistoryProvider historyProvider;
    private HistoryAdapter adapter;

    private SettingsChangeListener settingsChangeListener;
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

        historyProvider = ServiceLocator.getHistoryProvider(this); //new HistoryDatabaseProvider(null);
        history = new ArrayList<>();

        settingsChangeListener = new HistorySettingsChangeListener(this, getWindow().getDecorView().getRootView());
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

    public void updateSettings() {
        fillSettingsPanel();
        initHistory();
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
                sharedPreferences.getString(SORT_ORDER, DatabaseDescription.History.COLUMN_DATE + _DESC));

        settingsChangeListener.fillSettingsPanel();
        Result settings = ((HistorySettingsChangeListener)settingsChangeListener).getCurrentSettings();

        if(historyProvider instanceof HistoryDatabaseProvider) {
            ((HistoryDatabaseProvider) historyProvider)
                    .setPunchParameters(settings.getPunchType(), settings.getHand(),
                            settings.getGloves(), settings.getPosition());
        }
    }


    class GetHistoryLoader extends AsyncTaskLoader<ArrayList<Result>>{

        private HistoryProvider historyProvider;

        public GetHistoryLoader(Context context) {
            super(context);
            historyProvider = ServiceLocator.getHistoryProvider(context);
        }

        @Override
        public ArrayList<Result> loadInBackground() {
            ArrayList<Result> historyList = new ArrayList<>();
            historyList = historyProvider.getHistoryList();
            return null;
        }
    }
}

