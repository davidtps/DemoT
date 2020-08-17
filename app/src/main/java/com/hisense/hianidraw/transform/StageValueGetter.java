package com.hisense.hianidraw.transform;

import com.hisense.hianidraw.draw.DrawStage;
import com.hisense.hianidraw.draw.ValueGetterByTime;

/**
 * Create by xuchongxiang at 2019-05-07 2:57 PM
 */

public class StageValueGetter extends DefaultValueGetter {
    private DrawStage mDrawStage;

    public final DrawStage getDrawStage() {
        return mDrawStage;
    }

    public StageValueGetter(String key, String value, DrawStage drawStage) {
        super(key, value);
        mDrawStage = drawStage;
    }

    @Override
    public int compareTo(ValueGetterByTime other) {
        if (other instanceof StageValueGetter) {
            return (int) (mDrawStage.getStart() - ((StageValueGetter) other).getDrawStage().getStart());
        } else {
            return 0;
        }
    }
}
