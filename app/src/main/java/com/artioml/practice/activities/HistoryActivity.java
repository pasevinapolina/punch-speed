package com.artioml.practice.activities;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.artioml.practice.fragments.SettingsDialog;
import com.artioml.practice.inject.ServiceLocator;
import com.artioml.practice.interfaces.SettingsChangeListener;
import com.artioml.practice.interfaces.TaskExecutionListener;
import com.artioml.practice.interfaces.impl.HistorySettingsChangeListener;
import com.artioml.practice.models.Settings;
import com.artioml.practice.preferences.SettingsPreferenceManager;
import com.artioml.practice.asynctasks.HistoryListAsyncTask;
import com.artioml.practice.utils.PunchSpeedApplication;
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
        implements SettingsDialog.SettingsDialogListener, TaskExecutionListener {

    private final static String TAG = HistoryActivity.class.getSimpleName();

    private static final String HISTORY_SETTINGS = "historySettings";
    private static final String SETTINGS_DIALOG = "historySettingsDialog";
    private static final String _DESC = " DESC";

    private ArrayList<Result> historyList;
    private HistoryAdapter adapter;
    private RecyclerView recyclerView;

    private Settings settings;
    private SettingsChangeListener settingsChangeListener;

    private HistoryListAsyncTask historyListTask;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        ServiceLocator.setContext(PunchSpeedApplication.getContext());

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        historyList = new ArrayList<>();

        settingsChangeListener = new HistorySettingsChangeListener(this, getWindow().getDecorView().getRootView());
        settings = settingsChangeListener.fillSettingsPanel();

        recyclerView = (RecyclerView) findViewById(R.id.historyRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new HistoryAdapter(this, historyList);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new ItemDivider(this));

        //initHistory();

        ImageButton settingsHistoryButton = (ImageButton) findViewById(R.id.settingsHistoryButton);
        settingsHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new HistorySettingsDialog()).show(getSupportFragmentManager(), SETTINGS_DIALOG);
            }
        });

        restoreAsyncTask();
        historyListTask.execute(settings);

    }

    private void restoreAsyncTask() {
        historyListTask = (HistoryListAsyncTask) getLastCustomNonConfigurationInstance();
        if(historyListTask == null) {
            historyListTask = new HistoryListAsyncTask();
            historyListTask.addTaskListener(this);
        }
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
        //getSupportLoaderManager().restartLoader(LOADER_ID, null, this);

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
        settings = settingsChangeListener.fillSettingsPanel();

        //restoreAsyncTask();
        //historyListTask.execute(settings);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        if(historyListTask != null) {
            Log.i(TAG, "onRetainCustomNonConfigurationInstance");
            historyListTask.removeTaskListener();
        }
        return historyListTask;
    }

    @Override
    public void updateSettings() {
        settings = settingsChangeListener.fillSettingsPanel();
        restoreAsyncTask();
        historyListTask.execute(settings);
    }

    @Override
    public void onStarted() {
        if(progressDialog == null) {
            progressDialog = ProgressDialog.show(this, getString(R.string.collecting_data),
                    getString(R.string.collecting_data));
        }
    }

    @Override
    public void onCompleted(Object... result) {
        if(progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        historyList.clear();
        if(result != null) {
            historyList.addAll((List<Result>) result[0]);
        }
        adapter.notifyDataSetChanged();

        historyListTask = null;
    }

    @Override
    public void onError() {
        if(progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        Toast.makeText(this, R.string.community_error, Toast.LENGTH_LONG)
                .show();
        historyListTask = null;
    }
}

