package com.hisense.hianidraw.draw.impl;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;

import com.hisense.hianidraw.draw.DrawData;
import com.hisense.hianidraw.draw.DrawDataPropertyConstant;
import com.hisense.hianidraw.draw.DrawElement;
import com.hisense.hianidraw.draw.DrawEntry;
import com.hisense.hianidraw.draw.DrawHelper;
import com.hisense.hianidraw.draw.DrawStage;
import com.hisense.hianidraw.parser.ClipParser;

import java.util.List;

/**
 * Create by xuchongxiang at 2019-05-06 8:18 PM
 */

public abstract class DrawElementImpl implements DrawElement {

    private DrawStage mCurDrawStage;
    private DrawHelper mDrawHelper;
    protected DrawData mDrawData;

    public DrawElementImpl(DrawData drawData) {
        super();
        mDrawData = drawData;
    }

    public final DrawStage getCurDrawStage() {
        return mCurDrawStage;
    }

    public final void setCurDrawStage(DrawStage curDrawStage) {
        this.mCurDrawStage = curDrawStage;
    }

    @Override
    public void onDraw(Canvas canvas, long frameTime, Paint paint) {

        Paint localPaint;

        if (mDrawData.shouldCreatePaint()) {
            localPaint = new Paint();
        } else {
            localPaint = paint != null ? paint : new Paint();
        }
        if (mDrawData.shouldBeDraw(frameTime)) {
            preDraw(canvas, localPaint);
            drawContent(canvas, localPaint, frameTime);
            postDraw(canvas, localPaint);
        }
    }

    public final void preDraw(Canvas canvas, Paint paint) {

    }

    public abstract void drawContent(Canvas canvas, Paint localPaint, long frameTime);

    public void postDraw(Canvas canvas, Paint paint) {

    }

    @Override
    public int compareTo(DrawElement other) {
        if (other instanceof DrawElementImpl) {
            return ((DrawElementImpl) other).mDrawData.getlayoutOrder() - mDrawData.getlayoutOrder();
        } else {
            throw new IllegalArgumentException("DrawElementImpl NotImplementedError");
        }
    }

    @Override
    public void onStageChanged(DrawStage pre, DrawStage cur) {

    }

    @Override
    public void fit(long time) {
        mDrawData.fitTime(time);
        DrawStage oldStage = mCurDrawStage;

        if (mCurDrawStage == null || !mCurDrawStage.contains(time)) {
            mCurDrawStage = getTargetStage(time);
            if (mCurDrawStage != null) {
                onStageChanged(oldStage, mCurDrawStage);
                mDrawData.onStageChanged(oldStage, mCurDrawStage);
            }
        }
    }

    public DrawStage getTargetStage(long time) {
        return null;
    }

    @Override
    public List<DrawStage> getStages() {
        return mDrawData.getStages();
    }

    @Override
    public String getName() {
        String name = mDrawData.getName();
        return !TextUtils.isEmpty(name) ? name : "";
    }

    @Override
    public void injectDrawHelper(DrawHelper helper) {
        mDrawHelper = helper;
    }

    @Override
    public DrawHelper getDrawHelper() {
        if (mDrawHelper == null) {
            throw new NullPointerException("DrawHelper is null, you must call injectDrawHelper");
        }
        return mDrawHelper;
    }

    public float[] calculateRect(long frameTime) {

        DrawEntry entryX = mDrawData.getDrawValue(DrawDataPropertyConstant.X, frameTime);
        DrawEntry entryY = mDrawData.getDrawValue(DrawDataPropertyConstant.Y, frameTime);
        DrawEntry entryW = mDrawData.getDrawValue(DrawDataPropertyConstant.W, frameTime);
        DrawEntry entryH = mDrawData.getDrawValue(DrawDataPropertyConstant.H, frameTime);

        float x = 0f;
        float y = 0f;
        float w = 0f;
        float h = 0f;

        if (entryX != null && entryX.getValue() != null) {
            x = Float.parseFloat(entryX.getValue().toString());
        }

        if (entryY != null && entryY.getValue() != null) {
            y = Float.parseFloat(entryY.getValue().toString());
        }

        if (entryW != null && entryW.getValue() != null) {
            w = Float.parseFloat(entryW.getValue().toString());
        }

        if (entryH != null && entryH.getValue() != null) {
            h = Float.parseFloat(entryH.getValue().toString());
        }

        return new float[]{x, y, w + x, y + h};
    }

    public float[] calculateClip(long frameTime) {
        DrawEntry clip = mDrawData.getDrawValue(DrawDataPropertyConstant.CLIP, frameTime);

        if (clip != null && clip.getValue() != null) {
            return ClipParser.parseClip(frameTime, clip.getValue().toString());
        }
        return null;
    }
}
