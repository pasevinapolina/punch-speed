package com.artioml.practice.interfaces;

/**
 * Created by Polina P on 20.02.2017.
 */

public interface TaskExecutionListener {
    void onStarted();
    void onCompleted(Object... result);
    void onError();
}
