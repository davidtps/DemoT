package com.hisense.hianidraw.parser;

import android.text.TextUtils;
import android.widget.Button;

import com.hisense.hianidraw.draw.DrawDataPropertyConstant;

/**
 * Create by xuchongxiang at 2019-05-06 8:31 PM
 */

public class StateParser {

    public static DrawDataPropertyConstant.STATE parseState(String value) {

        if (TextUtils.isEmpty(value)) {
            return null;
        }

        String string = value.toLowerCase();

        if (string.contains("in") && string.contains("out")) {
            return DrawDataPropertyConstant.STATE.IN_OUT;
        } else if (string.contains("in")) {
            return DrawDataPropertyConstant.STATE.IN;
        } else if (string.contains("out")) {
            return DrawDataPropertyConstant.STATE.OUT;
        } else if (string.contains("keep")) {
            return DrawDataPropertyConstant.STATE.KEEP;
        } else if (string.contains("disapper")) {
            return DrawDataPropertyConstant.STATE.DISAPPER;
        }
        return null;
    }
}
