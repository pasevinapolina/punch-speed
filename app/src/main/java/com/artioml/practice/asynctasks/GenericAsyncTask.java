package com.artioml.practice.asynctasks;

import android.os.AsyncTask;

import com.artioml.practice.data.CommunityProvider;
import com.artioml.practice.inject.ServiceLocator;
import com.artioml.practice.interfaces.TaskExecutionListener;
import com.artioml.practice.utils.PunchSpeedApplication;

/**
 * Created by Polina P on 23.02.2017.
 */

public abstract class GenericAsyncTask<Params, Result> extends AsyncTask<Params, Void, Result> {

    protected TaskExecutionListener taskListener;
    protected CommunityProvider communityProvider;

    public void addTaskListener(TaskExecutionListener taskListener) {
        this.taskListener = taskListener;
    }

    public void removeTaskListener() {
        this.taskListener = null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        ServiceLocator.setContext(null); //PunchSpeedApplication.getContext());
        communityProvider = ServiceLocator.getCommunityProvider();
        taskListener.onStarted();
    }

    @Override
    protected void onPostExecute(Result result) {
        super.onPostExecute(result);
        if(taskListener != null) {
            if(result != null) {
                taskListener.onCompleted(result);
            } else {
                taskListener.onError();
            }
        }
    }
}
