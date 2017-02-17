package com.artioml.practice.activities;

import android.content.SharedPreferences;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.artioml.practice.R;
import com.artioml.practice.data.CommunityProvider;
import com.artioml.practice.inject.ServiceLocator;

public class LoginActivity extends AppCompatActivity {

    private static final String COMMUNITY_STORAGE = "communityStorage";
    private static final String IS_LOGGED_IN = "pref_isLoggedIn";
    private static final String LOGIN = "pref_login";

    private CommunityProvider communityProvider;
    private EditText loginEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginEditText = (EditText) findViewById(R.id.loginEditText);

        communityProvider = ServiceLocator.getCommunityProvider(this);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if(sharedPreferences.getBoolean(IS_LOGGED_IN, false)) {
            finish();
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = loginEditText.getText().toString();
                boolean isLoggedIn = communityProvider.loginUser(login);
                if(isLoggedIn) {
                    sharedPreferences
                            .edit()
                            .putBoolean(IS_LOGGED_IN, true)
                            .putString(LOGIN, login)
                            .apply();
                    finish();
                }
            }
        });
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null) {
            loginEditText.setText(savedInstanceState.getString(LOGIN));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(LOGIN, loginEditText.getText().toString());
    }
}