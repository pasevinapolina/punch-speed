package com.artioml.practice.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

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
    public void addDataSet() {

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
        return true;
    }

    @Override
    public String getCurrentLogin() {
        return null;
    }

    @Override
    public void setCurrentLogin(String newLogin) {

    }

    @Override
    public void logout() {

    }

    @Override
    public Result getBestUserResult() {
        return null;
    }

    @Override
    public Result getAverageUserResult() {
        return null;
    }

    @Override
    public Result getAverageResults() {
        return null;
    }
}
