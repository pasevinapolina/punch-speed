package com.artioml.practice.data;

import com.artioml.practice.models.Result;
import com.artioml.practice.models.Settings;
import com.artioml.practice.utils.SortOrder;

import java.util.ArrayList;

/**
 * Created by Polina P on 06.02.2017.
 */

public interface HistoryProvider {

    long addResult(Result result);
    ArrayList<Result> getHistoryList();
    void clearHistory();

    void setSortOrder(String sortOrder, SortOrder type);
    void setPunchParameters(Settings settings);
}
