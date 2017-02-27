package com.artioml.practice.asynctasks;

import android.util.Log;

import com.artioml.practice.fragments.AverageValuesDialog;
import com.artioml.practice.models.AverageValuePair;
import com.artioml.practice.models.Result;
import com.artioml.practice.models.Settings;

/**
 * Created by Polina P on 23.02.2017.
 */

public class AverageValuesAsyncTask extends GenericAsyncTask<Settings, AverageValuePair> {

    public static final String TAG = AverageValuesDialog.class.getSimpleName();

    @Override
    protected AverageValuePair doInBackground(Settings... params) {
        Log.i(TAG, "getting average pair...");
        if(params == null) {
            return null;
        }
        Settings settings = params[0];
        communityProvider.setPunchParameters(settings);
        Result userResult = communityProvider.getAverageUserResult();
        Result communityResult = communityProvider.getAverageCommunityResult();
        return new AverageValuePair(userResult, communityResult);
    }
}
