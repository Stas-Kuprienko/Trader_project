package com.trade_project.enums;

public enum Sorting {

    VOLUME(Direction.DESC), ALPHABET(Direction.ASC), CHANGE_PERCENT(Direction.DESC);


    public final Direction defaultDirection;

    Sorting(Direction defaultDirection) {
        this.defaultDirection = defaultDirection;
    }


    public enum Direction {
        ASC, DESC
    }
}
