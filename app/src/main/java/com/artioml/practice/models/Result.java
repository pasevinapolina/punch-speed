package com.artioml.practice.models;

/**
 * Created by Artiom L on 29.01.2017.
 */

public class Result {

    private int punchType;
    private String hand;
    private String gloves;
    private String glovesWeight;
    private String position;
    private float speed;
    private float reaction;
    private float acceleration;
    private String date;
    private String user;

    public Result(int punchType, String hand, String gloves, String glovesWeight, String position,
                  float speed, float reaction, float acceleration, String date) {
        this.punchType = punchType;
        this.hand = hand;
        this.gloves = gloves;
        this.glovesWeight = glovesWeight;
        this.position = position;
        this.speed = speed;
        this.reaction = reaction;
        this.acceleration = acceleration;
        this.date = date;
    }

    public Result(String user, int punchType, String hand, String gloves, String glovesWeight, String position,
                  float speed, float reaction, float acceleration, String date) {
        this.punchType = punchType;
        this.hand = hand;
        this.gloves = gloves;
        this.glovesWeight = glovesWeight;
        this.position = position;
        this.speed = speed;
        this.reaction = reaction;
        this.acceleration = acceleration;
        this.date = date;
        this.user = user;
    }

    public int getPunchType() {
        return punchType;
    }

    public void setPunchType(int punchType) {
        this.punchType = punchType;
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

    public String getGlovesWeight() {
        return glovesWeight;
    }

    public void setGlovesWeight(String glovesWeight) {
        this.glovesWeight = glovesWeight;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getReaction() {
        return reaction;
    }

    public void setReaction(float reaction) {
        this.reaction = reaction;
    }

    public float getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(float acceleration) {
        this.acceleration = acceleration;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
