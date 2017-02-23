package com.artioml.practice.data;

import com.artioml.practice.models.Result;
import com.artioml.practice.models.Settings;

import java.util.ArrayList;

/**
 * Created by Polina P on 06.02.2017.
 */

public interface CommunityProvider {

    ArrayList<Result> getBestResults();
    ArrayList<Result> getAverageResults(Settings settings);

    Result getBestUserResult();
    Result getAverageUserResult();
    Result getAverageCommunityResult();

    void clearCommunity();

    boolean loginUser(String login);
    boolean changeLogin(String oldLogin, String newLogin);
    void logout();

    String getCurrentLogin();
    boolean setCurrentLogin(String newLogin);

    void setPunchParameters(Settings settings);
}
