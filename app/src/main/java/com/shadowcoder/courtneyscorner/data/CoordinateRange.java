package com.shadowcoder.courtneyscorner.data;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CoordinateRange {

    @SerializedName("start")
    public final Coordinate start;

    @SerializedName("end")
    public final Coordinate end;

    public CoordinateRange(@NonNull Coordinate start, @NonNull Coordinate end) {
        this.start = start;
        this.end = end;
    }

    public List<Coordinate> inflate(@NonNull Direction direction) {
        return this.start.inflateTo(this.end, direction);
    }
}
