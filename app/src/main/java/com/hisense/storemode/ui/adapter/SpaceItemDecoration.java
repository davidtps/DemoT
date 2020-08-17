package com.hisense.storemode.ui.adapter;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Set the item space.
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int mBottom;
    private int mRight;

    private static final String TAG = SpaceItemDecoration.class.getSimpleName();

    public SpaceItemDecoration(int bottom, int right) {
        this.mBottom = bottom;
        this.mRight = right;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
        if (layoutManager.getOrientation() == RecyclerView.VERTICAL) {
            outRect.bottom = mBottom;
            outRect.right = mRight;
        }
    }
}

