package com.artioml.practice.asynctasks;

import com.artioml.practice.interfaces.TaskExecutionListener;

/**
 * Created by Polina P on 20.02.2017.
 */

public class AsyncTaskContainer {

    private static final String TAG = AsyncTaskContainer.class.getSimpleName();

    private BestResultsAsyncTask bestResultsTask;
    private AverageResultsAsyncTask avgResultsTask;
    private UserBestResultAsyncTask userBestResultTask;
    private UserAvgResultAsyncTask userAvgResultAsyncTask;

    public AsyncTaskContainer() {
        bestResultsTask = new BestResultsAsyncTask();
        avgResultsTask = new AverageResultsAsyncTask();
        userBestResultTask = new UserBestResultAsyncTask();
        userAvgResultAsyncTask = new UserAvgResultAsyncTask();

    }

    public BestResultsAsyncTask getBestResultsTask() {
        return bestResultsTask;
    }

    public void setBestResultsTask(BestResultsAsyncTask communityListTask) {
        this.bestResultsTask = communityListTask;
    }

    public AverageResultsAsyncTask getAvgResultsTask() {
        return avgResultsTask;
    }

    public void setAvgResultsTask(AverageResultsAsyncTask avgResultsTask) {
        this.avgResultsTask = avgResultsTask;
    }

    public UserBestResultAsyncTask getUserBestResultTask() {
        return userBestResultTask;
    }

    public void setUserBestResultTask(UserBestResultAsyncTask userBestResultTask) {
        this.userBestResultTask = userBestResultTask;
    }

    public UserAvgResultAsyncTask getUserAvgResultAsyncTask() {
        return userAvgResultAsyncTask;
    }

    public void setUserAvgResultAsyncTask(UserAvgResultAsyncTask userAvgResultAsyncTask) {
        this.userAvgResultAsyncTask = userAvgResultAsyncTask;
    }

    public void addTaskListeners(TaskExecutionListener taskListener) {
        bestResultsTask.addTaskListener(taskListener);
        avgResultsTask.addTaskListener(taskListener);

        userBestResultTask.addTaskListener(taskListener);
        userAvgResultAsyncTask.addTaskListener(taskListener);
    }

    public void removeTaskListeners() {
        bestResultsTask.removeTaskListener();
        avgResultsTask.removeTaskListener();

        userBestResultTask.removeTaskListener();
        userAvgResultAsyncTask.removeTaskListener();
    }

}
