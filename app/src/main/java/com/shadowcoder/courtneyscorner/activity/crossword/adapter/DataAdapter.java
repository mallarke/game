package com.shadowcoder.courtneyscorner.activity.crossword.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.shadowcoder.courtneyscorner.activity.crossword.mapper.Focus;
import com.shadowcoder.courtneyscorner.data.Coordinate;
import com.shadowcoder.courtneyscorner.data.CrosswordData;
import com.shadowcoder.courtneyscorner.data.Direction;
import com.shadowcoder.courtneyscorner.data.MutableCoordinate;
import com.shadowcoder.courtneyscorner.data.WordData;

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

    public DataAdapter(Focus focus) {
        this.focus = focus;
    }

    /*
     * GETTER/SETTER METHODS
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

    @Nullable
    @SuppressWarnings("WeakerAccess")
    public WordData getCurrentWord() {
        if (this.crosswordData == null) {
            return null;
        }

        CrosswordData.CacheResult result = this.crosswordData.getCache(this.direction).get(this.wordIndex);
        return result.data;
    }

    @NonNull
    @SuppressWarnings("unused")
    Direction getDirection() {
        return this.direction;
    }

    /*
     * PUBLIC METHODS
     */

    // header control methods

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

    public void flipDirection() {
        this.direction = this.direction.flip();

        if (this.crosswordData == null) {
            this.reset();
            return;
        }

        CrosswordData.CacheResult result = this.crosswordData.getCache(this.direction).lookup(this.coordinate.copy());
        this.set(result);
    }

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

    private boolean set(@NonNull CrosswordData.CacheResult result) {
        if (result.data == null) {
            return false;
        }

        this.set(result.data);

        return true;
    }

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
