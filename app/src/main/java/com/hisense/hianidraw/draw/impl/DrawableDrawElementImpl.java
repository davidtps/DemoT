package com.hisense.hianidraw.draw.impl;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import com.hisense.hianidraw.draw.DrawData;
import com.hisense.hianidraw.draw.DrawDataPropertyConstant;
import com.hisense.hianidraw.draw.DrawEntry;
import com.hisense.storemode.utils.LogUtils;

/**
 * Create by xuchongxiang at 2019-05-07 10:37 AM
 */

public class DrawableDrawElementImpl extends DrawElementImpl {

    private Drawable mContent;
    private float[] mRect;

    public final Drawable getContent() {
        return mContent;
    }

    public final void setContent(Drawable content) {
        mContent = content;
    }

    public final float[] getRect() {
        return mRect;
    }

    public final void setRect(float[] rect) {
        mRect = rect;
    }

    public DrawableDrawElementImpl(DrawData drawData) {
        super(drawData);
        mRect = new float[4];
    }

    @Override
    public void drawContent(Canvas canvas, Paint localPaint, long frameTime) {
        float scale = 1.0f;
        float alpha = 1.0f;
        float rotation = 0.0f;

        DrawEntry drawValueScale = mDrawData.getDrawValue(DrawDataPropertyConstant.SCALE, frameTime);
        if (drawValueScale != null && drawValueScale.getValue() != null) {
            scale = Float.parseFloat(drawValueScale.getValue().toString());
        }

        DrawEntry drawValueAlpha = mDrawData.getDrawValue(DrawDataPropertyConstant.ALPHA, frameTime);
        if (drawValueAlpha != null && drawValueAlpha.getValue() != null) {
            alpha = Float.parseFloat(drawValueAlpha.getValue().toString());
        }

        DrawEntry drawValueRotation = mDrawData.getDrawValue(DrawDataPropertyConstant.ROTATION, frameTime);
        if (drawValueRotation != null && drawValueRotation.getValue() != null) {
            rotation = Float.parseFloat(drawValueRotation.getValue().toString());
        }
        mRect = calculateRect(frameTime);

        DrawEntry drawableName = mDrawData.getDrawValue(DrawDataPropertyConstant.CONTENT, frameTime);
        if (drawableName.getValue() == null) {
            return;
        }

        String value = drawableName.getValue().toString();
        if (value == null) {
            return;
        }

        mContent = getDrawHelper().getDrawableCache().getDrawableBykey(value);
        if (mContent == null) {
            return;
        }

        int w = mContent.getIntrinsicWidth();
        int h = mContent.getIntrinsicHeight();
        if (mRect[0] != mRect[2]) {
            w = (int) (mRect[2] - mRect[0]);
        }
        if (mRect[1] != mRect[3]) {
            h = (int) (mRect[3] - mRect[1]);
        }
        float cx = mRect[0] + w / 2.0f;
        float cy = mRect[1] + h / 2.0f;
        mContent.setAlpha((int) (alpha * 255));
        canvas.save();
        mContent.setBounds((int) mRect[0], (int) mRect[1], (int) mRect[0] + w, h + (int) mRect[1]);
        if (scale != 1F) {
            canvas.scale(scale, scale, cx, cy);
        }
        if (rotation != 0F) {
            LogUtils.i("ROTATION", "$ROTATION");
            canvas.rotate(rotation, cx, cy);
        }
        mContent.draw(canvas);
        canvas.restore();
    }
}
