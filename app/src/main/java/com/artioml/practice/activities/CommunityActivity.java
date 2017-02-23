package com.artioml.practice.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.artioml.practice.asynctasks.AsyncTaskContainer;
import com.artioml.practice.fragments.AverageValuesDialog;
import com.artioml.practice.fragments.ChangeNameDialog;
import com.artioml.practice.adapters.CommunityAdapter;
import com.artioml.practice.inject.ServiceLocator;
import com.artioml.practice.interfaces.TaskExecutionListener;
import com.artioml.practice.models.Settings;
import com.artioml.practice.preferences.LoginPrefernceManager;
import com.artioml.practice.views.ItemDivider;
import com.artioml.practice.fragments.LogoutDialog;
import com.artioml.practice.interfaces.impl.MainSettingsChangeListener;
import com.artioml.practice.fragments.MainSettingsDialog;
import com.artioml.practice.R;
import com.artioml.practice.interfaces.SettingsChangeListener;
import com.artioml.practice.models.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Polina P on 05.02.2017.
 */

public class CommunityActivity extends AppCompatActivity
        implements ChangeNameDialog.ChangeNameListener, LogoutDialog.LogoutListener,
        MainSettingsDialog.SettingsDialogListener, TaskExecutionListener {

    private static final String TAG = CommunityActivity.class.getSimpleName();

    private static final String SETTINGS_DIALOG = "settingsDialog";
    private static final String CHANGE_NAME_DIALOG = "changeNameDialog";
    private static final String AVERAGE_VALUES_DIALOG = "averageValuesDialog";
    private static final String LOGOUT_DIALOG = "logoutDialog";
    private static final String AVG_BUTTON_CHECKED = "checkedButton";

    private CommunityAdapter adapter;
    private List<Result> communityResults;
    private boolean avgIsChecked;

    private BottomSheetBehavior mBottomSheetBehavior;
    private RadioButton bestResultsButton;
    private RadioButton avgResultsButton;

    private SettingsChangeListener settingsChangeListener;

    private ProgressDialog progressDialog;
    private AsyncTaskContainer taskContainer;

    private Result currentResult;
    private Settings settings;
    private String userLogin;

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        if(taskContainer != null) {
            Log.i(TAG, "onRetainCustomNonConfigurationInstance");
            taskContainer.removeTaskListeners();
        }
        return taskContainer;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ServiceLocator.setContext(null);

        Intent loginIntent = new Intent(CommunityActivity.this, LoginActivity.class);
        startActivity(loginIntent);

        LoginPrefernceManager prefernceManager = new LoginPrefernceManager(this);
        userLogin = prefernceManager.getLoginPreference();

        communityResults = new ArrayList<>();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.communityRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CommunityAdapter(this, communityResults);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new ItemDivider(this));

        ImageButton settingsButton = (ImageButton) findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainSettingsDialog mainSettingsDialog = new MainSettingsDialog();
                mainSettingsDialog.show(getSupportFragmentManager(), SETTINGS_DIALOG);
            }
        });

        bestResultsButton = (RadioButton)findViewById(R.id.bestResultsButton);
        avgResultsButton = (RadioButton)findViewById(R.id.avgResultsButton);

        bestResultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restoreAsyncTask();
                taskContainer.getBestResultsTask().execute(settings);
                taskContainer.getUserBestResultTask().execute(userLogin);
            }
        });

        avgResultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restoreAsyncTask();
                taskContainer.getAvgResultsTask().execute(settings);
                taskContainer.getUserAvgResultAsyncTask().execute(userLogin);
            }
        });

        settingsChangeListener = new MainSettingsChangeListener(this, getWindow().getDecorView().getRootView());
        settings = settingsChangeListener.fillSettingsPanel();

        setBottomSheet();

        restoreAsyncTask();
    }

    private void fillBottomSheet(Result result) {
        LinearLayout bottomSheet = (LinearLayout) findViewById(R.id.bottom_sheet);

        TextView loginTextView = (TextView)bottomSheet.findViewById(R.id.loginCommunityTextView);
        loginTextView.setText(userLogin);

        TextView speedTextView = (TextView)bottomSheet.findViewById(R.id.speedCommunityTextView);
        speedTextView.setText(getString(R.string.speed_result, result.getSpeed()));

        TextView reactionTextView = (TextView)bottomSheet.findViewById(R.id.reactionCommunityTextView);
        reactionTextView.setText(getString(R.string.reaction_result, result.getReaction()));

        TextView accelerationTextView = (TextView)bottomSheet.findViewById(R.id.accelerationCommunityTextView);
        accelerationTextView.setText(Html.fromHtml(
                getString(R.string.acceleration_result, result.getAcceleration())));
    }

    private void restoreAsyncTask() {
        taskContainer = (AsyncTaskContainer)getLastCustomNonConfigurationInstance();
        if(taskContainer == null) {
            taskContainer = new AsyncTaskContainer();
            taskContainer.addTaskListeners(this);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        settings = settingsChangeListener.fillSettingsPanel();

        restoreAsyncTask();
        restoreCommunityView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_community, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        FragmentManager manager = getSupportFragmentManager();

        switch (item.getItemId()) {
            case R.id.average_values:
                AverageValuesDialog averageValuesDialog = new AverageValuesDialog();
                averageValuesDialog.show(manager, AVERAGE_VALUES_DIALOG);

                break;
            case R.id.change_username:
                new ChangeNameDialog().show(manager, CHANGE_NAME_DIALOG);
                break;
            case R.id.logout:
                new LogoutDialog().show(manager, LOGOUT_DIALOG);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setBottomSheet() {
        LinearLayout bottomSheet = (LinearLayout) findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(newState == BottomSheetBehavior.STATE_DRAGGING) {
                    Log.i(TAG, "BottomSheet: STATE_DRAGGING");
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        TextView myLoginTextView = (TextView)bottomSheet.findViewById(R.id.loginCommunityTextView);
        myLoginTextView.setTextColor(ContextCompat.getColor(this, R.color.colorRedDark));

        bottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                else if(mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        avgIsChecked = savedInstanceState.getBoolean(AVG_BUTTON_CHECKED);
        Log.i(TAG, "AVG_BUTTON_CHECKED " + avgIsChecked);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(AVG_BUTTON_CHECKED, avgResultsButton.isChecked());

    }

    @Override
    public void updateLogin(String username) {
        userLogin = username;
        LoginPrefernceManager prefernceManager = new LoginPrefernceManager(this);
        prefernceManager.setLoginPreferences(true, username);
        LinearLayout bottomSheet = (LinearLayout) findViewById(R.id.bottom_sheet);
        TextView loginTextView = (TextView)bottomSheet.findViewById(R.id.loginCommunityTextView);
        loginTextView.setText(userLogin);
    }

    @Override
    public void logout() {
        LoginPrefernceManager prefernceManager = new LoginPrefernceManager(this);
        prefernceManager.setLoginPreferences(false, null);
        Intent mainActivity = new Intent(CommunityActivity.this, MainActivity.class);
        startActivity(mainActivity);
    }

    @Override
    public void updateSettings() {
        settings = settingsChangeListener.fillSettingsPanel();
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
        if(result != null) {
            if(result[0] instanceof Result) {
                fillBottomSheet((Result) result[0]);
            }
            else if(result[0] instanceof List){
                onCommunityTaskCompleted((List<Result>)result[0]);
            }
        }
        taskContainer = null;
    }

    @Override
    public void onError() {
        if(progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        Toast.makeText(this, R.string.community_error, Toast.LENGTH_LONG)
                .show();
        taskContainer = null;
    }

    private void onCommunityTaskCompleted(List<Result> resultList) {
        communityResults.clear();
        if(resultList != null) {
            communityResults.addAll(resultList);
        }
        adapter.notifyDataSetChanged();
    }

    private void restoreCommunityView() {
        LoginPrefernceManager prefernceManager = new LoginPrefernceManager(this);
        userLogin = prefernceManager.getLoginPreference();

        if(avgIsChecked) {
            avgResultsButton.setChecked(true);
            taskContainer.getAvgResultsTask().execute(settings);
            taskContainer.getUserAvgResultAsyncTask().execute(userLogin);
        } else {
            bestResultsButton.setChecked(true);
            taskContainer.getBestResultsTask().execute(settings);
            taskContainer.getUserBestResultTask().execute(userLogin);
        }
    }
}
