package com.artioml.practice.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.artioml.practice.data.CommunityProvider;
import com.artioml.practice.inject.ServiceLocator;
import com.artioml.practice.interfaces.TaskExecutionListener;
import com.artioml.practice.models.Result;
import com.artioml.practice.models.Settings;
import com.artioml.practice.utils.PunchSpeedApplication;

import java.util.List;

/**
 * Created by Polina P on 20.02.2017.
 */

public class BestResultsAsyncTask extends GenericAsyncTask<Settings, List<Result>> {

    private static final String TAG = BestResultsAsyncTask.class.getSimpleName();

    @Override
    protected List<Result> doInBackground(Settings... params) {
        communityProvider.setPunchParameters(params[0]);
        List<Result> results = communityProvider.getBestResults();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "Community list size: " + results.size());
        return results;
    }

}
