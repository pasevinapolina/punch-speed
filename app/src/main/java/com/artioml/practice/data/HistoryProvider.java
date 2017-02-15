package com.artioml.practice.data;

import java.util.ArrayList;

/**
 * Created by Polina P on 06.02.2017.
 */

public interface HistoryProvider {

    void addDataSet();
    void clearHistory();
    ArrayList<Result> getHistoryList();
    void setSortOrder(String sortOrder);
}
