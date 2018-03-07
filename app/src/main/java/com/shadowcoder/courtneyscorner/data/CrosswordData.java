package com.shadowcoder.courtneyscorner.data;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CrosswordData {

    private final DataCache horizontalCache = new DataCache();
    private final DataCache verticalCache = new DataCache();

    public CrosswordData(@NonNull List<WordData> horizontalData, @NonNull List<WordData> verticalData) {
        this.horizontalCache.add(horizontalData);
        this.verticalCache.add(verticalData);
    }

    public boolean hasData() {
        return (this.horizontalCache.size() > 0 && this.verticalCache.size() > 0);
    }

    @NonNull
    public List<WordData> getHorizontalWords() {
        return this.horizontalCache.dataLookup;
    }

    @NonNull
    public List<WordData> getVerticalWords() {
        return this.verticalCache.dataLookup;
    }

    @NonNull
    public DataCache getCache(@NonNull Direction direction) {
        return (direction == Direction.HORIZONTAL ? this.horizontalCache : this.verticalCache);
    }

    public class DataCache {

        private HashMap<Coordinate, LookupValue> coordinateLookup = new HashMap<>();
        private List<WordData> dataLookup = new ArrayList<>();

        @SuppressWarnings("unused")
        private void clear() {
            this.dataLookup.clear();
            this.coordinateLookup.clear();
        }

        private void add(@NonNull List<WordData> items) {
            int currentSize = this.dataLookup.size();
            this.dataLookup.addAll(items);

            for (int i = 0; i < items.size(); i++) {
                WordData data = items.get(i);

                int index = i + currentSize;

                int start = (data.getDirection() == Direction.HORIZONTAL ? data.getStartPosition().x : data.getStartPosition().y);
                int end = (data.getDirection() == Direction.HORIZONTAL ? data.getEndPosition().x : data.getEndPosition().y);

                for (int j = start; j <= end; j++) {
                    int x = (data.getDirection() == Direction.HORIZONTAL ? j : data.getStartPosition().x);
                    int y = (data.getDirection() == Direction.HORIZONTAL ? data.getStartPosition().y : j);

                    this.coordinateLookup.put(new Coordinate(x, y), new LookupValue(data, index));
                }
            }
        }

        public int size() {
            return this.dataLookup.size();
        }

        @NonNull
        public CacheResult get(int index) {
            WordData data = this.dataLookup.get(index);
            return new CacheResult(data, index);
        }

        @NonNull
        public CacheResult lookup(@NonNull Coordinate coordinate) {
            LookupValue value = this.coordinateLookup.get(coordinate);

            if (value == null) {
                return new CacheResult();
            }

            return new CacheResult(value.data, value.index);
        }
    }

    public static class CacheResult {
        public final WordData data;
        public final int index;
        public final Coordinate coordinate;

        // error state
        private CacheResult() {
            this.data = null;
            this.index = 0;
            this.coordinate = null;
        }

        private CacheResult(WordData data, int index) {
            this.data = data;
            this.index = index;
            this.coordinate = data.getStartPosition();
        }
    }

    private static class LookupValue {

        private WordData data;
        private int index;

        LookupValue(WordData data, int index) {
            this.data = data;
            this.index = index;
        }
    }
}
