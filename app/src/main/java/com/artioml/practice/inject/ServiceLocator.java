package com.artioml.practice.inject;

import android.content.Context;

import com.artioml.practice.data.CommunityDatabaseProvider;
import com.artioml.practice.data.CommunityListProvider;
import com.artioml.practice.data.CommunityProvider;
import com.artioml.practice.data.HistoryDatabaseProvider;
import com.artioml.practice.data.HistoryListProvider;
import com.artioml.practice.data.HistoryProvider;

/**
 * Created by Polina P on 06.02.2017.
 */

public final class ServiceLocator {
    public static HistoryProvider getHistoryProvider(Context context) {
        if(context == null)
            return new HistoryListProvider();
        return new HistoryDatabaseProvider(context);
    }

    public static CommunityProvider getCommunityProvider(Context context) {
        if(context == null)
            return new CommunityListProvider();
        return new CommunityDatabaseProvider(context);
    }
}
