package com.artioml.practice.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.artioml.practice.utils.PunchType;

import java.io.Serializable;

/**
 * Created by Artiom L on 29.01.2017.
 */

public class Result implements Serializable, Parcelable {

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

    public Result() {
        this.punchType = 0;
        this.hand = PunchType.RIGHT_HAND.getValue();
        this.gloves = PunchType.GLOVES_ON.getValue();
        this.glovesWeight = "80";
        this.position = PunchType.WITHOUT_STEP.getValue();
        this.speed = 80;
        this.reaction = 20;
        this.acceleration = 100;
        this.date = "17.02.20 17.27.30";
    }

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

    @Override
    public String toString() {
        return "Result{" +
                "punchType=" + punchType +
                ", hand='" + hand + '\'' +
                ", gloves='" + gloves + '\'' +
                ", glovesWeight='" + glovesWeight + '\'' +
                ", position='" + position + '\'' +
                ", speed=" + speed +
                ", reaction=" + reaction +
                ", acceleration=" + acceleration +
                ", date='" + date + '\'' +
                ", user='" + user + '\'' +
                '}';
    }

    protected Result(Parcel in) {
        punchType = in.readInt();
        hand = in.readString();
        gloves = in.readString();
        glovesWeight = in.readString();
        position = in.readString();
        speed = in.readFloat();
        reaction = in.readFloat();
        acceleration = in.readFloat();
        date = in.readString();
        user = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(punchType);
        dest.writeString(hand);
        dest.writeString(gloves);
        dest.writeString(glovesWeight);
        dest.writeString(position);
        dest.writeFloat(speed);
        dest.writeFloat(reaction);
        dest.writeFloat(acceleration);
        dest.writeString(date);
        dest.writeString(user);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Result> CREATOR = new Parcelable.Creator<Result>() {
        @Override
        public Result createFromParcel(Parcel in) {
            return new Result(in);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }
    };
}
