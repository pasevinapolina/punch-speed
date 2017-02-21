package com.artioml.practice.utils;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * Created by Polina P on 20.02.2017.
 */

public class PunchSpeedApplication extends Application {

    private static PunchSpeedApplication instance;

    public static PunchSpeedApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }

}
