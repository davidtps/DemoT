package com.hisense.hianidraw.draw;

/**
 * Create by xuchongxiang at 2019-05-07 10:19 AM
 */

public interface DrawStage {

    long getStart();

    long getEnd();

    String getName();

    long getStartOffset();

    float getProgress(long time);

    boolean contains(long value);
}
