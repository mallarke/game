package com.shadowcoder.courtneyscorner.activity.crossword.mapper;

import android.support.annotation.NonNull;
import android.util.Log;

import com.shadowcoder.courtneyscorner.data.Coordinate;
import com.shadowcoder.courtneyscorner.data.ItemData;
import com.shadowcoder.courtneyscorner.data.MutableCoordinate;
import com.shadowcoder.courtneyscorner.lookup.LookupType;
import com.shadowcoder.courtneyscorner.lookup.ViewLookup;
import com.shadowcoder.courtneyscorner.data.WordData;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller class to reflect the current input focus with the onscreen components
 */
public class Focus {

    private static final String TAG = Focus.class.getSimpleName();

    private List<Coordinate> coordinates = new ArrayList<>();
    private MutableCoordinate focus = new MutableCoordinate();

    private boolean locked;

    /*
     * PUBLIC METHODS
     */

    /**
     * Resets the focus.  This clears the cached coordinates associated with a {@link WordData} and
     * the coordinate of the caret on screen
     */
    public void reset() {
        for (Coordinate coordinate : this.coordinates) {
            try {
                ItemData.ItemDataView view = this.getViewLookup().get(coordinate);
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

    /**
     * Sets the highlighted state of the active {@link WordData} and the caret
     *
     * @param data the active {@link WordData}
     * @param focus the {@link Coordinate} of the caret
     */
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
                ItemData.ItemDataView view = this.getViewLookup().get(coordinate);
                view.setFocus(this.focus.equalsCoordinate(coordinate));
                view.setHighlight(true);
            }
            catch (IllegalStateException e) {
                Log.d(TAG, "unable to fetch view for focus set event");
            }
        }
    }

    /**
     * Locks the focus during screen invalidation cycles
     */
    public void lock() {
        this.locked = true;
    }

    /**
     * Unlocks the focus after the screen invalidation cycles and syncs any coordinates set while locked
     */
    public void unlock() {
        this.locked = false;
        this.update();
    }

    /*
     * PRIVATE METHODS
     */

    private ViewLookup getViewLookup() {
        return ViewLookup.get(LookupType.CROSSWORD);
    }
}
