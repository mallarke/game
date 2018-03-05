package com.shadowcoder.courtneyscorner.data;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public abstract class ViewLookup {

    private List<ItemData.ItemDataView> lookup = new ArrayList<>();

    protected ViewLookup() {

    }

    protected abstract int rowLength();

    private int flatten(@NonNull Coordinate coordinate) {
        return (coordinate.y * this.rowLength()) + coordinate.x;
    }

    @SuppressWarnings("WeakerAccess")
    public void register(int index, @NonNull ItemData.ItemDataView view) {
        if (index >= this.lookup.size()) {
            for (int i = this.lookup.size(); i < index; i++) {
                this.lookup.add(null);
            }

            this.lookup.add(view);
            return;
        }

        this.lookup.set(index, view);
    }

    public void clear() {
        this.lookup.clear();
    }

    @NonNull
    @SuppressWarnings("WeakerAccess")
    public ItemData.ItemDataView get(@NonNull Coordinate coordinate) {
        int index = this.flatten(coordinate);
        return this.get(index);
    }

    @NonNull
    @SuppressWarnings("WeakerAccess")
    public ItemData.ItemDataView get(int index) {
        if (index >= this.lookup.size()) {
            throw new IllegalStateException("view was not registered");
        }

        ItemData.ItemDataView view = this.lookup.get(index);
        if (view == null) {
            throw new IllegalStateException("view was not registered");
        }

        return view;
    }
}
