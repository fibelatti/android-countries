package com.fibelatti.countries.presentation.ui.extensions;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class RecyclerTouchListener
        implements RecyclerView.OnItemTouchListener {
    private GestureDetector gestureDetector;
    private OnItemTouchListener itemTouchListener;
    private OnItemLongPressListener itemLongPressListener;
    @Nullable
    private View childView;
    private int childViewPosition;

    public interface OnItemTouchListener {
        void onItemTouch(View view, int position);
    }

    public interface OnItemLongPressListener {
        void onItemLongPress(View view, int position);
    }

    private RecyclerTouchListener(Context context) {
        this.gestureDetector = new GestureDetector(context, new GestureListener());
    }

    private void setOnItemTouchListener(OnItemTouchListener listener) {
        this.itemTouchListener = listener;
    }

    private void setOnItemLongPressListener(OnItemLongPressListener listener) {
        this.itemLongPressListener = listener;
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        childView = rv.findChildViewUnder(e.getX(), e.getY());
        childViewPosition = rv.getChildAdapterPosition(childView);

        return childView != null && itemTouchListener != null && gestureDetector.onTouchEvent(e);
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }

    protected class GestureListener
            extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            if (childView != null && itemTouchListener != null) {
                itemTouchListener.onItemTouch(childView, childViewPosition);
            }

            return true;
        }

        @Override
        public void onLongPress(MotionEvent event) {
            if (childView != null && itemLongPressListener != null) {
                itemLongPressListener.onItemLongPress(childView, childViewPosition);
            }
        }

        @Override
        public boolean onDown(MotionEvent event) {
            // Best practice to always return true here.
            // http://developer.android.com/training/gestures/detector.html#detect
            return true;
        }
    }

    public static class Builder {
        final RecyclerTouchListener rtListener;

        public Builder(Context context) {
            rtListener = new RecyclerTouchListener(context);
        }

        public Builder setOnItemTouchListener(OnItemTouchListener listener) {
            rtListener.setOnItemTouchListener(listener);
            return this;
        }

        public Builder setOnItemLongPressListener(OnItemLongPressListener listener) {
            rtListener.setOnItemLongPressListener(listener);
            return this;
        }

        public RecyclerTouchListener build() {
            return rtListener;
        }
    }
}
