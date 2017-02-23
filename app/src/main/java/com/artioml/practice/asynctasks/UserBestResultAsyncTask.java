package com.artioml.practice.asynctasks;

import com.artioml.practice.models.Result;

/**
 * Created by Polina P on 23.02.2017.
 */

public class UserBestResultAsyncTask extends GenericAsyncTask<String, Result> {

    private static final String TAG = UserBestResultAsyncTask.class.getSimpleName();

    @Override
    protected Result doInBackground(String... params) {
        Result userResult = null;
        if(params != null) {
            communityProvider.setCurrentLogin(params[0]);
            userResult = communityProvider.getBestUserResult();
        }
        return userResult;
    }

}
