package com.artioml.practice.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.artioml.practice.data.HistoryProvider;
import com.artioml.practice.inject.ServiceLocator;
import com.artioml.practice.interfaces.TaskExecutionListener;
import com.artioml.practice.models.Result;
import com.artioml.practice.models.Settings;
import com.artioml.practice.utils.PunchSpeedApplication;

import java.util.List;

/**
 * Created by Polina P on 17.02.2017.
 */

public class HistoryListAsyncTask extends AsyncTask<Settings, Void, List<Result>> {

    private final static String TAG = HistoryListAsyncTask.class.getSimpleName();

    private HistoryProvider historyProvider;
    private TaskExecutionListener taskListener;

    public void addTaskListener(TaskExecutionListener taskListener) {
        this.taskListener = taskListener;
    }

    public void removeTaskListener() {
        this.taskListener = null;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        ServiceLocator.setContext(PunchSpeedApplication.getContext());
        historyProvider = ServiceLocator.getHistoryProvider();
        taskListener.onStarted();
    }

    @Override
    protected List<Result> doInBackground(Settings... params) {
        List<Result> results = null;
        if(params != null) {
            historyProvider.setPunchParameters(params[0]);
            results = historyProvider.getHistoryList();
            Log.i(TAG, "History list size: " + results.size());
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    @Override
    protected void onPostExecute(List<Result> results) {
        super.onPostExecute(results);
        if(results == null) {
            taskListener.onError();
        }
        taskListener.onCompleted(results);
    }
}
