package com.shadowcoder.courtneyscorner.activity.crossword.view;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.shadowcoder.courtneyscorner.R;
import com.shadowcoder.courtneyscorner.data.ItemData;

public class CrosswordItemView extends FrameLayout implements ItemData.ItemDataView {

    private static final int POSITION_SPACING = 4;
    private static final float POSITION_TEXT_SIZE = 5f;
    private static final float VALUE_TEXT_SIZE = 18f;

    private ItemData data;
    private TextView position;
    private TextView value;

    private HighlightLayer highlightLayer;

    private boolean highlighted;
    private boolean focused;

    public CrosswordItemView(Context context) {
        super(context);

        this.highlightLayer = new HighlightLayer(context);
        this.addView(this.highlightLayer, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        this.value = new TextView(context);
        this.value.setBackgroundColor(getResources().getColor(R.color.transparent));
        this.value.setTextSize(VALUE_TEXT_SIZE);
        this.value.setTypeface(null, Typeface.BOLD);
        this.value.setTextColor(getResources().getColor(R.color.black));
        this.addView(this.value, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        this.position = new TextView(context);
        this.position.setBackgroundColor(getResources().getColor(R.color.transparent));
        this.position.setTextSize(POSITION_TEXT_SIZE);
        this.position.setTypeface(null, Typeface.BOLD);
        this.addView(this.position, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public ItemData getData() {
        return this.data;
    }

    public void setData(@NonNull ItemData data) {
        this.data = data;

        this.position.setText(data.getIndex());
        this.value.setText(null);
        this.updateState();
        this.invalidate();
        this.value.setText(data.getText());
    }

    @Nullable
    @Override
    public String getText() {
        String text = this.value.getText().toString();
        return (text.length() == 0) ? null : text;
    }

    @Override
    public void setText(@Nullable String text) {
        if (this.data == null) {
            throw new IllegalStateException("data must be set before using CrosswordItemView");
        }

        this.value.setText(text);
        this.updateState();
        this.invalidate();
    }

    @Override
    public void setHighlight(boolean highlighted) {
        this.highlighted = highlighted;
        this.updateState();
    }

    @Override
    public void setFocus(boolean focused) {
        this.focused = focused;
        this.updateState();
    }

    public boolean isValid() {
        return this.data.isValid();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int width = r - l;
        final int height = b - t;

        int positionWidth = this.position.getMeasuredWidth();
        int positionHeight = this.position.getMeasuredHeight();

        int positionL = POSITION_SPACING;
        int positionT = POSITION_SPACING;
        int positionR = positionL + positionWidth;
        int positionB = positionT + positionHeight;
        this.position.layout(positionL, positionT, positionR, positionB);

        int valueWidth = this.value.getMeasuredWidth();
        int valueHeight = this.value.getMeasuredHeight();

        int valueL = (width - valueWidth) / 2;
        int valueT = (height - valueHeight) / 2;
        int valueR = valueL + valueWidth;
        int valueB = valueT + valueHeight;
        this.value.layout(valueL, valueT, valueR, valueB);

        this.highlightLayer.layout(0, 0, width, height);
    }

    private void updateState() {
        if (this.data == null) {
            this.setBackgroundColor(getResources().getColor(R.color.black));
            this.value.setTextColor(getResources().getColor(R.color.black));
            this.highlightLayer.clear();

            return;
        }

        String text = this.value.getText().toString();
        boolean hasError = !(text.length() == 0 || this.data.getText().equalsIgnoreCase(text));

        if (!hasError) {
            this.value.setTextColor(getResources().getColor(R.color.black));
            this.setBackgroundColor(getResources().getColor(data.getBackgroundColor()));
        }
        else {
            this.value.setTextColor(getResources().getColor(R.color.white));
            this.setBackgroundColor(getResources().getColor(R.color.red));
        }

        this.highlightLayer.setHighlighted(this.highlighted);
        this.highlightLayer.setFocused(this.focused);
    }

    private static class HighlightLayer extends FrameLayout {

        private View highlightLayer;
        private View focusLayer;

        public HighlightLayer(Context context) {
            super(context);
            setUp(context);
        }

        public HighlightLayer(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            setUp(context);
        }

        public HighlightLayer(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            setUp(context);
        }

        public HighlightLayer(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
            setUp(context);
        }

        private void setUp(Context context) {
            setBackgroundColor(getResources().getColor(R.color.transparent));

            highlightLayer = new View(context);
            highlightLayer.setBackgroundColor(getResources().getColor(R.color.transparent));
            addView(highlightLayer, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            focusLayer = new View(context);
            focusLayer.setBackgroundColor(getResources().getColor(R.color.transparent));
            addView(focusLayer, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            final int width = r - l;
            final int height = b - t;

            this.highlightLayer.layout(0, 0, width, height);
            this.focusLayer.layout(0, 0, width, height);
        }

        void clear() {
            int color = getResources().getColor(R.color.transparent);
            this.highlightLayer.setBackgroundColor(color);
            this.focusLayer.setBackgroundColor(color);
        }

        void setHighlighted(boolean highlighted) {
            int color = (highlighted ? R.color.colorHighlight : R.color.transparent);
            this.highlightLayer.setBackgroundColor(getResources().getColor(color));
        }

        void setFocused(boolean focused) {
            int color = (focused ? R.color.colorFocus : R.color.transparent);
            this.focusLayer.setBackgroundColor(getResources().getColor(color));
        }
    }
}
