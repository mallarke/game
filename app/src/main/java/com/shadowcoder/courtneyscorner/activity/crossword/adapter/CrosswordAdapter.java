package com.shadowcoder.courtneyscorner.activity.crossword.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.shadowcoder.courtneyscorner.data.Coordinate;
import com.shadowcoder.courtneyscorner.data.CrosswordData;
import com.shadowcoder.courtneyscorner.data.ItemData;
import com.shadowcoder.courtneyscorner.data.ViewLookup;
import com.shadowcoder.courtneyscorner.notification.EventBus;
import com.shadowcoder.courtneyscorner.activity.crossword.view.CrosswordItemView;

import java.util.ArrayList;
import java.util.List;

public class CrosswordAdapter extends BaseAdapter {

    public interface OnItemClickListener {
        void onClick(@NonNull Coordinate coordinate);
    }

    private @NonNull List<ItemData> data = new ArrayList<>();

    private final ViewLookup viewLookup;

    private ArrayList<CrosswordItemView> views = new ArrayList<>();
    private MyItemClickListener itemClickListener = new MyItemClickListener();
    private OnItemClickListener externalItemClickListener;

    public CrosswordAdapter(@NonNull ViewLookup viewLookup) {
        this.viewLookup = viewLookup;
    }

    public void setCrosswordData(@Nullable CrosswordData data) {
        this.unregisterViews();
        this.views.clear();

        if (data != null) {
            this.data = AdapterUtils.getItemData(data);
        }
        else {
            this.data.clear();
        }

        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return this.data.size();
    }

    @Override
    public ItemData getItem(int position) {
        return this.data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemData data = this.getItem(position);

        CrosswordItemView view = (convertView == null) ? new CrosswordItemView(parent.getContext()) : (CrosswordItemView) convertView;
        view.setOnClickListener(this.itemClickListener);
        view.setData(data);

        this.views.add(view);
        this.viewLookup.register(position, view);

        return view;
    }

    public void setOnItemClickListener(@Nullable OnItemClickListener listener) {
        this.externalItemClickListener = listener;
    }

    public void registerViews() {
        for (int i = 0; i < this.views.size(); i++) {
            CrosswordItemView view = this.views.get(i);
            EventBus.getSingleton().register(view);
            this.viewLookup.register(i, view);
        }
    }

    public void unregisterViews() {
        for (CrosswordItemView view: this.views) {
            EventBus.getSingleton().unregister(view);
        }

        this.viewLookup.clear();
    }

    private class MyItemClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v instanceof CrosswordItemView && externalItemClickListener != null) {
                CrosswordItemView view = (CrosswordItemView) v;
                if (!view.isValid()) {
                    return;
                }

                externalItemClickListener.onClick(view.getData().getPosition());
            }
        }
    }
}

