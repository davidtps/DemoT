package com.hisense.hianidraw.draw;


import java.util.List;

/**
 * Create by xuchongxiang at 2019-05-06 8:13 PM
 */

public interface DrawData {

    String getName();

    boolean shouldBeDraw(long Long);

    DrawElementFactory.Shape getShape();

    Object getDrawContent();

    void fitTime(long frameTime);

    DrawEntry getDrawValue(String key, long frameTime);

    int getlayoutOrder();

    boolean shouldCreatePaint();

    List<DrawStage> getStages();

    void onStageChanged(DrawStage pre, DrawStage cur);
}
