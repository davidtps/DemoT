package com.hisense.hianidraw.draw.impl;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.hisense.hianidraw.draw.DrawData;
import com.hisense.hianidraw.draw.DrawDataPropertyConstant;
import com.hisense.hianidraw.draw.DrawEntry;
import com.hisense.hianidraw.draw.DrawStage;
import com.hisense.hianidraw.parser.ShaderParser;

/**
 * Create by xuchongxiang at 2019-05-07 10:36 AM
 */

public class RectDrawElementImpl extends DrawElementImpl {

    private float[] mRect;
    private final Paint mPaint;

    public final void setRect(float[] rect) {
        mRect = rect;
    }

    public final float[] getRect() {
        return mRect;
    }

    public final Paint getPaint() {
        return mPaint;
    }

    public RectDrawElementImpl(DrawData drawData) {
        super(drawData);
        mRect = new float[4];
        mPaint = new Paint();
    }

    @Override
    public void drawContent(Canvas canvas, Paint localPaint, long frameTime) {

        DrawEntry drawValueColor = mDrawData.getDrawValue(DrawDataPropertyConstant.COLOR, frameTime);
        if (drawValueColor != null && drawValueColor.getValue() != null) {
            if (DrawDataPropertyConstant.COLOR.equals(drawValueColor.getType())) {
                mPaint.setColor(Color.parseColor((String) drawValueColor.getValue()));
            }
        }

        mRect = calculateRect(frameTime);
        DrawEntry drawValueShader = mDrawData.getDrawValue(DrawDataPropertyConstant.SHADER_STRING, frameTime);
        if (drawValueShader != null && drawValueShader.getValue() != null) {
            mPaint.setShader(ShaderParser.parseShader((String) drawValueShader.getValue(), mRect[0], mRect[2], 0f, 0f));
        }

        canvas.save();
        if (mRect != null) {
            float[] clip = calculateClip(frameTime);
            if (clip != null) {
                canvas.clipRect(clip[0], clip[1], clip[2], clip[3]);
            }
            canvas.drawRect(mRect[0], mRect[1], mRect[2], mRect[3], mPaint);
        }
        canvas.restore();
    }

    @Override
    public void onStageChanged(DrawStage pre, DrawStage cur) {
        super.onStageChanged(pre, cur);
    }
}
