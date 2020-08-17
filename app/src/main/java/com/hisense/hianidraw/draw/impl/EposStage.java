package com.hisense.hianidraw.draw.impl;

import com.hisense.hianidraw.draw.DrawStage;

/**
 * Create by xuchongxiang at 2019-05-07 10:37 AM
 */

public class EposStage implements DrawStage {

    private long mStart;
    private long mEnd;
    private String mName;

    public EposStage(long start, long end, String name) {
        super();
        mStart = start;
        mEnd = end;
        mName = name;
    }

    @Override
    public long getStart() {
        return mStart * 2;
    }

    @Override
    public long getEnd() {
        return mEnd * 2;
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public long getStartOffset() {
        throw new IllegalArgumentException("not implemented");
    }

    @Override
    public float getProgress(long time) {
        throw new IllegalArgumentException("not implemented");
    }

    @Override
    public boolean contains(long value) {
        return value >= getStart() && value <= getEnd();
    }
}
