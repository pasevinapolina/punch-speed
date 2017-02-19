package com.artioml.practice.utils;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.artioml.practice.data.HistoryDatabaseProvider;
import com.artioml.practice.data.HistoryProvider;
import com.artioml.practice.inject.ServiceLocator;
import com.artioml.practice.models.Result;
import com.artioml.practice.preferences.SettingsPreferenceManager;

import java.util.List;

/**
 * Created by Polina P on 17.02.2017.
 */

public class HistoryListLoader extends AsyncTaskLoader<List<Result>>{

    private final HistoryProvider historyProvider;
    private static final String HISTORY_SETTINGS = "historySettings";
    private final static String TAG = HistoryListLoader.class.getSimpleName();

    private List<Result> historyList;

    public HistoryListLoader(Context context, List<Result> historyList) {
        super(context);
        this.historyProvider = ServiceLocator.getHistoryProvider(context);
        SettingsPreferenceManager preferenceManager = new SettingsPreferenceManager(context, HISTORY_SETTINGS);
        historyProvider.setSortOrder(preferenceManager.getSortOrderPreference());

        historyProvider.setPunchParameters(preferenceManager.getPunchTypePreference(),
                            preferenceManager.getHandPreference(),
                            preferenceManager.getGlovesPreference(),
                            preferenceManager.getPositionPreference());

        this.historyList = historyList;
    }

    @Override
    public List<Result> loadInBackground() {
        try {
            synchronized (this) {
                historyProvider.clearHistory();
                historyProvider.addDataSet();

                historyList.clear();
                historyList.addAll(historyProvider.getHistoryList());

                Log.i(TAG, "loadInBackground");
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return historyList;
    }
}
