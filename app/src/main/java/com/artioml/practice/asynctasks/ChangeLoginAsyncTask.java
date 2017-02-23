package com.artioml.practice.asynctasks;

import com.artioml.practice.preferences.LoginPrefernceManager;

/**
 * Created by Polina P on 23.02.2017.
 */

public class ChangeLoginAsyncTask extends GenericAsyncTask<String, Boolean> {

    @Override
    protected Boolean doInBackground(String... params) {
        String newLogin = "";
        if(params != null) {
            newLogin = params[0];
        }
        return communityProvider.setCurrentLogin(newLogin);
    }
}
