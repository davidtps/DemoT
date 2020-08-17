package com.hisense.hianidraw.transform;

import com.hisense.hianidraw.draw.DrawDataPropertyConstant;
import com.hisense.hianidraw.draw.DrawEntry;
import com.hisense.hianidraw.draw.DrawStage;
import com.hisense.hianidraw.parser.StateParser;

/**
 * Create by xuchongxiang at 2019-05-07 4:10 PM
 */

public class StateValueGetter extends StageValueGetter {
    public StateValueGetter(String key, String value, DrawStage drawStage) {
        super(key, value, drawStage);
    }

    @Override
    public DrawEntry getValueByTime(long time) {
        if (time <= getDrawStage().getStart()
                && (StateParser.parseState(getValue()) == DrawDataPropertyConstant.STATE.IN
                || StateParser.parseState(getValue()) == DrawDataPropertyConstant.STATE.IN_OUT)) {
            return new DrawEntry(getKey(), "disapper");
        }
        if (time > getDrawStage().getEnd() && (StateParser.parseState(getValue()) == DrawDataPropertyConstant.STATE.OUT
                || StateParser.parseState(getValue()) == DrawDataPropertyConstant.STATE.IN_OUT)) {
            return new DrawEntry(getKey(), "disapper");
        }
        return super.getValueByTime(time);
    }

}
