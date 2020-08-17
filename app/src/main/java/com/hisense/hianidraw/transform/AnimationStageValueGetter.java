package com.hisense.hianidraw.transform;

import com.hisense.hianidraw.draw.DrawEntry;
import com.hisense.hianidraw.draw.DrawStage;
import com.hisense.hianidraw.util.StringUtil;

/**
 * Create by xuchongxiang at 2019-05-07 4:06 PM
 */

public abstract class AnimationStageValueGetter extends StageValueGetter {

    private String[] mValueArray;
    private float[] mProgressArray;
    private String mProgressValue;

    public final String[] getValueArray() {
        return mValueArray;
    }

    public final float[] getProgressArray() {
        return mProgressArray;
    }

    public abstract Object processValue(String[] valueArray, float[] progressArray, float progress);

    public final String getProgressValue() {
        return mProgressValue;
    }

    public AnimationStageValueGetter(String key, String value, DrawStage drawStage, String progressValue) {
        super(key, value, drawStage);
        mProgressValue = progressValue;
        mValueArray = StringUtil.parseArray(value);
        if (StringUtil.isValid(progressValue)) {
            String[] strArr = StringUtil.parseArray(progressValue);
            float[] arr = new float[strArr.length];
            for (int i = 0; i < strArr.length; i++) {
                arr[i] = Float.parseFloat(strArr[i]);
            }
            mProgressArray = arr;
        } else {
            mProgressArray = generateDefault(mValueArray);
        }

    }

    @Override
    public DrawEntry getValueByTime(long time) {
        float progress = Math.max(0f, Math.min(1f, getDrawStage().getProgress(time)));
        Object result = this.processValue(this.mValueArray, this.mProgressArray, progress);
        return new DrawEntry(this.getKey(), result);
    }

    public float[] generateDefault(Object[] array) {
        float[] divideArray = new float[array.length];
        for (int i = 0; i < array.length; i++) {
            divideArray[i] = 0F + i * (1F / (array.length - 1));
        }
        return divideArray;
    }

}
