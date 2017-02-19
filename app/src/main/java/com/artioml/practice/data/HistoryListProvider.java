package com.artioml.practice.data;

import com.artioml.practice.models.Result;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static com.artioml.practice.utils.PunchType.GLOVES_OFF;
import static com.artioml.practice.utils.PunchType.GLOVES_ON;
import static com.artioml.practice.utils.PunchType.LEFT_HAND;
import static com.artioml.practice.utils.PunchType.RIGHT_HAND;
import static com.artioml.practice.utils.PunchType.WITHOUT_STEP;
import static com.artioml.practice.utils.PunchType.WITH_STEP;

/**
 * Created by Polina P on 06.02.2017.
 */

public class HistoryListProvider implements HistoryProvider {

    private ArrayList<Result> historyList;
    private String sortOrder;

    public HistoryListProvider() {
        historyList = new ArrayList<>();
        sortOrder = DatabaseDescription.History.COLUMN_DATE + " DESC";
    }

    @Override
    public void addDataSet() {
        SimpleDateFormat sdf = new SimpleDateFormat("yy.MM.dd HH.mm.ss", Locale.ROOT);
        Calendar calendar = Calendar.getInstance();

        historyList = new ArrayList<>();
        historyList.add(new Result(0, RIGHT_HAND.getValue(), GLOVES_OFF.getValue(),
                "80", WITH_STEP.getValue(), 45, 20, 30, sdf.format(calendar.getTime())));
        historyList.add(new Result(1, LEFT_HAND.getValue(), GLOVES_ON.getValue(),
                "100", WITHOUT_STEP.getValue(), 45, 20, 30, sdf.format(calendar.getTime())));
    }

    @Override
    public void clearHistory() {
        historyList.clear();
    }

    @Override
    public ArrayList<Result> getHistoryList() {
        return historyList;
    }

    @Override
    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    @Override
    public void setPunchParameters(int punchType, String hand, String gloves, String position) {

    }

    public void setHistoryList(ArrayList<Result> historyList) {
        this.historyList = historyList;
    }
}
