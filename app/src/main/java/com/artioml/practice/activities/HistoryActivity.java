package com.artioml.practice.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.artioml.practice.data.DatabaseDescription;
import com.artioml.practice.interfaces.SettingsChangeListener;
import com.artioml.practice.interfaces.impl.HistorySettingsChangeListener;
import com.artioml.practice.preferences.SettingsPreferenceManager;
import com.artioml.practice.utils.HistoryListLoader;
import com.artioml.practice.views.ItemDivider;
import com.artioml.practice.R;
import com.artioml.practice.adapters.HistoryAdapter;
import com.artioml.practice.data.HistoryDatabaseProvider;
import com.artioml.practice.data.HistoryProvider;
import com.artioml.practice.data.DatabaseDescription.History;
import com.artioml.practice.models.Result;
import com.artioml.practice.fragments.HistorySettingsDialog;
import com.artioml.practice.inject.ServiceLocator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Artiom L on 30.01.2017.
 */

public class HistoryActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Result>> {

    private final static String TAG = HistoryActivity.class.getSimpleName();

    private static final String HISTORY_SETTINGS = "historySettings";
    private static final String _DESC = " DESC";

    public static final int LOADER_ID = 1;
    private Loader<List<Result>> mLoader;

    private ArrayList<Result> history;
    private HistoryAdapter adapter;
    private RecyclerView recyclerView;

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

        Log.i(TAG, "onCreate");

        history = new ArrayList<>();

        settingsChangeListener = new HistorySettingsChangeListener(this, getWindow().getDecorView().getRootView());
        fillSettingsPanel();

        recyclerView = (RecyclerView) findViewById(R.id.historyRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new HistoryAdapter(this, history);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new ItemDivider(this));

        mLoader = getSupportLoaderManager().initLoader(LOADER_ID, savedInstanceState, this);

        //db = (PracticeDatabaseHelper.getInstance(this)).getReadableDatabase();

        //initHistory();

        ImageButton settingsHistoryButton = (ImageButton) findViewById(R.id.settingsHistoryButton);
        settingsHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new HistorySettingsDialog(HistoryActivity.this)).show();
            }
        });

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

        SettingsPreferenceManager preferenceManager =
                new SettingsPreferenceManager(this, HISTORY_SETTINGS);

        switch (item.getItemId()) {
            case (R.id.speed_sort):
                preferenceManager.setSortOrderPreference(History.COLUMN_SPEED + _DESC);
                break;
            case (R.id.reaction_sort):
                preferenceManager.setSortOrderPreference(History.COLUMN_REACTION);
                break;
            case (R.id.acceleration_sort):
                preferenceManager.setSortOrderPreference(History.COLUMN_ACCELERATION + _DESC);
                break;
            case (R.id.date_sort):
                preferenceManager.setSortOrderPreference(History.COLUMN_DATE + _DESC);
                break;
        }
        mLoader = getSupportLoaderManager().restartLoader(LOADER_ID, null, this);

        //sortOrder =
        //historyProvider.setSortOrder(
        //        sharedPreferences.getString(SORT_ORDER, History.COLUMN_DATE + _DESC));
        //initHistory();
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
        Log.i(TAG, "onResume");
        fillSettingsPanel();
        //initHistory();
        mLoader = getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
        super.onResume();
    }

    public void updateSettings() {
        fillSettingsPanel();
        //initHistory();
        mLoader = getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
    }

//    private void initHistory () {
//
//        historyProvider.clearHistory();
//        historyProvider.addDataSet();
//
//        history.clear();
//        history.addAll(historyProvider.getHistoryList());
//
//        adapter.notifyDataSetChanged();
//    }

    private void fillSettingsPanel(){
        SharedPreferences sharedPreferences =
                getSharedPreferences(HISTORY_SETTINGS, Context.MODE_PRIVATE);

        //historyProvider.setSortOrder(
          //      sharedPreferences.getString(SORT_ORDER, DatabaseDescription.History.COLUMN_DATE + _DESC));

        settingsChangeListener.fillSettingsPanel();
        //Result settings = ((HistorySettingsChangeListener)settingsChangeListener).getCurrentSettings();

        //if(historyProvider instanceof HistoryDatabaseProvider) {
//            ((HistoryDatabaseProvider) historyProvider)
//                    .setPunchParameters(settings.getPunchType(), settings.getHand(),
//                            settings.getGloves(), settings.getPosition());
//        }
    }

    @Override
    public Loader<List<Result>> onCreateLoader(int id, Bundle args) {
        HistoryListLoader asyncTaskLoader = new HistoryListLoader(this, history);
        asyncTaskLoader.forceLoad();
        Log.i(TAG, "onCreateLoader");
        return asyncTaskLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<Result>> loader, List<Result> data) {

        adapter.notifyDataSetChanged();
        recyclerView.setVisibility(View.VISIBLE);
        Log.i(TAG, "onLoadFinish");
    }

    @Override
    public void onLoaderReset(Loader<List<Result>> loader) {
        recyclerView.setAdapter(null);
        Log.i(TAG, "onLoaderReset");
    }
}

