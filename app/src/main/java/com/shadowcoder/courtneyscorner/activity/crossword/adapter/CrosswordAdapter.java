package com.shadowcoder.courtneyscorner.activity.crossword.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.shadowcoder.courtneyscorner.data.Coordinate;
import com.shadowcoder.courtneyscorner.data.CrosswordData;
import com.shadowcoder.courtneyscorner.data.ItemData;
import com.shadowcoder.courtneyscorner.lookup.LookupType;
import com.shadowcoder.courtneyscorner.lookup.ViewLookup;
import com.shadowcoder.courtneyscorner.notification.EventBus;
import com.shadowcoder.courtneyscorner.activity.crossword.view.CrosswordItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * An adapter to manage the view creation for a crossword.
 */
public class CrosswordAdapter extends BaseAdapter {

    public interface OnItemClickListener {
        void onClick(@NonNull Coordinate coordinate);
    }

    private @NonNull List<ItemData> data = new ArrayList<>();

    private ArrayList<CrosswordItemView> views = new ArrayList<>();
    private MyItemClickListener itemClickListener = new MyItemClickListener();
    private OnItemClickListener externalItemClickListener;

    /*
     * GETTER/SETTER METHODS
     */

    /**
     * Setter for the {@link CrosswordData}.  This clears the previous view state.
     *
     * For the purpose of this adapter, the {@link ItemData} is flattened out to make for easier
     * view creation.
     *
     * @param data the {@link CrosswordData} to display
     */
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
        this.getViewLookup().register(position, view);

        return view;
    }

    public void setOnItemClickListener(@Nullable OnItemClickListener listener) {
        this.externalItemClickListener = listener;
    }

    /*
     * PUBLIC METHODS
     */

    /**
     * Register the adapter views with the {@link EventBus} and the {@link ViewLookup}
     */
    public void registerViews() {
        for (int i = 0; i < this.views.size(); i++) {
            CrosswordItemView view = this.views.get(i);
            EventBus.getSingleton().register(view);
            this.getViewLookup().register(i, view);
        }
    }

    /**
     * Unregister the adapter views with the {@link EventBus} and the {@link ViewLookup}
     */
    public void unregisterViews() {
        for (CrosswordItemView view: this.views) {
            EventBus.getSingleton().unregister(view);
        }

        this.getViewLookup().clear();
    }

    /*
     * PRIVATE METHODS
     */

    private ViewLookup getViewLookup() {
        return ViewLookup.get(LookupType.CROSSWORD);
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

