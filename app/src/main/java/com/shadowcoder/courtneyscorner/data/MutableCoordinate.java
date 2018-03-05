package com.shadowcoder.courtneyscorner.data;

import android.support.annotation.NonNull;
import android.view.MotionEvent;

public class MutableCoordinate {

    public int x;
    public int y;

    @SuppressWarnings({"unused", "WeakerAccess"})
    public MutableCoordinate() {
        this.x = 0;
        this.y = 0;
    }

    @SuppressWarnings({"unused", "WeakerAccess"})
    public MutableCoordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @SuppressWarnings({"unused", "WeakerAccess"})
    public MutableCoordinate(@NonNull MutableCoordinate coordinate) {
        this.x = coordinate.x;
        this.y = coordinate.y;
    }

    @SuppressWarnings({"unused", "WeakerAccess"})
    public MutableCoordinate(@NonNull Coordinate coordinate) {
        this.x = coordinate.x;
        this.y = coordinate.y;
    }

    @SuppressWarnings({"unused", "WeakerAccess"})
    public MutableCoordinate(@NonNull MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        this.x = x;
        this.y = y;
    }

    @SuppressWarnings({"unused", "WeakerAccess"})
    public Coordinate copy() {
        return new Coordinate(this.x, this.y);
    }

    @SuppressWarnings({"unused", "WeakerAccess"})
    public MutableCoordinate mutableCopy() {
        return new MutableCoordinate(this);
    }

    @SuppressWarnings({"unused", "WeakerAccess"})
    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @SuppressWarnings({"unused", "WeakerAccess"})
    public void set(@NonNull Coordinate coordinate) {
        this.set(coordinate.x, coordinate.y);
    }

    @SuppressWarnings({"unused", "WeakerAccess"})
    public void set(@NonNull MutableCoordinate coordinate) {
        this.set(coordinate.x, coordinate.y);
    }

    @SuppressWarnings({"unused", "WeakerAccess"})
    public void reset() {
        this.x = 0;
        this.y = 0;
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

    @SuppressWarnings({"unused", "WeakerAccess"})
    public boolean equalsCoordinate(Object object) {
        return this.equals(object);
    }

    @Override
    public int hashCode() {
        int result = 17;

        result = 31 * result + this.x;
        result = 31 * result + this.y;

        return result;
    }
}
