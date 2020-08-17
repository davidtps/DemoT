package com.hisense.hianidraw.draw.impl;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.hisense.hianidraw.draw.DrawData;
import com.hisense.hianidraw.draw.DrawDataPropertyConstant;
import com.hisense.hianidraw.draw.DrawEntry;
import com.hisense.hianidraw.parser.ShaderParser;

/**
 * Create by xuchongxiang at 2019-05-07 10:37 AM
 */

public class CircleDrawElementImpl extends DrawElementImpl {

    private float[] mRect;
    private Paint mPaint;

    public float[] getRect() {
        return mRect;
    }

    public void setRect(float[] rect) {
        mRect = rect;
    }

    public final Paint getPaint() {
        return mPaint;
    }

    public CircleDrawElementImpl(DrawData drawData) {
        super(drawData);
        mRect = new float[4];
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    public void drawContent(Canvas canvas, Paint localPaint, long frameTime) {

        float[] calculateRect = calculateRect(frameTime);

        if (calculateRect == null) {
            return;
        }

        mRect = calculateRect;
        float radius = (mRect[2] - mRect[0]) / 2;
        float x = radius + mRect[0];
        float y = radius + mRect[1];
        float scale = 1.0f;

        DrawEntry entry = mDrawData.getDrawValue(DrawDataPropertyConstant.SCALE, frameTime);
        if (entry != null && entry.getValue() != null) {
            scale = Float.parseFloat(entry.getValue().toString());
        }

        DrawEntry drawValueColor = mDrawData.getDrawValue(DrawDataPropertyConstant.COLOR, frameTime);
        if (drawValueColor != null) {
            String type = drawValueColor.getType();
            Object value = drawValueColor.getValue();
            if (DrawDataPropertyConstant.COLOR.equals(type)) {
                mPaint.setColor(Color.parseColor((String) value));
            }
        }

        DrawEntry drawValueShader = mDrawData.getDrawValue(DrawDataPropertyConstant.SHADER_STRING, frameTime);
        if (drawValueShader != null && drawValueShader.getValue() != null) {
            String type = drawValueColor.getType();
            Object value = drawValueColor.getValue();
            mPaint.setShader(ShaderParser.parseShader((String) value, mRect[0], mRect[2], 0f, 0f, null));
        }

        canvas.save();
        if (mRect != null) {
            float[] clip = calculateClip(frameTime);
            if (clip != null) {
                canvas.clipRect(clip[0], clip[1], clip[2], clip[3]);
            }
            if (scale != 1F) {
                canvas.scale(scale, scale, x, y);
            }
            canvas.drawCircle(x, y, radius, mPaint);
        }
        canvas.restore();
    }
}
