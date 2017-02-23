package com.artioml.practice.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.artioml.practice.data.CommunityProvider;
import com.artioml.practice.data.DatabaseDescription;
import com.artioml.practice.inject.ServiceLocator;
import com.artioml.practice.interfaces.TaskExecutionListener;

/**
 * Created by Polina P on 20.02.2017.
 */

public class LoginAsyncTask extends GenericAsyncTask<String, Boolean> {

    private static final String TAG = LoginAsyncTask.class.getSimpleName();

    @Override
    protected Boolean doInBackground(String... params) {
        if(params == null) {
            return false;
        }
        String login = params[0];
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "doInBackground Login: " + login);
        return communityProvider.loginUser(login);
    }
}
