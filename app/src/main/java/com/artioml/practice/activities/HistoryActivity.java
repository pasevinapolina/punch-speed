package com.artioml.practice.activities;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.artioml.practice.fragments.SettingsDialog;
import com.artioml.practice.interfaces.SettingsChangeListener;
import com.artioml.practice.interfaces.impl.HistorySettingsChangeListener;
import com.artioml.practice.preferences.SettingsPreferenceManager;
import com.artioml.practice.asynctasks.HistoryListLoader;
import com.artioml.practice.views.ItemDivider;
import com.artioml.practice.R;
import com.artioml.practice.adapters.HistoryAdapter;
import com.artioml.practice.data.DatabaseDescription.History;
import com.artioml.practice.models.Result;
import com.artioml.practice.fragments.HistorySettingsDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Artiom L on 30.01.2017.
 */

public class HistoryActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Result>>, SettingsDialog.SettingsDialogListener {

    private final static String TAG = HistoryActivity.class.getSimpleName();

    private static final String HISTORY_SETTINGS = "historySettings";
    private static final String SETTINGS_DIALOG = "historySettingsDialog";
    private static final String _DESC = " DESC";

    public static final int LOADER_ID = 1;

    private ArrayList<Result> history;
    private HistoryAdapter adapter;
    private RecyclerView recyclerView;

    private SettingsChangeListener settingsChangeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        history = new ArrayList<>();

        settingsChangeListener = new HistorySettingsChangeListener(this, getWindow().getDecorView().getRootView());
        settingsChangeListener.fillSettingsPanel();

        recyclerView = (RecyclerView) findViewById(R.id.historyRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new HistoryAdapter(this, history);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new ItemDivider(this));

        getSupportLoaderManager().initLoader(LOADER_ID, savedInstanceState, this);

        //db = (PracticeDatabaseHelper.getInstance(this)).getReadableDatabase();

        //initHistory();

        ImageButton settingsHistoryButton = (ImageButton) findViewById(R.id.settingsHistoryButton);
        settingsHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new HistorySettingsDialog()).show(getSupportFragmentManager(), SETTINGS_DIALOG);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_history, menu);
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
                preferenceManager.setSortOrderPreference(History.COLUMN_REACTION + _DESC);
                break;
            case (R.id.acceleration_sort):
                preferenceManager.setSortOrderPreference(History.COLUMN_ACCELERATION + _DESC);
                break;
            case (R.id.date_sort):
                preferenceManager.setSortOrderPreference(History.COLUMN_DATE + _DESC);
                break;
        }
        getSupportLoaderManager().restartLoader(LOADER_ID, null, this);

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "onResume");
        settingsChangeListener.fillSettingsPanel();
        //initHistory();
        getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
        super.onResume();
    }

    @Override
    public void updateSettings() {
        settingsChangeListener.fillSettingsPanel();
        //initHistory();
        getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
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

