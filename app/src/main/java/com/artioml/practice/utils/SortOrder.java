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

    public static SortOrder getTypeByValue(String value) {
        for (SortOrder type : SortOrder.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }
}
