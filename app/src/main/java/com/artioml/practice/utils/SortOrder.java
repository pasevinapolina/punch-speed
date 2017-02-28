package com.artioml.practice.utils;

/**
 * Created by Polina P on 28.02.2017.
 */

public enum SortOrder {
    ASC(" ASC"),
    DESC(" DESC");

    String value;

    SortOrder(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
