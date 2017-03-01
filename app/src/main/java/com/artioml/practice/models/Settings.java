package com.artioml.practice.models;

import com.artioml.practice.utils.SortOrder;

/**
 * Created by Polina P on 20.02.2017.
 */

public class Settings {

    private int punchType;
    private String hand;
    private String gloves;
    private String glovesWeight;
    private String position;
    private String sortColumn;
    private SortOrder orderType;

    public Settings(int punchType, String hand, String gloves, String position) {
        this.punchType = punchType;
        this.hand = hand;
        this.gloves = gloves;
        this.position = position;
    }

    public Settings(int punchType, String hand, String gloves, String glovesWeight, String position) {
        this(punchType, hand, gloves, position);
        this.glovesWeight = glovesWeight;
    }

    public Settings(int punchType, String hand, String gloves,
                    String position, String sortColumn, SortOrder orderType) {
        this(punchType, hand, gloves, position);
        this.sortColumn = sortColumn;
        this.orderType = orderType;
    }

    public String getGlovesWeight() {
        return glovesWeight;
    }

    public void setGlovesWeight(String glovesWeight) {
        this.glovesWeight = glovesWeight;
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

    public int getPunchType() {
        return punchType;
    }

    public void setPunchType(int punchType) {
        this.punchType = punchType;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getSortColumn() {
        return sortColumn;
    }

    public void setSortColumn(String sortColumn) {
        this.sortColumn = sortColumn;
    }

    public void setSortOrder(String column, SortOrder type) {
        this.sortColumn = column;
        this.orderType = type;
    }

    public SortOrder getOrderType() {
        return orderType;
    }

    public void setOrderType(SortOrder orderType) {
        this.orderType = orderType;
    }
}
