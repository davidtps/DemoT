package com.hisense.hianidraw.data;

import com.hisense.hianidraw.draw.DrawData;
import com.hisense.hianidraw.draw.DrawDataPropertyConstant;
import com.hisense.hianidraw.draw.DrawElementFactory;
import com.hisense.hianidraw.draw.DrawEntry;
import com.hisense.hianidraw.draw.DrawProperty;
import com.hisense.hianidraw.draw.DrawDataPropertyConstant.STATE;
import com.hisense.hianidraw.draw.DrawStage;
import com.hisense.hianidraw.parser.DrawProperyParser;
import com.hisense.hianidraw.parser.StateParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Create by xuchongxiang at 2019-05-06 8:23 PM
 */

public class DrawItem implements DrawData {

    private DrawDataPropertyConstant.STATE mState = DrawDataPropertyConstant.STATE.DISAPPER;
    private String mName;
    private Map<String, DrawProperty> mDrawPropertys;

    public void setDrawPropertys(Map<String, DrawProperty> drawPropertys) {
        mDrawPropertys = drawPropertys;
    }

    public void setState(STATE state) {
        mState = state;
    }

    public final DrawDataPropertyConstant.STATE getState() {
        return mState;
    }

    public Map<String, DrawProperty> getDrawPropertys() {
        return mDrawPropertys;
    }

    public DrawItem(String name, Map<String, DrawProperty> drawPropertys) {
        mName = name;
        mDrawPropertys = drawPropertys;
    }


    @Override
    public String getName() {
        return mName;
    }

    @Override
    public boolean shouldBeDraw(long time) {
        DrawProperty property = mDrawPropertys.get("state");
        if (property != null) {
            STATE tmpState = StateParser.parseState((String) property.getValueGetter().getValueByTime(time).getValue());

            if (tmpState == null) {
                tmpState = mState == STATE.OUT ? STATE.DISAPPER : mState;
            }
            mState = tmpState;
        }

        return this.mState != STATE.DISAPPER;
    }

    @Override
    public DrawElementFactory.Shape getShape() {
        DrawEntry entry = DrawProperyParser.parseContent(this, DrawDataPropertyConstant.TYPE, 0L);

        if (entry != null) {
            String value = (String) entry.getValue();
            if (value.equals(DrawDataPropertyConstant.SHAPE.SHAPE_RECT)) {
                return DrawElementFactory.Shape.RECT;
            } else if (value.equals(DrawDataPropertyConstant.PIC)) {
                return DrawElementFactory.Shape.PIC;
            } else if (value.equals(DrawDataPropertyConstant.SHAPE.SHAPE_CIRCLE)) {
                return DrawElementFactory.Shape.CIRCLE;
            } else {
                return DrawElementFactory.Shape.RECT;
            }
        } else {
            return DrawElementFactory.Shape.RECT;
        }
    }

    @Override
    public Object getDrawContent() {
        return null;
    }

    @Override
    public void fitTime(long frameTime) {

    }

    @Override
    public DrawEntry getDrawValue(String key, long frameTime) {
        return DrawProperyParser.parseContent(this, key, frameTime);
    }

    @Override
    public int getlayoutOrder() {
        DrawEntry drawEntry = DrawProperyParser.parseContent(this, DrawDataPropertyConstant.LAYOUTORDER, 0L);
        if (drawEntry != null) {
            String value = String.valueOf(drawEntry.getValue());
            return Integer.parseInt(value);
        }
        return 0;
    }

    @Override
    public boolean shouldCreatePaint() {
        return DrawProperyParser.parseContent(this, DrawDataPropertyConstant.NEWPAINT, 0L) != null;
    }

    @Override
    public List<DrawStage> getStages() {
        return new ArrayList<>();
    }

    @Override
    public void onStageChanged(DrawStage pre, DrawStage cur) {

    }
}
