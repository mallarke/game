package com.shadowcoder.courtneyscorner.activity.crossword.mapper;

import android.support.annotation.NonNull;
import android.util.Log;

import com.shadowcoder.courtneyscorner.data.Coordinate;
import com.shadowcoder.courtneyscorner.data.ItemData;
import com.shadowcoder.courtneyscorner.data.MutableCoordinate;
import com.shadowcoder.courtneyscorner.data.ViewLookup;
import com.shadowcoder.courtneyscorner.data.WordData;

import java.util.ArrayList;
import java.util.List;

public class Focus {

    private static final String TAG = Focus.class.getSimpleName();

    private final ViewLookup viewLookup;

    private List<Coordinate> coordinates = new ArrayList<>();
    private MutableCoordinate focus = new MutableCoordinate();

    private boolean locked;

    /*
     * CONSTRUCTOR METHODS
     */

    public Focus(ViewLookup viewLookup) {
        this.viewLookup = viewLookup;
    }

    /*
     * PUBLIC METHODS
     */

    public void reset() {
        for (Coordinate coordinate : this.coordinates) {
            try {
                ItemData.ItemDataView view = this.viewLookup.get(coordinate);
                view.setFocus(false);
                view.setHighlight(false);
            }
            catch (IllegalArgumentException | IllegalStateException e) {
                Log.d(TAG, "unable to fetch view for focus reset event");
            }
        }

        this.coordinates.clear();
        this.focus.reset();
    }

    public void set(@NonNull WordData data, @NonNull Coordinate focus) {
        this.reset();

        this.coordinates = data.getPositions();
        this.focus.set(focus);

        if (this.locked) {
            return;
        }

        this.update();
    }

    private void update() {
        for (Coordinate coordinate : this.coordinates) {
            try {
                ItemData.ItemDataView view = this.viewLookup.get(coordinate);
                view.setFocus(this.focus.equalsCoordinate(coordinate));
                view.setHighlight(true);
            }
            catch (IllegalStateException e) {
                Log.d(TAG, "unable to fetch view for focus set event");
            }
        }
    }

    public void lock() {
        this.locked = true;
    }

    public void unlock() {
        this.locked = false;
        this.update();
    }
}
