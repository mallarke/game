package com.shadowcoder.courtneyscorner.data;

import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.shadowcoder.courtneyscorner.R;

public class ItemData {

    public interface ItemDataView {
        @Nullable String getText();
        void setText(@Nullable String text);
        void setHighlight(boolean highlighted);
        void setFocus(boolean focused);
    }

    private final @NonNull Coordinate position;
    private final @Nullable String index;
    private final @Nullable String text;
    private final @ColorRes int backgroundColor;

    private final boolean isValid;

    public ItemData(@NonNull Coordinate position) {
        this(position, null, null, false);
    }

    ItemData(@NonNull Coordinate position, @Nullable Integer index, @NonNull String text) {
        this(position, index, text, true);
    }

    private ItemData(@NonNull Coordinate position, @Nullable Integer index, @Nullable String text, boolean isValid) {
        this.position = position;
        this.index = (index != null) ? String.valueOf(index) : null;
        this.text = (text != null) ? text.toUpperCase() : null;
        this.isValid = isValid;
        this.backgroundColor = (isValid) ? R.color.white : R.color.black;
    }

    @NonNull
    public Coordinate getPosition() {
        return this.position;
    }

    @Nullable
    public String getIndex() {
        return this.index;
    }

    @NonNull
    public String getText() {
        return (this.text == null) ? "" : this.text;
    }

    @ColorRes
    public int getBackgroundColor() {
        return this.backgroundColor;
    }

    public boolean isValid() {
        return isValid;
    }
}


