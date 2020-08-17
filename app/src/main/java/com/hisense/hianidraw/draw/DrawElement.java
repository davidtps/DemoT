package com.hisense.hianidraw.draw;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.List;

/**
 * Create by xuchongxiang at 2019-05-07 10:42 AM
 */

public interface DrawElement extends Comparable<DrawElement> {

    void onDraw(Canvas canvas, long frameTime, Paint paint);

    void onStageChanged(DrawStage pre, DrawStage cur);

    void fit(long time);

    List<DrawStage> getStages();

    String getName();

    void injectDrawHelper(DrawHelper helper);

    DrawHelper getDrawHelper();
}
