package com.artioml.practice.asynctasks;

import android.util.Log;

import com.artioml.practice.models.Result;
import com.artioml.practice.models.Settings;

import java.util.List;

/**
 * Created by Polina P on 22.02.2017.
 */

public class AverageResultsAsyncTask extends GenericAsyncTask<Settings, List<Result>> {

    private static final String TAG = AverageResultsAsyncTask.class.getSimpleName();

    @Override
    protected List<Result> doInBackground(Settings... params) {
        if(params == null) {
            return null;
        }
        Settings settings = params[0];
        List<Result> results = communityProvider.getAverageResults(settings);
        Log.i(TAG, "Community list size: " + results.size());
        return results;
    }
}
