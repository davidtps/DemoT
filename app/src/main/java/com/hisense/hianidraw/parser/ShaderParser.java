package com.hisense.hianidraw.parser;

import android.graphics.LinearGradient;
import android.graphics.Shader;

import com.hisense.hianidraw.draw.DrawDataPropertyConstant;
import com.hisense.hianidraw.util.StringUtil;

/**
 * Create by xuchongxiang at 2019-05-06 8:31 PM
 */

public class ShaderParser {

    public static Shader parseShader(String value, float sX, float eX, float sY, float eY) {
        return parseShader(value, sX, eX, sY, eY, null);
    }

    public static Shader parseShader(String value, float sX, float eX, float sY, float eY, float[] positions) {
        if (StringUtil.isValid(value)) {

            if (value.startsWith(DrawDataPropertyConstant.SHADER.SHADER_LINEAR_GRADIENT)) {
                String inValue = value.replace(DrawDataPropertyConstant.SHADER.SHADER_LINEAR_GRADIENT, "");
                String[] shaderString = StringUtil.parseArray(inValue);

                int[] colors = new int[shaderString.length];

                for (int i = 0; i < shaderString.length; i++) {
                    colors[i] = ColorParser.parseColor(shaderString[i]);
                }
                float[] pos = positions != null ? positions : generateDefault(colors);
                return new LinearGradient(sX, sY, eX, eY, colors, pos, Shader.TileMode.CLAMP);
            }
        }
        throw new IllegalArgumentException("Color value:" + value + " is wrong");
    }

    public static float[] generateDefault(int[] array) {
        float[] divideArray = new float[array.length];
        for (int i = 0; i < array.length; ++i) {
            divideArray[i] = 0.0F + (float) i * (1.0F / (float) (array.length - 1));
        }
        return divideArray;
    }

}
