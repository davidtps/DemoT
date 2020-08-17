package com.hisense.hianidraw.parser;

import com.hisense.hianidraw.draw.DrawDataPropertyConstant;
import com.hisense.storemode.utils.LogUtils;

/**
 * Create by xuchongxiang at 2019-05-06 8:31 PM
 */

public class ClipParser {
    public static float[] parseClip(long time, String value) {
        if (value.startsWith(DrawDataPropertyConstant.RECT_STRING)) {
            String inValue = value.replace(DrawDataPropertyConstant.RECT_STRING + ":", "");
            String[] shaderString = inValue.split("_");

            float[] clips = new float[shaderString.length];

            for (int i = 0; i < shaderString.length; i++) {
                float v = Float.parseFloat(shaderString[i]);
                clips[i] = v;
            }
            return clips;
        }
        return null;
    }
}
