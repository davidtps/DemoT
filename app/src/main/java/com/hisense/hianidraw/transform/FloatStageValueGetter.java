package com.hisense.hianidraw.transform;

import com.hisense.hianidraw.draw.DrawStage;

/**
 * Create by xuchongxiang at 2019-05-07 4:06 PM
 */

public class FloatStageValueGetter extends AnimationStageValueGetter {

    public FloatStageValueGetter(String key, String value, DrawStage drawStage, String progressValue) {
        super(key, value, drawStage, progressValue);
    }

    @Override
    public Object processValue(String[] valueArray, float[] progressArray, float progress) {
        if (progress >= 1) {
            return Float.parseFloat(valueArray[valueArray.length - 1]);
        }
        if (progress <= 0) {
            return Float.parseFloat(valueArray[0]);
        }

        for (int i = progressArray.length - 1; i >= 0; i--) {
            if (progress > progressArray[i]) {
                float startV = Float.parseFloat(valueArray[i]);
                float endV = Float.parseFloat(valueArray[i + 1]);
                float inProgress = (progress - progressArray[i]) / (progressArray[i + 1] - progressArray[i]);
                return startV + (endV - startV) * inProgress;
            }
        }
        return 0;
    }
}
