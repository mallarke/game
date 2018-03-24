package com.shadowcoder.courtneyscorner.data;

import com.google.gson.annotations.SerializedName;

public enum Direction {
    @SerializedName("horizontal")
    HORIZONTAL,

    @SerializedName("vertical")
    VERTICAL;

    public Direction flip() {
        if (this == HORIZONTAL) {
            return VERTICAL;
        }

        return HORIZONTAL;
    }
}
