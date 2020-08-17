package com.hisense.hianidraw.transform;

import com.hisense.hianidraw.draw.DrawDataPropertyConstant;
import com.hisense.hianidraw.draw.DrawEntry;
import com.hisense.hianidraw.draw.DrawStage;
import com.hisense.hianidraw.parser.StateParser;

/**
 * Create by xuchongxiang at 2019-05-07 4:07 PM
 */

public class StateStageValueGetter extends StageValueGetter {

    public StateStageValueGetter(String key, String value, DrawStage drawStage) {
        super(key, value, drawStage);
    }

    @Override
    public DrawEntry getValueByTime(long time) {
        float progress = getDrawStage().getProgress(time);
        DrawDataPropertyConstant.STATE value = StateParser.parseState(getValue());
        DrawDataPropertyConstant.STATE state = value != null ? value : DrawDataPropertyConstant.STATE.DISAPPER;

        if (progress > 1.0f && state == DrawDataPropertyConstant.STATE.OUT || state == DrawDataPropertyConstant.STATE.IN_OUT) {
            return new DrawEntry(getKey(), DrawDataPropertyConstant.STATE.DISAPPER);
        }
        return new DrawEntry(getKey(), state);
    }
}
