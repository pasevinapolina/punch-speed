package com.artioml.practice.asynctasks;

import com.artioml.practice.models.AverageValuePair;
import com.artioml.practice.models.Result;
import com.artioml.practice.models.Settings;

/**
 * Created by Polina P on 23.02.2017.
 */

public class AverageValuesAsyncTask extends GenericAsyncTask<Settings, AverageValuePair> {

    @Override
    protected AverageValuePair doInBackground(Settings... params) {
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
