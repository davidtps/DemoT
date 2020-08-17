package com.hisense.hianidraw.transform;

import com.hisense.hianidraw.draw.DrawEntry;
import com.hisense.hianidraw.draw.ValueGetterByTime;

import java.util.Collections;
import java.util.List;

/**
 * Create by xuchongxiang at 2019-05-07 3:19 PM
 */

public class StageGroupValueGetter implements ValueGetterByTime {

    private List<StageValueGetter> mValueGetters;
    private String mKey;

    public final List<StageValueGetter> getMStageGetters() {
        return mValueGetters;
    }

    public final String getKey() {
        return mKey;
    }

    public StageGroupValueGetter(String key, List<StageValueGetter> stageGetters) {
        super();
        mKey = key;
        Collections.sort(stageGetters);
        mValueGetters = stageGetters;
    }

    @Override
    public DrawEntry getValueByTime(long time) {
        StageValueGetter getter = getTargetStageGetter(time);
        return getter != null ? getter.getValueByTime(time) : null;
    }

    public StageValueGetter getTargetStageGetter(long time) {
        if (mValueGetters.size() == 1) {
            return mValueGetters.get(0);
        }
        for (int i = mValueGetters.size() - 1; i >= 0; i--) {
            if (i == mValueGetters.size() - 1) {
                if (time >= mValueGetters.get(i).getDrawStage().getStart()) {
                    return mValueGetters.get(i);
                }
            } else if (i == 0) {
                return mValueGetters.get(i);
            } else {
                long start = mValueGetters.get(i).getDrawStage().getStart();
                long end = mValueGetters.get(i + 1).getDrawStage().getStart();
                if (time >= start && time <= end) {
                    return mValueGetters.get(i);
                }
            }
        }
        return null;
    }

    @Override
    public int compareTo(ValueGetterByTime o) {
        return 0;
    }
}
