package com.artioml.practice.data;

import com.artioml.practice.Result;
import com.artioml.practice.data.CommunityProvider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.artioml.practice.data.PunchType.GLOVES_OFF;
import static com.artioml.practice.data.PunchType.GLOVES_ON;
import static com.artioml.practice.data.PunchType.LEFT_HAND;
import static com.artioml.practice.data.PunchType.RIGHT_HAND;
import static com.artioml.practice.data.PunchType.WITHOUT_STEP;
import static com.artioml.practice.data.PunchType.WITH_STEP;

/**
 * Created by Polina P on 06.02.2017.
 */
public class CommunityListProvider implements CommunityProvider {

    private ArrayList<Result> communityResults;
    private String currentLogin;

    public CommunityListProvider() {
        this.communityResults = new ArrayList<>();
        this.currentLogin = "Hello";
    }

    @Override
    public void addDataSet() {
        SimpleDateFormat sdf = new SimpleDateFormat("yy.MM.dd HH.mm.ss", Locale.ROOT);
        Calendar calendar = Calendar.getInstance();

        communityResults = new ArrayList<>();
        communityResults.add(new Result("ПАШКА", 0, RIGHT_HAND.getValue(), GLOVES_OFF.getValue(),
                "80", WITH_STEP.getValue(), 45, 20, 30, sdf.format(calendar.getTime())));
        communityResults.add(new Result("кач001", 1, LEFT_HAND.getValue(), GLOVES_ON.getValue(),
                "100", WITHOUT_STEP.getValue(), 95, 10, 70, sdf.format(calendar.getTime())));
        communityResults.add(new Result("Burda", 2, RIGHT_HAND.getValue(), GLOVES_ON.getValue(),
                "80", WITH_STEP.getValue(), 55, 15, 60, sdf.format(calendar.getTime())));
        communityResults.add(new Result("Санек95", 3, LEFT_HAND.getValue(), GLOVES_OFF.getValue(),
                "", WITH_STEP.getValue(), 75, 18, 30, sdf.format(calendar.getTime())));
    }

    @Override
    public void clearCommunity() {
        communityResults.clear();
    }

    @Override
    public boolean changeLogin(String oldLogin, String newLogin) {
        return false;
    }

    @Override
    public boolean loginUser(String login) {
        return true;
    }

    @Override
    public String getCurrentLogin() {
        return currentLogin;
    }

    @Override
    public void setCurrentLogin(String newLogin) {
        this.currentLogin = newLogin;
    }

    @Override
    public void logout() {

    }

    public ArrayList<Result> getCommunityResults() {
        return communityResults;
    }

    public void setCommunityResults(ArrayList<Result> communityResults) {
        this.communityResults = communityResults;
    }
}
