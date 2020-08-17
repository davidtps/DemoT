package com.hisense.hianidraw.draw;

/**
 * Create by xuchongxiang at 2019-05-07 9:51 AM
 */

public class DrawEntry {

    private String mType;
    private Object mValue;

    public final String getType() {
        return mType;
    }

    public final Object getValue() {
        return mValue;
    }

    public DrawEntry(String type, Object value) {
        super();
        mType = type;
        mValue = value;
    }
}
