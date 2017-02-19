package com.artioml.practice.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.artioml.practice.models.Result;
import com.artioml.practice.utils.PunchType;

import java.util.ArrayList;

/**
 * Created by user on 06.02.2017.
 */

public class HistoryDatabaseProvider implements HistoryProvider {

    private ArrayList<Result> historyList;

    private static final String[] DB_PROJECTION = {
            DatabaseDescription.History._ID,
            DatabaseDescription.History.COLUMN_PUNCH_TYPE,
            DatabaseDescription.History.COLUMN_HAND,
            DatabaseDescription.History.COLUMN_GLOVES,
            DatabaseDescription.History.COLUMN_GLOVES_WEIGHT,
            DatabaseDescription.History.COLUMN_POSITION,
            DatabaseDescription.History.COLUMN_SPEED,
            DatabaseDescription.History.COLUMN_REACTION,
            DatabaseDescription.History.COLUMN_ACCELERATION,
            DatabaseDescription.History.COLUMN_DATE
    };

    private SQLiteDatabase db;
    private StringBuffer condition;
    private ArrayList<String> values;

    private String hand;
    private String gloves;
    private String position;
    private int punchType;
    private String sortOrder;

    public HistoryDatabaseProvider(Context context) {
        db = (PracticeDatabaseHelper.getInstance(context)).getReadableDatabase();
        historyList = new ArrayList<>();
        values = new ArrayList<>();
        condition = new StringBuffer("");
    }

    @Override
    public void addDataSet() {

        values = new ArrayList<>();
        condition = new StringBuffer("");

        parseParameters();
        String[] vals = prepareSelectionArgs();

        Cursor cursor = db.query(
                DatabaseDescription.History.TABLE_NAME,     // таблица
                DB_PROJECTION,             // столбцы
                condition.toString(),      // столбцы для условия WHERE
                vals,                   // значения для условия WHERE
                null,                   // Don't group the rows
                null,                   // Don't filter by row groups
                sortOrder);             // порядок сортировки

        while (cursor.moveToNext()) {
            historyList.add(new Result(
                    cursor.getInt(cursor.getColumnIndex(DatabaseDescription.History.COLUMN_PUNCH_TYPE)),
                    cursor.getString(cursor.getColumnIndex(DatabaseDescription.History.COLUMN_HAND)),
                    cursor.getString(cursor.getColumnIndex(DatabaseDescription.History.COLUMN_GLOVES)),
                    cursor.getString(cursor.getColumnIndex(DatabaseDescription.History.COLUMN_GLOVES_WEIGHT)),
                    cursor.getString(cursor.getColumnIndex(DatabaseDescription.History.COLUMN_POSITION)),
                    cursor.getFloat(cursor.getColumnIndex(DatabaseDescription.History.COLUMN_SPEED)),
                    cursor.getFloat(cursor.getColumnIndex(DatabaseDescription.History.COLUMN_REACTION)),
                    cursor.getFloat(cursor.getColumnIndex(DatabaseDescription.History.COLUMN_ACCELERATION)),
                    cursor.getString(cursor.getColumnIndex(DatabaseDescription.History.COLUMN_DATE))
            ));
        }

        cursor.close();
    }

    @Override
    public void clearHistory() {
        historyList.clear();
    }

    @Override
    public ArrayList<Result> getHistoryList() {
        return historyList;
    }

    public void setHistoryList(ArrayList<Result> historyList) {
        this.historyList = historyList;
    }

    private String[] prepareSelectionArgs() {
        String cond = "";
        String[] vals = null;
        if (condition.toString().compareTo("") != 0) {
            cond = condition.toString().substring(4);
            vals = new String[values.size()];
            for (int i = 0; i < values.size(); i++)
                vals[i] = values.get(i);
        }
        condition = new StringBuffer(cond);
        return vals;
    }

    private void parseParameters() {

        if (punchType > 0) {
            condition.append("AND " + DatabaseDescription.History.COLUMN_PUNCH_TYPE + " = ? ");
            values.add((punchType - 1) + "");
        }
        if (PunchType.getTypeByValue(hand) != PunchType.DOESNT_MATTER) {
            condition.append("AND " + DatabaseDescription.History.COLUMN_HAND + " = ? ");
            values.add(hand);
        }
        if (PunchType.getTypeByValue(gloves) != PunchType.DOESNT_MATTER) {
            condition.append("AND " + DatabaseDescription.History.COLUMN_GLOVES + " = ? ");
            values.add(gloves);
        }
        if (PunchType.getTypeByValue(position) != PunchType.DOESNT_MATTER) {
            condition.append("AND position = ? ");
            values.add(position);
        }
    }

    public void setPunchParameters(int punchType, String hand,
                                   String gloves, String position) {
        this.punchType = punchType;
        this.hand = hand;
        this.gloves = gloves;
        this.position = position;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getHand() {
        return hand;
    }

    public void setHand(String hand) {
        this.hand = hand;
    }

    public String getGloves() {
        return gloves;
    }

    public void setGloves(String gloves) {
        this.gloves = gloves;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getPunchType() {
        return punchType;
    }

    public void setPunchType(int punchType) {
        this.punchType = punchType;
    }
}
