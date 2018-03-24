package com.shadowcoder.courtneyscorner.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;


public abstract class GridAdapter<DATA> extends BaseAdapter {

    protected List<DATA> data = new ArrayList<>();

    public abstract int getColumnCount();
    public abstract int getRowCount();

    public void setData(@Nullable List<DATA> data) {
        this.data.clear();

        if (data != null) {
            this.data.addAll(data);
        }

        this.notifyDataSetChanged();
    }

    @Override
    public final int getCount() {
        return this.data.size();
    }

    @Override
    public final DATA getItem(int position) {
        return (this.data != null ? this.data.get(position) : null);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public abstract View getView(@NonNull DATA data, int x, int y, @Nullable View convertView, @NonNull ViewGroup parent);

    @Override
    public final View getView(int position, View convertView, ViewGroup parent) {
        int columnCount = this.getColumnCount();
        if (columnCount <= 0) {
            throw new IllegalStateException("getColumnCount must be > than 0");
        }

        int rowCount = this.getRowCount();
        if (rowCount <= 0) {
            throw new IllegalStateException("getRowCount must be > than 0");
        }

        DATA data = this.data.get(position);
        int y = (int) Math.floor(position / columnCount);
        int x = position - (columnCount * y);

        return this.getView(data, x, y, convertView, parent);
    }
}
