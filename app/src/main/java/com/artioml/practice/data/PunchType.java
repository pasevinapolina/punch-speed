package com.artioml.practice.data;

/**
 * Created by Polina P on 06.02.2017.
 */

public enum PunchType {

    RIGHT_HAND("right"),
    LEFT_HAND("left"),

    GLOVES_ON("on"),
    GLOVES_OFF("off"),

    WITH_STEP("with"),
    WITHOUT_STEP("without"),

    DOESNT_MATTER("dm");

    String value;

    PunchType(String value) {
        this.value = value;
    }

    public static PunchType getTypeByValue(String value) {
        for(PunchType type : PunchType.values()) {
            if(type.getValue().equals(value)) {
                return type;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }
}
