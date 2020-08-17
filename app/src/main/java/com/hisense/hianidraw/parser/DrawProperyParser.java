package com.hisense.hianidraw.parser;

import com.hisense.hianidraw.data.DrawItem;
import com.hisense.hianidraw.draw.DrawDataPropertyConstant;
import com.hisense.hianidraw.draw.DrawEntry;
import com.hisense.hianidraw.draw.DrawProperty;

/**
 * Create by xuchongxiang at 2019-05-06 8:31 PM
 */

public class DrawProperyParser {

    public static DrawEntry parseContent(DrawItem drawData, String key, long time) {
        DrawEntry drawEntry;
        if (key.equals(DrawDataPropertyConstant.TYPE)) {
            DrawProperty property = drawData.getDrawPropertys().get(key);
            drawEntry = property != null ? property.getValueGetter().getValueByTime(time) : null;
        } else {
            DrawProperty property = drawData.getDrawPropertys().get(key);
            drawEntry = property != null ? property.getValueGetter().getValueByTime(time) : null;
        }
        return drawEntry;
    }

}
