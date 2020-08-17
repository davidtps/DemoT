package com.hisense.hianidraw.draw;

/**
 * Create by xuchongxiang at 2019-05-07 9:47 AM
 */

public class DrawProperty {

    private String mKey;
    private ValueGetterByTime mValueGetter;

    public String getMKey() {
        return mKey;
    }

    public ValueGetterByTime getValueGetter() {
        return mValueGetter;
    }

    public DrawProperty(String key, ValueGetterByTime valueGetter) {
        super();
        mKey = key;
        mValueGetter = valueGetter;
    }

}
