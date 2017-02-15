package com.artioml.practice.data;

/**
 * Created by Polina P on 06.02.2017.
 */

public interface CommunityProvider {
    void addDataSet();
    void clearCommunity();
    boolean changeLogin(String oldLogin, String newLogin);
    boolean loginUser(String login);
    String getCurrentLogin();
    void setCurrentLogin(String newLogin);
    void logout();
    Result getBestUserResult();
    Result getAverageUserResult();
    Result getAverageResults();
}
