package com.shadowcoder.courtneyscorner.data;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CoordinateRange {

    @SerializedName("start")
    public final Coordinate start;

    @SerializedName("end")
    public final Coordinate end;

    @SerializedName("direction")
    public final Direction direction;

    public CoordinateRange(@NonNull Coordinate start, @NonNull Coordinate end, @NonNull Direction direction) {
        this.start = start;
        this.end = end;
        this.direction = direction;
    }

    public List<Coordinate> inflate() {
        return this.start.inflateTo(this.end, this.direction);
    }
}
