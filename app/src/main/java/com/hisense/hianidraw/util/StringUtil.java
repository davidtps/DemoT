package com.hisense.hianidraw.util;

import java.util.List;

/**
 * Create by xuchongxiang at 2019-05-06 7:16 PM
 */

public class StringUtil {

    public static boolean isValid(String value) {
        return value != null && !value.isEmpty();
    }

    public static String[] parseArray(String value) {
        String content = value.replace("(", "");
        content = content.replace(")", "");
        return content.split(",");
    }
}
