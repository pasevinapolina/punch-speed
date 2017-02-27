package com.artioml.practice.asynctasks;

import android.util.Log;

/**
 * Created by Polina P on 23.02.2017.
 */

public class ChangeLoginAsyncTask extends GenericAsyncTask<String, String> {

    public static final String TAG = ChangeLoginAsyncTask.class.getSimpleName();

    @Override
    protected String doInBackground(String... params) {
        String newLogin = null;
        if(params != null) {
            newLogin = params[0];
            if(!(communityProvider.setCurrentLogin(newLogin))) {
                newLogin = null;
            }
        }
        Log.i(TAG, "New Login: " + newLogin);
        return newLogin;
    }
}
