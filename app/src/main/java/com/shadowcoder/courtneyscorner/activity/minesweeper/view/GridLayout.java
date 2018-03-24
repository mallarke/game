package com.shadowcoder.courtneyscorner.activity.minesweeper.view;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.shadowcoder.courtneyscorner.adapter.GridAdapter;


public abstract class GridLayout<DATA> extends FrameLayout {

    private GridAdapter<DATA> adapter;

    public GridLayout(@NonNull Context context) {
        super(context);
        this.setUp(context);
    }

    public GridLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.setUp(context);
    }

    public GridLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setUp(context);
    }

    @SuppressWarnings("unused")
    public GridLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.setUp(context);
    }

    private void setUp(@NonNull Context context) {

    }

    /*
     * GETTER/SETTER METHODS
     */

    public void setAdapter(@Nullable GridAdapter<DATA> adapter) {
        this.adapter = adapter;

        if (adapter != null) {
            adapter.registerDataSetObserver(new MyDataSetObservable());
        }

        this.refresh();
    }

    /*
     * PRIVATE METHODS
     */

    private void refresh() {
        this.removeAllViews();

        if (this.adapter == null || this.adapter.getCount() == 0) {
            return;
        }

        for (int i = 0; i < this.adapter.getCount(); i++) {
            View view = this.adapter.getView(i, null, this);
            this.addView(view);
        }
    }

    /*
     * LAYOUT METHODS
     */

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);


        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    private class MyDataSetObservable extends DataSetObserver {
        @Override
        public void onChanged() {
            super.onChanged();
            refresh();
        }
    }
}
