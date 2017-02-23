package com.artioml.practice.inject;

import android.content.Context;

import com.artioml.practice.data.CommunityDatabaseProvider;
import com.artioml.practice.data.CommunityListProvider;
import com.artioml.practice.data.CommunityProvider;
import com.artioml.practice.data.HistoryDatabaseProvider;
import com.artioml.practice.data.HistoryListProvider;
import com.artioml.practice.data.HistoryProvider;
import com.artioml.practice.utils.PunchSpeedApplication;

/**
 * Created by Polina P on 06.02.2017.
 */

public final class ServiceLocator {

    private static Context context;

    public static HistoryProvider getHistoryProvider() {
        if(context == null)
            return new HistoryListProvider();
        return new HistoryDatabaseProvider(PunchSpeedApplication.getContext());
    }

    public static CommunityProvider getCommunityProvider() {
        if(context == null)
            return new CommunityListProvider();
        return new CommunityDatabaseProvider(PunchSpeedApplication.getContext());
    }

    public static void setContext(Context context) {
        ServiceLocator.context = context;
    }

    public Context getContext() {
        return context;
    }
}
