package com.hisense.hianidraw.draw.impl;

import android.text.TextUtils;

import com.hisense.hianidraw.draw.DrawStage;

/**
 * Create by xuchongxiang at 2019-05-07 10:37 AM
 */

public class DrawStageImpl implements DrawStage {

    private String mName;
    private long mStart;
    private long mEnd;
    private long mOffset;

    public DrawStageImpl(String name, long start, long end) {
        this(name, start, end, 0);
    }

    public DrawStageImpl(String name, long start, long end, long offset) {
        mName = name;
        mStart = start;
        mEnd = end;
        mOffset = offset;
    }


    @Override
    public long getStart() {
        return mStart;
    }

    @Override
    public long getEnd() {
        return mEnd;
    }

    @Override
    public String getName() {
        return !TextUtils.isEmpty(mName) ? mName : "TO MODIFY name";
    }

    @Override
    public long getStartOffset() {
        return mOffset;
    }

    @Override
    public float getProgress(long time) {
        long inTime = time - getStart() - mOffset;
        return Math.max(0.0F, (float) inTime / (float) (getEnd() - (getStart() + getStartOffset())));
    }

    @Override
    public boolean contains(long value) {
        return value >= getStart() && value <= getEnd();
    }
}
