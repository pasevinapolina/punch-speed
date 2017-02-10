package com.artioml.practice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.artioml.practice.data.CommunityListProvider;
import com.artioml.practice.data.CommunityProvider;
import com.artioml.practice.data.DatabaseDescription;

import java.util.ArrayList;

/**
 * Created by Polina P on 05.02.2017.
 */

public class CommunityActivity extends AppCompatActivity {

    private static final String CHANGE_NAME_DIALOG = "changeNameDialog";
    private static final String AVERAGE_VALUES_DIALOG = "averageValuesDialog";
    private static final String LOGOUT_DIALOG = "logoutDialog";

    private static final String COMMUNITY_STORAGE = "communityStorage";
    private static final String IS_LOGGED_IN = "pref_isLoggedIn";
    private static final String LOGIN = "pref_login";

    private CommunityAdapter adapter;
    private ArrayList<Result> communityResults;
    private CommunityProvider communityProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        Intent loginIntent = new Intent(CommunityActivity.this, LoginActivity.class);
        startActivity(loginIntent);

        communityProvider = new CommunityListProvider();
        communityResults = new ArrayList<>();

        communityProvider.addDataSet();
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
                MainSettingsDialog mainSettingsDialog = new MainSettingsDialog(CommunityActivity.this);
                mainSettingsDialog.show();
            }
        });

        RadioButton bestResultsButton = (RadioButton)findViewById(R.id.bestResultsButton);
        RadioButton avgResultsButton = (RadioButton)findViewById(R.id.avgResultsButton);

        bestResultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        avgResultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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
                new AverageValuesDialog().show(manager, AVERAGE_VALUES_DIALOG);
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

        SharedPreferences sharedPreferences = getSharedPreferences(COMMUNITY_STORAGE, MODE_PRIVATE);
        if(sharedPreferences.getBoolean(IS_LOGGED_IN, true)) {

        }
    }


}
