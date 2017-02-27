package com.artioml.practice.activities;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Parcelable;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
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
    private static final String IS_LOADED = "isLoaded";
    private static final String KEY_RECYCLER_STATE = "recyclerState";
    private static final String _DESC = " DESC";

    private ArrayList<Result> historyList = new ArrayList<>();
    private HistoryAdapter adapter;
    private RecyclerView recyclerView;
    private Bundle mBundleRecyclerViewState;

    private Settings settings;
    private SettingsChangeListener settingsChangeListener;

    private boolean isLoaded;
    private HistoryListAsyncTask historyListTask;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setActionBar();

        if(savedInstanceState != null) {
            isLoaded = savedInstanceState.getBoolean(IS_LOADED);
        }

        settingsChangeListener = new HistorySettingsChangeListener(this, getWindow().getDecorView().getRootView());
        settings = settingsChangeListener.fillSettingsPanel();

        recyclerView = (RecyclerView) findViewById(R.id.historyRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new HistoryAdapter(this, historyList);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new ItemDivider(this));

        ImageButton settingsHistoryButton = (ImageButton) findViewById(R.id.settingsHistoryButton);
        settingsHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new HistorySettingsDialog()).show(getSupportFragmentManager(), SETTINGS_DIALOG);
            }
        });

        restoreAsyncTask();
    }

    private void restoreAsyncTask() {
        historyListTask = (HistoryListAsyncTask) getLastCustomNonConfigurationInstance();
        if(historyListTask == null) {
            historyListTask = new HistoryListAsyncTask();
            historyListTask.addTaskListener(this);
            //if(!isLoaded) {
                historyListTask.execute(settings);
            //}
        } else {
            progressDialog = ProgressDialog.show(this, getString(R.string.collecting_data),
                    getString(R.string.collecting_data));
        }
        historyListTask.addTaskListener(this);
    }

    private void setActionBar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_history, menu);

        SettingsPreferenceManager preferenceManager =
                new SettingsPreferenceManager(this, HISTORY_SETTINGS);
        MenuItem checkedItem = menu.getItem(0).getSubMenu().getItem(preferenceManager.getSortOrderIdPreference());
        checkedItem.setChecked(true);
        styleMenuItem(checkedItem);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        SettingsPreferenceManager preferenceManager =
                new SettingsPreferenceManager(this, HISTORY_SETTINGS);
        String sortOrder = History.COLUMN_SPEED + _DESC;

        switch (item.getItemId()) {
            case (R.id.speed_sort):
                sortOrder = History.COLUMN_SPEED + _DESC;
                break;
            case (R.id.reaction_sort):
                sortOrder = History.COLUMN_REACTION + _DESC;
                break;
            case (R.id.acceleration_sort):
                sortOrder = History.COLUMN_ACCELERATION + _DESC;
                break;
            case (R.id.date_sort):
                sortOrder = History.COLUMN_DATE + _DESC;
                break;
            case (android.R.id.home):
                finish();
                return super.onOptionsItemSelected(item);
            case (R.id.submenu_sort):
                return super.onOptionsItemSelected(item);
        }

        item.setChecked(true);
        styleMenuItem(item);

        settings.setSortOrder(sortOrder);
        preferenceManager.setSortOrderPreference(sortOrder, item.getOrder());

        restoreAsyncTask();

        return super.onOptionsItemSelected(item);
    }

    public void styleMenuItem(MenuItem item) {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Menu subMenu = toolbar.getMenu().getItem(0).getSubMenu();

        for(int i = 0; i < subMenu.size(); ++i) {
            changeMenuItemColor(subMenu.getItem(i), R.color.colorTextBlack);
            subMenu.getItem(i).setIcon(R.drawable.ic_sort_item_48dp);
        }
        changeMenuItemColor(item, R.color.colorRedDark);
        item.setIcon(R.drawable.ic_sort_item_48dp_checked);
    }

    private void changeMenuItemColor(MenuItem item, @ColorRes int color) {
        SpannableString s = new SpannableString(item.getTitle().toString());
        s.setSpan(new ForegroundColorSpan(
                ContextCompat.getColor(this, color)), 0, s.length(), 0);
        item.setTitle(s);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
        restoreRecyclerViewState();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(IS_LOADED, isLoaded);
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
    protected void onPause() {
        super.onPause();
        saveRecyclerViewState();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
        if(progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void updateSettings() {
        settings = settingsChangeListener.fillSettingsPanel();
        restoreAsyncTask();
    }

    @Override
    public void onStarted() {
        if(progressDialog == null || !progressDialog.isShowing()) {
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
        isLoaded = true;
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

    private void saveRecyclerViewState() {
        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = recyclerView.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
    }

    private void restoreRecyclerViewState() {
        if (mBundleRecyclerViewState != null) {
            Parcelable listState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            recyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }
    }
}

