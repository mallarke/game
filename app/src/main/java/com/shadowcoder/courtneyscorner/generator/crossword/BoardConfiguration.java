package com.shadowcoder.courtneyscorner.generator.crossword;

import com.google.gson.annotations.SerializedName;
import com.shadowcoder.courtneyscorner.data.Coordinate;
import com.shadowcoder.courtneyscorner.data.CoordinateRange;

import java.util.List;

public class BoardConfiguration {

    @SerializedName("name")
    public String name;

    @SerializedName("blanks")
    public List<Coordinate> blanks;

    @SerializedName("horizontal")
    public List<CoordinateRange> horizontalRanges;

    @SerializedName("vertical")
    public List<CoordinateRange> verticalRanges;
}
