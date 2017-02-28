package com.artioml.practice.asynctasks;

import android.util.Log;

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
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "doInBackground Login: " + login);
        return communityProvider.loginUser(login);
    }
}
