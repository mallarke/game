package com.shadowcoder.courtneyscorner.activity.crossword.view;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;

import com.shadowcoder.courtneyscorner.Utils;

public class CrosswordLayout extends FrameLayout {

    public static final int CROSSWORD_LENGTH = 13;
    public static final int CROSSWORD_SIZE = CROSSWORD_LENGTH * CROSSWORD_LENGTH;

    public interface OnViewLoadListener {
        void onStart();
        void onFinish();
    }

    private int cellSize;

    private BaseAdapter adapter;
    private OnViewLoadListener loadListener;

    private final Utils utils = new Utils();

    public CrosswordLayout(@NonNull Context context) {
        super(context);
    }

    public CrosswordLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CrosswordLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressWarnings("unused")
    public CrosswordLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setAdapter(@Nullable BaseAdapter adapter) {
        this.adapter = adapter;
        this.adapter.registerDataSetObserver(new MyDataSetObservable());
        this.refresh();
    }

    public void setOnViewLoadListener(OnViewLoadListener listener) {
        this.loadListener = listener;
    }

    private void refresh() {
        if (this.loadListener != null) {
            this.loadListener.onStart();
        }

        this.removeAllViews();

        if (this.adapter == null || this.adapter.getCount() == 0) {
            if (this.loadListener != null) {
                this.loadListener.onFinish();
            }

            return;
        }

        for (int i = 0; i < this.adapter.getCount(); i++) {
            this.addView(this.adapter.getView(i, null, this));
        }

        if (this.loadListener != null) {
            this.loadListener.onFinish();
        }
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);

        int spacing = this.utils.convertDp(2, this.getContext());
        int totalSpacing = (CROSSWORD_LENGTH + 1) * spacing;
        int workingWidth = width - totalSpacing;

        this.cellSize = workingWidth / CROSSWORD_LENGTH;

        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int childCount = this.getChildCount();
        if (childCount == 0) {
            return;
        }

        int workingWidth = right - (CROSSWORD_LENGTH * this.cellSize);
        int spacing = workingWidth / (CROSSWORD_LENGTH - 2);

        int x = 0, y = 0;

        for (int i = 0, column = 0, row = 0; i < childCount; row++, i++) {
            if (row == CROSSWORD_LENGTH) {
                column++;
                row = 0;

                x = 0;
                y = column * (this.cellSize + spacing);
            }

            int width = x + this.cellSize;
            int height = y + this.cellSize;

            CrosswordItemView view = (CrosswordItemView) this.getChildAt(i);
            view.layout(x, y, width, height);

            x += this.cellSize + spacing;
        }
    }

    private class MyDataSetObservable extends DataSetObserver {
        @Override
        public void onChanged() {
            super.onChanged();
            refresh();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
        }
    }
}
