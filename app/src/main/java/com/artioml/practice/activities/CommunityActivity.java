package com.artioml.practice.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
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
import com.artioml.practice.asynctasks.BestResultsAsyncTask;
import com.artioml.practice.fragments.AverageValuesDialog;
import com.artioml.practice.fragments.ChangeNameDialog;
import com.artioml.practice.adapters.CommunityAdapter;
import com.artioml.practice.interfaces.TaskExecutionListener;
import com.artioml.practice.models.Settings;
import com.artioml.practice.views.ItemDivider;
import com.artioml.practice.fragments.LogoutDialog;
import com.artioml.practice.interfaces.impl.MainSettingsChangeListener;
import com.artioml.practice.fragments.MainSettingsDialog;
import com.artioml.practice.R;
import com.artioml.practice.interfaces.SettingsChangeListener;
import com.artioml.practice.data.CommunityListProvider;
import com.artioml.practice.data.CommunityProvider;
import com.artioml.practice.models.Result;

import java.util.ArrayList;

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

    private CommunityAdapter adapter;
    private ArrayList<Result> communityResults;
    private CommunityProvider communityProvider;

    private BottomSheetBehavior mBottomSheetBehavior;
    private RadioButton bestResultsButton;
    private RadioButton avgResultsButton;

    private SettingsChangeListener settingsChangeListener;

    private ProgressDialog progressDialog;
    private AsyncTaskContainer taskContainer;

    private Result currentResult;
    private Settings settings;

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        if(taskContainer != null) {
            taskContainer.removeExecutionListener();
        }
        return taskContainer;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent loginIntent = new Intent(CommunityActivity.this, LoginActivity.class);
        startActivity(loginIntent);

        communityProvider = new CommunityListProvider();
        communityResults = new ArrayList<>();

        communityProvider.getBestResults();
        communityResults = ((CommunityListProvider)communityProvider).getCommunityResults();

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
                fillBottomSheet(communityProvider.getBestUserResult());
            }
        });

        avgResultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillBottomSheet(communityProvider.getAverageUserResult());
            }
        });

        settingsChangeListener = new MainSettingsChangeListener(this, getWindow().getDecorView().getRootView());
        settingsChangeListener.fillSettingsPanel();

        setBottomSheet();
        fillBottomSheet(communityProvider.getBestUserResult());

        restoreAsyncTask();
    }

    private void fillBottomSheet(Result result) {
        LinearLayout bottomSheet = (LinearLayout) findViewById(R.id.bottom_sheet);

        TextView loginTextView = (TextView)bottomSheet.findViewById(R.id.loginCommunityTextView);
        loginTextView.setText(result.getUser());

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
            taskContainer = new AsyncTaskContainer(this);
            BestResultsAsyncTask bestResultsAsyncTask = new BestResultsAsyncTask();
            taskContainer.setCommunityListTask(bestResultsAsyncTask);
            taskContainer.getCommunityListTask().execute(settings);
        }
//            CommunityListAsyncTask communityListTask = taskContainer.getCommunityListTask();
//            if(communityListTask != null) {
//                if(communityListTask.getStatus().equals(AsyncTask.Status.FINISHED)) {
//                    //taskContainer.addExecutionListener(this);
//                }
//            }
    }


    @Override
    protected void onResume() {
        settingsChangeListener.fillSettingsPanel();
        super.onResume();
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

    @Override
    protected void onStart() {
        super.onStart();

//        SharedPreferences sharedPreferences = getSharedPreferences(COMMUNITY_STORAGE, MODE_PRIVATE);
//        if(sharedPreferences.getBoolean(IS_LOGGED_IN, true)) {
//
//        }
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
    protected void onDestroy() {
        super.onDestroy();
        taskContainer.removeExecutionListener();
    }


    @Override
    public void updateResult(String username) {
        communityProvider.setCurrentLogin(username);
        Result currentResult;
        if(bestResultsButton.isChecked()) {
            currentResult = communityProvider.getBestUserResult();
        } else {
            currentResult = communityProvider.getAverageUserResult();
        }
        fillBottomSheet(currentResult);
    }

    @Override
    public void logout() {
        communityProvider.logout();
    }

    @Override
    public void updateSettings() {
        settingsChangeListener.fillSettingsPanel();
    }

    @Override
    public void onStarted() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(R.string.collecting_data);
        progressDialog.setMessage(getResources().getString(R.string.collecting_data));
        progressDialog.show();
    }

    @Override
    public void onCompleted() {
        if(progressDialog != null) {
            progressDialog.dismiss();
        }
        fillBottomSheet(currentResult);
    }

    @Override
    public void onError() {
        Toast.makeText(this, R.string.community_error, Toast.LENGTH_LONG)
                .show();
    }
}
