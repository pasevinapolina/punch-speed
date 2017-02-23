package com.artioml.practice.data;

import com.artioml.practice.models.Result;
import com.artioml.practice.models.Settings;
import com.artioml.practice.utils.PunchType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
public class CommunityListProvider implements CommunityProvider {

    private ArrayList<Result> communityResults;
    private String currentLogin;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yy.MM.dd HH.mm.ss", Locale.ROOT);

    public CommunityListProvider() {
        this.communityResults = new ArrayList<>();
        this.currentLogin = "Hello";
    }

    @Override
    public ArrayList<Result> getBestResults() {
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

        return communityResults;
    }

    @Override
    public ArrayList<Result> getAverageResults(Settings settings) {
        ArrayList<Result> averageResults = new ArrayList<>();
        averageResults.add(new Result("Сильный", 2, PunchType.RIGHT_HAND.getValue(),
                PunchType.GLOVES_ON.getValue(), "80", PunchType.WITHOUT_STEP.getValue(),
                70, 20, 76, "19.02.15 23.01.01"));
        averageResults.add(new Result("Санек95", 2, PunchType.LEFT_HAND.getValue(),
                PunchType.GLOVES_ON.getValue(), "100", PunchType.WITHOUT_STEP.getValue(),
                79, 15, 88, sdf.format(new Date())));
        averageResults.add(new Result("Username", 2, PunchType.RIGHT_HAND.getValue(),
                PunchType.GLOVES_ON.getValue(), "100", PunchType.WITH_STEP.getValue(),
                64, 18, 102, sdf.format(new Date())));
        averageResults.add(new Result("кач001", 2, PunchType.RIGHT_HAND.getValue(),
                PunchType.GLOVES_OFF.getValue(), "100", PunchType.WITHOUT_STEP.getValue(),
                55, 9, 51, sdf.format(new Date())));
        averageResults.add(new Result("Брат", 2, PunchType.LEFT_HAND.getValue(),
                PunchType.GLOVES_ON.getValue(), "100", PunchType.WITH_STEP.getValue(),
                60, 20, 44, "17.02.18 17.27.30"));

        return averageResults;
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
        return login.equals("hello");
    }

    @Override
    public String getCurrentLogin() {
        return currentLogin;
    }

    @Override
    public boolean setCurrentLogin(String newLogin) {
        this.currentLogin = newLogin;
        return true;
    }

    @Override
    public void logout() {

    }

    @Override
    public Result getBestUserResult() {
        return new Result(currentLogin, 2, PunchType.RIGHT_HAND.getValue(),
                PunchType.GLOVES_ON.getValue(), "100", PunchType.WITHOUT_STEP.getValue(),
                100, 10, 70, sdf.format(new Date()));
    }

    @Override
    public Result getAverageUserResult() {
        return new Result(currentLogin, 2, PunchType.RIGHT_HAND.getValue(),
                PunchType.GLOVES_ON.getValue(), "100", PunchType.WITHOUT_STEP.getValue(),
                85, 15, 66, sdf.format(new Date()));
    }

    @Override
    public Result getAverageCommunityResult() {
        return new Result(currentLogin, 2, PunchType.RIGHT_HAND.getValue(),
                PunchType.GLOVES_ON.getValue(), "100", PunchType.WITHOUT_STEP.getValue(),
                40, 8, 50, sdf.format(new Date()));
    }

    @Override
    public void setPunchParameters(Settings settings) {

    }

    public ArrayList<Result> getCommunityResults() {
        return communityResults;
    }

    public void setCommunityResults(ArrayList<Result> communityResults) {
        this.communityResults = communityResults;
    }


}
