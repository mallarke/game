package com.shadowcoder.courtneyscorner.activity.crossword.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.shadowcoder.courtneyscorner.activity.crossword.mapper.Focus;
import com.shadowcoder.courtneyscorner.data.Coordinate;
import com.shadowcoder.courtneyscorner.data.CrosswordData;
import com.shadowcoder.courtneyscorner.data.Direction;
import com.shadowcoder.courtneyscorner.data.MutableCoordinate;
import com.shadowcoder.courtneyscorner.data.WordData;

/**
 * An adapter to manage the user's state.  This is the bread and butter of the usability
 * of the crossword actions.  This links together the {@link Focus} actions to user's
 * actions
 */
public class DataAdapter {

    @NonNull
    private Direction direction = Direction.HORIZONTAL;

    private int wordIndex;
    private final MutableCoordinate coordinate = new MutableCoordinate();

    @Nullable
    private CrosswordData crosswordData;

    private final Focus focus;

    /*
     * CONSTRUCTOR METHODS
     */

    /**
     * Adapter used to link the user's action to the input {@link Focus}
     *
     * @param focus the {@link Focus} to use to highlight the input focus
     */
    public DataAdapter(Focus focus) {
        this.focus = focus;
    }

    /*
     * GETTER/SETTER METHODS
     */

    /**
     * Setter for the {@link CrosswordData} to manipulate.  The {@link Focus} is set
     *
     * @param data the {@link CrosswordData} to use
     */
    public void setCrosswordData(@Nullable CrosswordData data) {
        this.reset();
        this.crosswordData = data;

        WordData current = this.getCurrentWord();
        if (current == null) {
            return;
        }

        this.set(current);
    }

    /**
     * Getter for the current {@link WordData} in the input focus
     *
     * @return the current {@link WordData}
     */
    @Nullable
    @SuppressWarnings("WeakerAccess")
    public WordData getCurrentWord() {
        if (this.crosswordData == null) {
            return null;
        }

        CrosswordData.CacheResult result = this.crosswordData.getCache(this.direction).get(this.wordIndex);
        return result.data;
    }

    /**
     * Getter for the current {@link Direction} of the input focus
     *
     * @return the current {@link Direction}
     */
    @NonNull
    @SuppressWarnings("unused")
    Direction getDirection() {
        return this.direction;
    }

    /*
     * PUBLIC METHODS
     */

    // header control methods

    /**
     * Move the input focus to the previous {@link WordData}.  This is dependent on the current {@link Direction}
     *
     * @return The previous {@link WordData} or null if none are found.
     */
    @Nullable
    public WordData previous() {
        if (this.crosswordData == null) {
            return null;
        }

        CrosswordData.DataCache cache = this.crosswordData.getCache(this.direction);

        if (cache.size() == 0) {
            this.reset();
            return null;
        }

        int index = this.wordIndex - 1;
        if (index < 0) {
            index = cache.size() - 1;
        }

        CrosswordData.CacheResult result = cache.get(index);
        if (!this.set(result)) {
            this.reset();
        }

        return result.data;
    }

    /**
     * Flip the input focus from {@link Direction#HORIZONTAL} or {@link Direction#VERTICAL}
     */
    public void flipDirection() {
        this.direction = this.direction.flip();

        if (this.crosswordData == null) {
            this.reset();
            return;
        }

        CrosswordData.CacheResult result = this.crosswordData.getCache(this.direction).lookup(this.coordinate.copy());
        this.set(result);
    }

    /**
     * Move the input focus to the next {@link WordData}.  This is dependent on the current {@link Direction}
     *
     * @return The next {@link WordData} or null if none are found.
     */
    @Nullable
    public WordData next() {
        if (this.crosswordData == null) {
            return null;
        }

        CrosswordData.DataCache cache = this.crosswordData.getCache(this.direction);

        if (cache.size() == 0) {
            this.reset();
            return null;
        }

        int index = this.wordIndex + 1;
        if (index >= cache.size()) {
            index = 0;
        }

        CrosswordData.CacheResult result = cache.get(index);
        if (!this.set(result)) {
            this.reset();
        }

        return result.data;
    }

