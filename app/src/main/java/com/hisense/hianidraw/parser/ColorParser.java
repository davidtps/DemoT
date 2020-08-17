package com.hisense.hianidraw.parser;

import android.graphics.Color;

import com.hisense.hianidraw.util.StringUtil;

/**
 * Create by xuchongxiang at 2019-05-06 8:31 PM
 */

public class ColorParser {

    public static int parseColor(String value) {
        if (StringUtil.isValid(value)) {
            if (value.startsWith("#")) {
                return Color.parseColor(value);
            }
            if (value.contains("_")) {
                String[] tmp = value.split("_");
                if (tmp != null && tmp.length > 0) {
                    if (tmp.length == 3) {
                        return Color.rgb(Integer.valueOf(tmp[0]), Integer.valueOf(tmp[1]), Integer.valueOf(tmp[2]));
                    } else if (tmp.length == 4) {
                        return Color.argb(255, Integer.valueOf(tmp[0]), Integer.valueOf(tmp[1]), Integer.valueOf(tmp[2]));
                    }
                }
            }
        }
        throw new IllegalArgumentException("Color value:" + value + " is wrong");

    }
}
