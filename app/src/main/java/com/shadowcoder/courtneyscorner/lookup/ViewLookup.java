package com.shadowcoder.courtneyscorner.lookup;

import android.support.annotation.NonNull;

import com.shadowcoder.courtneyscorner.data.Coordinate;
import com.shadowcoder.courtneyscorner.data.ItemData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ViewLookup {

    private static Map<LookupType, ViewLookup> registry = new HashMap<>();
    private List<ItemData.ItemDataView> lookup = new ArrayList<>();

    public static ViewLookup get(@NonNull LookupType type) {
        ViewLookup lookup = registry.get(type);
        if (lookup == null) {
            lookup = type.get();
            registry.put(type, lookup);
        }

        return lookup;
    }

    ViewLookup(@NonNull LookupType type) {
        ViewLookup test = registry.get(type);
        if (test != null) {
            throw new IllegalStateException(String.format("already registered lookup for type: %s", type.toString()));
        }

        registry.put(type, this);
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
