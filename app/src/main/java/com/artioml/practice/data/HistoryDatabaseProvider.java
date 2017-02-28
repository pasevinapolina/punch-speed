package com.artioml.practice.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.artioml.practice.models.Result;
import com.artioml.practice.models.Settings;
import com.artioml.practice.utils.PunchType;
import com.artioml.practice.utils.SortOrder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Polina P on 06.02.2017.
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

    private PracticeDatabaseHelper dbHelper;
    private StringBuffer condition;
    private ArrayList<String> values;

    private String hand;
    private String gloves;
    private String position;
    private int punchType;
    private String sortOrder;

    public HistoryDatabaseProvider(Context context) {
        dbHelper = PracticeDatabaseHelper.getInstance(context);
        historyList = new ArrayList<>();
        values = new ArrayList<>();
        condition = new StringBuffer("");
        sortOrder = DatabaseDescription.History.COLUMN_SPEED + SortOrder.DESC.getValue();
    }

    @Override
    public void clearHistory() {
        historyList.clear();
    }

    @Override
    public long addResult(Result result) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseDescription.History.COLUMN_PUNCH_TYPE, result.getPunchType());
        cv.put(DatabaseDescription.History.COLUMN_HAND, result.getHand());
        cv.put(DatabaseDescription.History.COLUMN_GLOVES, result.getGloves());
        cv.put(DatabaseDescription.History.COLUMN_GLOVES_WEIGHT, result.getGlovesWeight());
        cv.put(DatabaseDescription.History.COLUMN_POSITION, result.getPosition());
        cv.put(DatabaseDescription.History.COLUMN_SPEED, result.getSpeed());
        cv.put(DatabaseDescription.History.COLUMN_REACTION, result.getReaction());
        cv.put(DatabaseDescription.History.COLUMN_ACCELERATION, result.getAcceleration());
        cv.put(DatabaseDescription.History.COLUMN_DATE, result.getDate());
        long id = db.insert(DatabaseDescription.History.TABLE_NAME, null, cv);
        return id;
    }

    @Override
    public ArrayList<Result> getHistoryList() {
        values = new ArrayList<>();
        condition = new StringBuffer("");

        parseParameters();
        String[] vals = prepareSelectionArgs();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                DatabaseDescription.History.TABLE_NAME,     // таблица
                DB_PROJECTION,             // столбцы
                condition.toString(),      // столбцы для условия WHERE
                vals,                   // значения для условия WHERE
                null,                   // Don't group the rows
                null,                   // Don't filter by row groups
                sortOrder);             // порядок сортировки

        while (cursor.moveToNext()) {
            historyList.add(extractResult(cursor));
        }
        cursor.close();
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

    private Result extractResult(Cursor cursor) {
        return new Result(
                cursor.getInt(cursor.getColumnIndex(DatabaseDescription.History.COLUMN_PUNCH_TYPE)),
                cursor.getString(cursor.getColumnIndex(DatabaseDescription.History.COLUMN_HAND)),
                cursor.getString(cursor.getColumnIndex(DatabaseDescription.History.COLUMN_GLOVES)),
                cursor.getString(cursor.getColumnIndex(DatabaseDescription.History.COLUMN_GLOVES_WEIGHT)),
                cursor.getString(cursor.getColumnIndex(DatabaseDescription.History.COLUMN_POSITION)),
                cursor.getFloat(cursor.getColumnIndex(DatabaseDescription.History.COLUMN_SPEED)),
                cursor.getFloat(cursor.getColumnIndex(DatabaseDescription.History.COLUMN_REACTION)),
                cursor.getFloat(cursor.getColumnIndex(DatabaseDescription.History.COLUMN_ACCELERATION)),
                cursor.getString(cursor.getColumnIndex(DatabaseDescription.History.COLUMN_DATE)));
    }

    @Override
    public void setPunchParameters(Settings settings) {
        this.punchType = settings.getPunchType();
        this.hand = settings.getHand();
        this.gloves = settings.getGloves();
        this.position = settings.getPosition();
        this.sortOrder = settings.getSortOrder();
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder, SortOrder type) {
        this.sortOrder = sortOrder + type.getValue();
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
