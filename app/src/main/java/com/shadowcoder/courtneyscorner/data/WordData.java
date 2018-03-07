package com.shadowcoder.courtneyscorner.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.shadowcoder.courtneyscorner.lookup.LookupType;
import com.shadowcoder.courtneyscorner.lookup.ViewLookup;

import java.util.ArrayList;
import java.util.List;

public class WordData {

    private final @NonNull String word;
    private final @NonNull Coordinate startPosition;
    private final @NonNull Coordinate endPosition;
    private final @NonNull Direction direction;

    private final @NonNull ArrayList<ItemData> data;

    private int textIndex = 0;

    public static @NonNull WordData horizontalData(@NonNull String word, @NonNull Coordinate startPosition) {
        Coordinate endPosition = new Coordinate((startPosition.x + word.length() - 1), startPosition.y);
        return new WordData(word, startPosition, endPosition, Direction.HORIZONTAL);
    }

    public static @NonNull WordData verticalData(@NonNull String word, @NonNull Coordinate startPosition) {
        Coordinate endPosition = new Coordinate(startPosition.x, startPosition.y + (word.length() - 1));
        return new WordData(word, startPosition, endPosition, Direction.VERTICAL);
    }

    private WordData(@NonNull String word, @NonNull Coordinate startPosition, @NonNull Coordinate endPosition, @NonNull Direction direction) {
        this.word = word;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.direction = direction;

        boolean isHorizontal = direction == Direction.HORIZONTAL;

        int start = (isHorizontal ? startPosition.x : startPosition.y);
        int end =  (isHorizontal ? endPosition.x : endPosition.y) + 1;

        ArrayList<ItemData> array = new ArrayList<>();

        for (int i = start, index = 0; i < end; i++, index++) {
            int x = (isHorizontal ? i : startPosition.x);
            int y = (isHorizontal ? startPosition.y : i);

            String letter = Character.toString(word.charAt(index));
            Integer startingIndex = (i == start ? start : null);
            ItemData data = new ItemData(new Coordinate(x, y), startingIndex, letter);
            array.add(data);
        }

        this.data = array;
    }

    @NonNull
    public ArrayList<ItemData> getData() {
        return data;
    }

    @Nullable
    public ItemData getData(@NonNull Coordinate coordinate) {
        for (ItemData data : this.data) {
            if (data.getPosition().x == coordinate.x && data.getPosition().y == coordinate.y) {
                return data;
            }
        }

        return null;
    }

    @NonNull
    Direction getDirection() {
        return this.direction;
    }

    @NonNull
    @SuppressWarnings({"unused", "WeakerAccess"})
    public Coordinate getStartPosition() {
        return startPosition;
    }

    @NonNull
    public Coordinate getEndPosition() {
        return endPosition;
    }

    @NonNull
    public List<Coordinate> getPositions() {
        return this.startPosition.inflateTo(this.endPosition, this.direction);
    }

    public boolean completed() {
        for (Coordinate coordinate : this.getPositions()) {
            ItemData.ItemDataView view;

            try {
                view = this.getViewLookup().get(coordinate);
            }
            catch (IllegalStateException e) {
                return false;
            }

            if (view.getText() == null) {
                return false;
            }
        }

        return true;
    }

    public int flushIndex() {
        for (int i = 0; i < this.data.size(); i++) {
            ItemData data = this.data.get(i);
            ItemData.ItemDataView view = this.getViewLookup().get(data.getPosition());

            if (view.getText() == null) {
                this.textIndex = i;
                return i;
            }
        }

        return this.textIndex;
    }

    public void setIndex(int index) {
        this.textIndex = index;
    }

    public boolean addText(@NonNull String text) {
        ItemData.ItemDataView view = getView();
        view.setText(text);

        SearchData data = getNextView(this.textIndex);
        if (data == null) {
            this.textIndex = 0;
            return true;
        }

        this.textIndex = data.index;

        return false;
    }

    public boolean deleteText() {
        SearchData data = getPreviousView(this.textIndex);
        if (data == null) {
            this.textIndex = 0;
            return true;
        }

        this.textIndex = data.index;
        data.view.setText(null);

        return false;
    }

    public void deleteLast() {
        this.textIndex = this.data.size() - 1;
        ItemData.ItemDataView view = this.getView();
        view.setText(null);
    }

    @NonNull
    private ItemData.ItemDataView getView() {
        return this.getView(this.textIndex);
    }

    @NonNull
    private ItemData.ItemDataView getView(int index) {
        return this.getViewLookup().get(this.data.get(index).getPosition());
    }

    @Nullable
    private SearchData getNextView(int currentIndex) {
        if (this.completed()) {
            return null;
        }

        ItemData.ItemDataView view;

        try {
            view = this.getView(currentIndex);
        }
        catch (IllegalStateException e) {
            return null;
        }

        if (view.getText() != null) {
            currentIndex++;
            if (currentIndex == this.textIndex) {
                return null;
            }

            if (currentIndex >= this.data.size()) {
                currentIndex = 0;
            }

            try {
                view = this.getView(currentIndex);
            }
            catch (IllegalStateException e) {
                return null;
            }

            if (view.getText() != null) {
                return getNextView(currentIndex);
            }
        }

        return new SearchData(view, currentIndex);
    }

    @Nullable
    private SearchData getPreviousView(int currentIndex) {
        if (this.completed()) {
            return null;
        }

        ItemData.ItemDataView view;
        try {
            view = this.getView(currentIndex);
        }
        catch (IllegalStateException e) {
            return null;
        }

        if (view.getText() == null) {
            currentIndex--;
            if (currentIndex == this.textIndex) {
                return null;
            }

            if (currentIndex < 0) {
                currentIndex = this.data.size() - 1;
            }

            try {
                view = this.getView(currentIndex);
            }
            catch (IllegalStateException e) {
                return null;
            }

            if (view.getText() == null) {
                return getPreviousView(currentIndex);
            }
        }

        return new SearchData(view, currentIndex);
    }

    private ViewLookup getViewLookup() {
        return ViewLookup.get(LookupType.CROSSWORD);
    }

    private static class SearchData {
        private final ItemData.ItemDataView view;
        private final int index;

        SearchData(ItemData.ItemDataView view, int index) {
            this.view = view;
            this.index = index;
        }
    }
}
