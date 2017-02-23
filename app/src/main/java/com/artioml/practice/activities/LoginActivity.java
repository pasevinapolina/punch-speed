package com.artioml.practice.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.artioml.practice.asynctasks.LoginAsyncTask;
import com.artioml.practice.interfaces.TaskExecutionListener;
import com.artioml.practice.preferences.LoginPrefernceManager;

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

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginEditText = (EditText) findViewById(R.id.loginEditText);

        final LoginPrefernceManager prefernceManager = new LoginPrefernceManager(this);
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
            loginEditText.setText(savedInstanceState.getString(LOGIN));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(LOGIN, loginEditText.getText().toString());
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
    public void onStarted() {
        if(progressDialog == null || !progressDialog.isShowing()) {
            progressDialog = ProgressDialog.show(LoginActivity.this, getString(R.string.loading),
                    getString(R.string.please_wait));
        }
    }

    @Override
    public void onCompleted(Object... result) {
        if(progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        LoginPrefernceManager prefernceManager = new LoginPrefernceManager(this);
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
        }
        loginTask.addTaskListener(this);
    }
}