    /**
     * Move the input focus to a specified {@link Coordinate}
     *
     * @param coordinate the new {@link Coordinate} to use
     */
    public void move(@NonNull Coordinate coordinate) {
        if (this.crosswordData == null) {
            return;
        }

        CrosswordData.DataCache cache = this.crosswordData.getCache(this.direction);

        if (cache.size() == 0) {
            return;
        }

        CrosswordData.CacheResult result = cache.lookup(coordinate);
        int offset = (this.direction == Direction.HORIZONTAL ? coordinate.x : coordinate.y);
        this.set(result, offset);
    }

    // text entry methods

    /**
     * Adds a new letter to the current {@link WordData}.  If the add action is taken on the last
     * element, the next {@link WordData} is found and made the focus.
     * @param text the new {@link String} to add
     */
    public void add(@NonNull String text) {
        if (this.crosswordData == null) {
            return;
        }

        WordData data = this.getCurrentWord();
        if (data == null) {
            return;
        }

        if (data.addText(text)) {
            data = this.next();
        }

        this.set(data);
    }

    /**
     * Deletes the last item from a {@link WordData}.  If the delete action is taken on the first
     * element, the previous {@link WordData} is found and made the focus.
     */
    public void delete() {
        if (this.crosswordData == null) {
            return;
        }

        WordData data = this.getCurrentWord();
        if (data == null) {
            return;
        }

        if (data.deleteText()) {
            data = this.previous();
            if (data != null) {
                data.deleteLast();
            }
        }

        this.set(data);
    }

    /*
     * PRIVATE METHODS
     */

    /**
     * Set the {@link #coordinate} and {@link #wordIndex} variables while updating the
     * {@link Focus}.
     *
     * @param data the result of a {@link CrosswordData.DataCache} lookup
     */
    private void set(@Nullable WordData data) {
        if (data == null || this.crosswordData == null) {
            return;
        }

        CrosswordData.CacheResult result = this.crosswordData.getCache(this.direction).lookup(data.getStartPosition());
        if (result.data == null) {
            return;
        }

        Coordinate focus = this.findFocus(result.data);

        this.coordinate.set(focus);
        this.focus.set(result.data, focus);
        this.wordIndex = result.index;
    }

    /**
     * Set the {@link #coordinate} and {@link #wordIndex} variables while updating the
     * {@link Focus}.
     *
     * @param result a {@link CrosswordData.CacheResult} from a {@link CrosswordData.DataCache} lookup
     */
    private boolean set(@NonNull CrosswordData.CacheResult result) {
        if (result.data == null) {
            return false;
        }

        this.set(result.data);

        return true;
    }

    /**
     * Set the {@link #coordinate} and {@link #wordIndex} variables while updating the
     * {@link Focus}.
     *
     * This lets you also set an optional index offset if the current {@link WordData} is completed
     *
     * @param result a {@link CrosswordData.CacheResult} from a {@link CrosswordData.DataCache} lookup
     * @param offset an offset to use if the current {@link WordData} has been completed
     */
    private void set(@NonNull CrosswordData.CacheResult result, int offset) {
        if (result.data == null) {
            return;
        }

        this.set(result.data);
        if (result.data.completed()) {
            Coordinate startingPoint = result.data.getStartPosition();

            int x, y;

            if (this.direction == Direction.HORIZONTAL) {
                x = startingPoint.x + offset;
                y = startingPoint.y;

                result.data.setIndex(offset);
            }
            else {
                x = startingPoint.x;
                y = startingPoint.y + offset;
            }

            Coordinate coordinate = new Coordinate(x, y);

            this.coordinate.set(coordinate);
            this.focus.set(result.data, coordinate);
        }
    }

    /**
     * Finds the next available empty space in a {@link WordData}.  If none are found, the current
     * index is used.
     *
     * @param data the {@link WordData} used
     * @return the {@link Coordinate} of the new focus
     */
    @NonNull
    private Coordinate findFocus(@NonNull WordData data) {
        int x, y;
        int offset;

        try {
            offset = data.flushIndex();
        }
        catch (IllegalStateException e) {
            offset = 0;
        }

        if (this.direction == Direction.HORIZONTAL) {
            x = data.getStartPosition().x + offset;
            y = data.getStartPosition().y;
        }
        else {
            x = data.getStartPosition().x;
            y = data.getStartPosition().y + offset;
        }

        return new Coordinate(x, y);
    }

    // clean up methods

    private void reset() {
        this.wordIndex = 0;
        this.coordinate.reset();
        this.focus.reset();
    }
}
