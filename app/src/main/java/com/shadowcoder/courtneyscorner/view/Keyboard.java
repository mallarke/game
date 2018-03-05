package com.shadowcoder.courtneyscorner.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shadowcoder.courtneyscorner.R;

public class Keyboard extends FrameLayout {

    private static final int MAX_NUMBER_OF_KEYS_ON_ROW = 10;
    private static final int CORNER_RADIUS = 20;
    private static final int FILL_COLOR = 0xAAFFFFFF;
    private static final int HIGHLIGHT_COLOR = 0X444BE6FB;

    private OnKeyboardClickListener listener;
    private MyKeyClickListener keyClickListener = new MyKeyClickListener();

    private FrameLayout innerLayout;
    private Button backspace;

    public interface OnKeyboardClickListener {
        void onClick(@NonNull String text);
        void onBackspace();
    }

    public Keyboard(@NonNull Context context) {
        super(context);
        this.setUp(context);
    }

    public Keyboard(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.setUp(context);
    }

    public Keyboard(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setUp(context);
    }

    @SuppressWarnings("unused")
    public Keyboard(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.setUp(context);
    }

    public void setOnKeyboardClickListener(@Nullable OnKeyboardClickListener listener) {
        this.listener = listener;
    }

    private void setUp(Context context) {
        this.setBackgroundColor(getResources().getColor(R.color.colorBackground));

        this.innerLayout = new FrameLayout(context);
        this.addView(this.innerLayout);

        this.addRows(context);
        this.addBackspace(context);
    }

    private void addRows(Context context) {
        this.generateRow(context, new String[] {"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"});
        this.generateRow(context, new String[] {"A", "S", "D", "F", "G", "H", "J", "K", "L"});
        this.generateRow(context, new String[] {"Z", "X", "C", "V", "B", "N", "M"});
    }

    private void addBackspace(Context context) {
        this.backspace = new Button(context);
        this.backspace.setText("Bs");
        this.backspace.setOnClickListener(new MyBackspaceClickListener());
        this.addView(this.backspace);
    }

    private void generateRow(Context context, String[] values) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.HORIZONTAL);

        for (String value : values) {
            Key key = new Key(context, value);
            key.setOnClickListener(this.keyClickListener);
            layout.addView(key);
        }

        this.innerLayout.addView(layout, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (this.innerLayout.getChildCount() != 3) {
            return;
        }

        int margin = 20;
        int workingWidth = r - l;
        int workingHeight = b - t;

        this.innerLayout.layout(margin, margin, workingWidth - margin, workingHeight - margin);
        int width = this.innerLayout.getWidth();
        int height = this.innerLayout.getHeight();

        int itemWidth = width / MAX_NUMBER_OF_KEYS_ON_ROW;
        int rowHeight = height / this.innerLayout.getChildCount();

        int layoutT = 0;

        for (int i = 0; i < 3; i++) {
            LinearLayout layout = (LinearLayout) this.innerLayout.getChildAt(i);

            int count = layout.getChildCount();
            if (count == 0) {
                layout.layout(0, layoutT, width, 0);
                continue;
            }

            int layoutWidth = itemWidth * count;
            int layoutL = (width - layoutWidth) / 2;
            int layoutR = layoutL + layoutWidth;
            int layoutB = layoutT + rowHeight;
            layout.layout(layoutL, layoutT, layoutR, layoutB);

            int x = 0;

            for (int j = 0; j < count; j++) {
                View view = layout.getChildAt(j);
                view.layout(x, 0, x + itemWidth, rowHeight);

                x += itemWidth;
            }

            layoutT += rowHeight;
        }

        int layoutL = workingWidth - margin - itemWidth;
        layoutT = margin + (rowHeight * 2);

        this.backspace.layout(layoutL, layoutT, layoutL + itemWidth, layoutT + rowHeight);
    }

    private void handleKeyPress(String key) {
        if (this.listener != null) {
            this.listener.onClick(key);
        }
    }

    private void handleBackspacePress() {
        if (this.listener != null) {
            this.listener.onBackspace();
        }
    }

    private static class Key extends FrameLayout {

        private final String value;

        private TextView textView;

        private final Path path = new Path();
        private final Paint strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final Paint fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        private final int strokeColor;

        public Key(Context context, String value) {
            super(context);

            this.value = value;

            this.strokeColor = getResources().getColor(R.color.black);

            this.setWillNotDraw(false);

            this.textView = new TextView(context);
            this.textView.setText(value);
            this.textView.setTextSize(22);
            this.textView.setTypeface(this.textView.getTypeface(), Typeface.BOLD);
            this.addView(this.textView, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            this.strokePaint.setColor(this.strokeColor);
            this.strokePaint.setAntiAlias(true);
            this.strokePaint.setStyle(Paint.Style.STROKE);

            this.fillPaint.setColor(FILL_COLOR);
            this.fillPaint.setAntiAlias(true);
            this.fillPaint.setStyle(Paint.Style.FILL);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);

            this.path.reset();
            this.path.addRoundRect(0, 0, w, h, CORNER_RADIUS, CORNER_RADIUS, Path.Direction.CW);
        }

        @Override
        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            int width = right - left;
            int height = bottom - top;

            int textWidth = this.textView.getMeasuredWidth();
            int textHeight = this.textView.getMeasuredHeight();
            int textL = (width - textWidth) / 2;
            int textT = (height - textHeight) / 2;
            int textR = textL + textWidth;
            int textB = textT + textHeight;
            this.textView.layout(textL, textT, textR, textB);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            canvas.drawPath(this.path, this.fillPaint);
            canvas.drawPath(this.path, this.strokePaint);
        }

        private void cyclePress() {
            this.fillPaint.setColor(HIGHLIGHT_COLOR);
            this.invalidate();

            this.postDelayed(new Runnable() {
                @Override
                public void run() {
                    fillPaint.setColor(FILL_COLOR);
                    invalidate();
                }
            }, 100);
        }
    }

    private class MyKeyClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v instanceof Key) {
                Key key = (Key) v;
                key.cyclePress();

                handleKeyPress(key.value);
            }
        }
    }

    private class MyBackspaceClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            handleBackspacePress();
        }
    }
}
