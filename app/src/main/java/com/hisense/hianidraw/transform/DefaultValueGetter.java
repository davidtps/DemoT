package com.hisense.hianidraw.transform;

import com.hisense.hianidraw.draw.DrawEntry;
import com.hisense.hianidraw.draw.ValueGetterByTime;

/**
 * Create by xuchongxiang at 2019-05-07 2:58 PM
 */

public class DefaultValueGetter implements ValueGetterByTime {

    private String mKey;
    private String mValue;

    public final String getKey() {
        return mKey;
    }

    public final String getValue() {
        return mValue;
    }

    public DefaultValueGetter(String key, String value) {
        super();
        mKey = key;
        mValue = value;
    }

    @Override
    public DrawEntry getValueByTime(long time) {
        return new DrawEntry(mKey, mValue);
    }

    @Override
    public int compareTo(ValueGetterByTime o) {
        return 0;
    }
}
