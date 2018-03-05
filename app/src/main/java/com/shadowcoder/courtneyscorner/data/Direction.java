package com.shadowcoder.courtneyscorner.data;

public enum Direction {
    HORIZONTAL, VERTICAL;

    public Direction flip() {
        if (this == HORIZONTAL) {
            return VERTICAL;
        }

        return HORIZONTAL;
    }
}
