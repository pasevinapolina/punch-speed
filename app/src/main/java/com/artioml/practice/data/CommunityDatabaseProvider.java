package com.artioml.practice.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.artioml.practice.models.Result;
import com.artioml.practice.models.Settings;

import java.util.ArrayList;

/**
 * Created by Polina P on 06.02.2017.
 */
public class CommunityDatabaseProvider implements CommunityProvider {

    private static final String[] DB_PROJECTION = {
            DatabaseDescription.Community._ID,
            DatabaseDescription.Community.COLUMN_LOGIN,
            DatabaseDescription.Community.COLUMN_PASSWORD
    };

    private SQLiteDatabase db;

    public CommunityDatabaseProvider(Context context) {
        db = (PracticeDatabaseHelper.getInstance(context)).getReadableDatabase();
    }

    @Override
    public ArrayList<Result> getBestResults() {
        ArrayList<Result> results = new ArrayList<>();

        return results;

    }

    @Override
    public void clearCommunity() {

    }

    public boolean changeLogin(String oldLogin, String newLogin) {

        ContentValues values = new ContentValues();
        values.put(DatabaseDescription.Community.COLUMN_LOGIN, newLogin);

        String selection = DatabaseDescription.Community.COLUMN_LOGIN + " LIKE ?";
        String[] selectionArgs = { oldLogin };

        int count = db.update(
                DatabaseDescription.Community.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        return count > 0;
    }

    @Override
    public boolean loginUser(String login) {
        return login.equals("hello");
    }

    @Override
    public String getCurrentLogin() {
        return "";
    }

    @Override
    public boolean setCurrentLogin(String newLogin) {
        return true;
    }

    @Override
    public void logout() {

    }

    @Override
    public Result getBestUserResult() {
        return new Result();
    }

    @Override
    public Result getAverageUserResult() {
        return new Result();
    }

    @Override
    public Result getAverageCommunityResult() {
        return new Result();
    }

    @Override
    public ArrayList<Result> getAverageResults(Settings settings) {
        return new ArrayList<>();
    }

    @Override
    public void setPunchParameters(Settings settings) {

    }
}
