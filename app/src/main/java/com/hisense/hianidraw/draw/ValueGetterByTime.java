package com.hisense.hianidraw.draw;

/**
 * Create by xuchongxiang at 2019-05-07 9:49 AM
 */

public interface ValueGetterByTime extends Comparable<ValueGetterByTime> {
    DrawEntry getValueByTime(long time);
}
