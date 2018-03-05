package com.shadowcoder.courtneyscorner.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

import com.shadowcoder.courtneyscorner.R;
import com.shadowcoder.courtneyscorner.data.Coordinate;

public class Header extends FrameLayout {

    public interface OnHeaderTouchListener {
        void onLeft();
        void onRight();
        void onFlipDirection();
    }

    private static final float TOUCH_AREA_VALUE = 0.23f;

    private enum TouchState {
        LEFT, CENTER, RIGHT;

        static TouchState stateForTouch(View v, MotionEvent event) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            int width = v.getWidth();
            int workableWidth = (int)(width * TOUCH_AREA_VALUE);
            int height = v.getHeight();


            Rect left = new Rect(0, 0, workableWidth, height);
            if (left.contains(x, y)) {
                return LEFT;
            }

            Rect right = new Rect(width - workableWidth, 0, width, height);
            if (right.contains(x, y)) {
                return RIGHT;
            }

            return CENTER;
        }
    }

    private View leftView;
    private View rightView;

    private OnHeaderTouchListener onTouchListener;

    private MotionEventState motionEventState;
    private TouchState touchState = TouchState.CENTER;

    public Header(@NonNull Context context) {
        super(context);
        this.setUp(context);
    }

    public Header(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.setUp(context);
    }

    public Header(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setUp(context);
    }

    @SuppressWarnings("unused")
    public Header(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.setUp(context);
    }

    private void setUp(Context context) {
        this.setBackgroundColor(getResources().getColor(R.color.colorBackground));

        leftView = new View(context);
        leftView.setBackgroundColor(getResources().getColor(android.R.color.holo_purple));
        addView(leftView);

        rightView = new View(context);
        rightView.setBackgroundColor(getResources().getColor(android.R.color.holo_purple));
        addView(rightView);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        final int width = right - left;
        final int workingWidth = (int)(width * TOUCH_AREA_VALUE);
        final int height = bottom - top;

        leftView.layout(0, 0, workingWidth, height);
        rightView.layout(width - workingWidth, 0, width, height);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if (action == MotionEvent.ACTION_MOVE) {
            int bob = 0;
            bob = 0;
        }
        return  (ev.getAction() != MotionEvent.ACTION_MOVE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                ViewConfiguration configuration = ViewConfiguration.get(this.getContext());
                this.motionEventState = new MotionEventState(event, configuration.getScaledTouchSlop());
                break;

            case MotionEvent.ACTION_MOVE:
                this.motionEventState.last = new Coordinate(event);
                break;

            case MotionEvent.ACTION_UP:
                Log.d("bob", "up");
                if (!this.motionEventState.isDragging()) {
                    // click
                    this.touchState = TouchState.stateForTouch(this, event);
                    performClick();
                }

                this.motionEventState = null;
                break;

            case MotionEvent.ACTION_CANCEL:
                this.motionEventState = null;
                break;
        }

        return true;
    }

    @Override
    public boolean performClick() {
        switch (this.touchState) {
            case LEFT:
                this.handleLeftClick();
                break;

            case CENTER:
                this.handleCenterClick();
                break;

            case RIGHT:
                this.handleRightClick();
                break;
        }

        return super.performClick();
    }

    public void setOnHeaderTouchListener(@Nullable OnHeaderTouchListener listener) {
        this.onTouchListener = listener;
    }

    private void handleLeftClick() {
        if (this.onTouchListener != null) {
            this.onTouchListener.onLeft();
        }
    }

    private void handleCenterClick() {
        if (this.onTouchListener != null) {
            this.onTouchListener.onFlipDirection();
        }
    }

    private void handleRightClick() {
        if (this.onTouchListener != null) {
            this.onTouchListener.onRight();
        }
    }

    private static class MotionEventState {
        private Coordinate start;
        private Coordinate last;

        private int touchSlop;

        MotionEventState(MotionEvent event, int touchSlop) {
            this.start = new Coordinate(event);
            this.touchSlop = touchSlop;
        }

        boolean isDragging() {
            return !(this.last == null || this.compareCoordinate(this.start, this.last));
        }

        private boolean compareCoordinate(Coordinate c1, Coordinate c2) {
            if (c1.equals(c2)) {
                return true;
            }

            int l = c1.x - (c1.x + this.touchSlop);
            int t = c1.y - (c1.y + this.touchSlop);
            int r = c1.x + (c1.x + this.touchSlop);
            int b = c1.y + (c1.y + this.touchSlop);

            Rect boundingBox = new Rect(l, t, r, b);
            return boundingBox.contains(c2.x, c2.y);
        }
    }
}
