package com.shadowcoder.courtneyscorner.data;

import android.support.annotation.NonNull;
import android.view.MotionEvent;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Coordinate {

    @SerializedName("x")
    public final int x;

    @SerializedName("y")
    public final int y;

    @SuppressWarnings({"unused", "WeakerAccess"})
    public Coordinate() {
        this.x = 0;
        this.y = 0;
    }

    @SuppressWarnings({"unused", "WeakerAccess"})
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @SuppressWarnings({"unused", "WeakerAccess"})
    public Coordinate(@NonNull Coordinate coordinate) {
        this.x = coordinate.x;
        this.y = coordinate.y;
    }

    @SuppressWarnings({"unused", "WeakerAccess"})
    public Coordinate(@NonNull MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Coordinate || obj instanceof MutableCoordinate)) {
            return false;
        }

        int x, y;

        if (obj instanceof Coordinate) {
            Coordinate o = (Coordinate) obj;
            x = o.x;
            y = o.y;
        }
        else {
            MutableCoordinate o = (MutableCoordinate) obj;
            x = o.x;
            y = o.y;
        }

        return (this.x == x && this.y == y);
    }

    @Override
    public int hashCode() {
        int result = 17;

        result = 31 * result + this.x;
        result = 31 * result + this.y;

        return result;
    }

    @SuppressWarnings({"unused", "WeakerAccess"})
    public boolean equalsCoordinate(Object object) {
        return this.equals(object);
    }

    @SuppressWarnings("WeakerAccess")
    public List<Coordinate> inflateTo(@NonNull Coordinate other, @NonNull Direction direction) {
        List<Coordinate> items = new ArrayList<>();

        if (direction == Direction.HORIZONTAL) {
            items.add(new Coordinate(this.x, this.y));

            for (int i = this.x + 1; i <= other.x; i++) {
                items.add(new Coordinate(i, this.y));
            }
        }

        if (direction == Direction.VERTICAL) {
            items.add(new Coordinate(this.x, this.y));

            for (int i = this.y + 1; i <= other.y; i++) {
                items.add(new Coordinate(this.x, i));
            }
        }

        return items;
    }
}
