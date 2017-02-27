package com.artioml.practice.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.artioml.practice.R;
import com.artioml.practice.asynctasks.AsyncTaskContainer;
import com.artioml.practice.asynctasks.LoginAsyncTask;
import com.artioml.practice.interfaces.TaskExecutionListener;
import com.artioml.practice.preferences.LoginPreferenceManager;

public class LoginActivity extends AppCompatActivity implements TaskExecutionListener {

    public static final String TAG = LoginActivity.class.getSimpleName();

    private static final String LOGIN = "pref_login";

    private ProgressDialog progressDialog;
    private EditText loginEditText;

    private LoginAsyncTask loginTask;
    private String userLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setActionBar();

        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginEditText = (EditText) findViewById(R.id.loginEditText);

        final LoginPreferenceManager prefernceManager = new LoginPreferenceManager(this);
        if(prefernceManager.getIsLoggedInPreference()) {
            finish();
        }

        initLoginTask();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin = loginEditText.getText().toString();
                loginTask.execute(userLogin);
            }
        });

        loginEditText.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            userLogin = textView.getText().toString();
                            loginTask.execute(userLogin);
                            return true;
                        }
                        return false;
                    }
                });
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null) {
            userLogin = savedInstanceState.getString(LOGIN);
            loginEditText.setText(userLogin);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(LOGIN, loginEditText.getText().toString());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent mainActivity = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(mainActivity);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        if(loginTask != null) {
            Log.i(TAG, "onRetainCustomNonConfigurationInstance");
            loginTask.removeTaskListener();
        }
        return loginTask;
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
    public void onStarted() {
        Log.i(TAG, "onStarted");
        if(progressDialog == null || progressDialog.isShowing()) {
            progressDialog = ProgressDialog.show(LoginActivity.this, getString(R.string.loading),
                    getString(R.string.please_wait));
        }
    }

    @Override
    public void onCompleted(Object... result) {
        Log.i(TAG, "onCompleted");
        if(progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        LoginPreferenceManager prefernceManager = new LoginPreferenceManager(this);
        prefernceManager.setLoginPreferences(true, userLogin);
        finish();
    }

    @Override
    public void onError() {
        if(progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        Toast.makeText(this, R.string.login_error, Toast.LENGTH_LONG)
                .show();
        loginTask = null;
    }

    private void initLoginTask() {
        loginTask = (LoginAsyncTask)getLastCustomNonConfigurationInstance();
        if(loginTask == null){
            Log.i(TAG, "Create new task");
            loginTask = new LoginAsyncTask();
        } else {
            Log.i(TAG, "Task restored");
            progressDialog = ProgressDialog.show(LoginActivity.this, getString(R.string.loading),
                    getString(R.string.please_wait));
            loginEditText.setText(userLogin);
        }
        loginTask.addTaskListener(this);
    }

    private void setActionBar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }
}
