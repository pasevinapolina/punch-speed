package com.artioml.practice.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Polina P on 22.02.2017.
 */

public class LoginPrefernceManager {
    private static final String COMMUNITY_STORAGE = "communityStorage";
    private static final String IS_LOGGED_IN = "pref_isLoggedIn";
    private static final String LOGIN = "pref_login";

    private SharedPreferences sharedPreferences;

    public LoginPrefernceManager(final Context context) {
        this.sharedPreferences = context.getSharedPreferences(COMMUNITY_STORAGE, Context.MODE_PRIVATE);
    }

    public boolean getIsLoggedInPreference() {
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false);
    }

    public String getLoginPreference() {
        return sharedPreferences.getString(LOGIN, "");
    }

    public void setLoginPreferences(boolean isLoggedIn, String login) {
        sharedPreferences.edit()
                .putBoolean(IS_LOGGED_IN, isLoggedIn)
                .putString(LOGIN, login)
                .apply();
    }

}
