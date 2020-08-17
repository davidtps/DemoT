package com.hisense.storemode.ui.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import com.hisense.storemode.utils.LogUtils;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * fix focus problem
 */
public class FocusGridLayoutManager extends GridLayoutManager {

    private static final String TAG = "FocusGridLayoutManager";

    private final Rect mChildViewRect = new Rect();
    private int mSelectedItemOffsetStart;
    private int mSelectedItemOffsetEnd;
    private boolean mSelectedItemCentered;

    public FocusGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public void setSelectedItemCentered(boolean centered) {
        mSelectedItemCentered = centered;
    }

    @Override
    public View onInterceptFocusSearch(View focused, int direction) {
        int count = getItemCount();//item count
        int currentPosition = getPosition(focused);
        int lastVisibleItemPosition = findLastVisibleItemPosition();// the last visible item
        int nextPosition = Math.max(0, getNextViewPosition(currentPosition, direction));
        LogUtils.d(TAG, "onInterceptFocusSearch():count = " + count + "; currentPosition = "
                + currentPosition + "; lastVisibleItemPosition = " + lastVisibleItemPosition
                + "; nextPosition = " + nextPosition);

        if (nextPosition < 0 || nextPosition >= count) {
            // return current view, focus not changed
            return focused;
        } else {
            // scroll to next position
            if (nextPosition > lastVisibleItemPosition) {
                scrollToPosition(nextPosition);
            }
        }
        return super.onInterceptFocusSearch(focused, direction);
    }

    @Override
    public View onFocusSearchFailed(View focused, int focusDirection, RecyclerView.Recycler recycler, RecyclerView.State state) {
        View nextFocus = super.onFocusSearchFailed(focused, focusDirection, recycler, state);

        if (nextFocus == null) {
            LogUtils.d(TAG, "onFocusSearchFailed():nextFocus == null");
            return null;
        }
        //step1: get current focus position
        int currentPosition = getPosition(focused);

        //step2: get next focus position
        int nextPosition = getNextViewPosition(currentPosition, focusDirection);
        nextPosition = Math.max(0, nextPosition);

        View nextView = findViewByPosition(nextPosition);
        if (nextView != null) {
            int nextPostion = getPosition(nextView);
            nextView.requestFocus();
            LogUtils.d(TAG, "onFocusSearchFailed():nextPostion = " + nextPostion);
        }
        LogUtils.d(TAG, "onFocusSearchFailed():fromPosition = " + currentPosition + "; nextPosition = " + nextPosition + "; nextView = " + nextView);

        return nextView;
    }

    private int getNextViewPosition(int fromPosition, int direction) {
        int offset = getOffsetToNextView(direction);
        if (hitBorder(fromPosition, offset)) {
            return fromPosition;
        }

        return fromPosition + offset;
    }

    private int getOffsetToNextView(int direction) {
        int spanCount = getSpanCount();
        int orientation = getOrientation();

        if (orientation == VERTICAL) {
            switch (direction) {
                case View.FOCUS_DOWN:
                    return spanCount;
                case View.FOCUS_UP:
                    return -spanCount;
                case View.FOCUS_RIGHT:
                    return 1;
                case View.FOCUS_LEFT:
                    return -1;
            }
        } else if (orientation == HORIZONTAL) {
            switch (direction) {
                case View.FOCUS_DOWN:
                    return 1;
                case View.FOCUS_UP:
                    return -1;
                case View.FOCUS_RIGHT:
                    return spanCount;
                case View.FOCUS_LEFT:
                    return -spanCount;
            }
        }

        return 0;
    }

    private boolean hitBorder(int from, int offset) {
        int spanCount = getSpanCount();

        if (Math.abs(offset) == 1) {
            int spanIndex = from % spanCount;
            int newSpanIndex = spanIndex + offset;
            return newSpanIndex < 0 || newSpanIndex >= spanCount;
        } else {
            int newPosition = from + offset;
            return newPosition < 0 && newPosition >= spanCount;
        }
    }
}