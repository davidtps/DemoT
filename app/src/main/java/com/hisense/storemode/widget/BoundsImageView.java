package com.hisense.artmode.ui.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * Define image view.
 */
public class BoundsImageView extends ImageView {

    private int mBorderColor, mInnerColor, mShadowColor;

    public BoundsImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    public BoundsImageView(Context context) {
        this(context, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBorderColor == 0 || mInnerColor == 0) {
            return;
        }
        //draw inner border
        Paint subPaint = new Paint();
        subPaint.setColor(mInnerColor);
        subPaint.setAntiAlias(true);
        subPaint.setStrokeWidth(6.0f);
        subPaint.setStyle(Paint.Style.STROKE);

        Rect rect = new Rect();
        canvas.getClipBounds(rect);
        canvas.drawRect(rect, subPaint);

        //draw outer border
        Paint paint = new Paint();
        paint.setColor(mBorderColor);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(4.0f);
        paint.setStyle(Paint.Style.STROKE);

        canvas.getClipBounds(rect);
        canvas.drawRect(rect, paint);
    }

    /**
     * set border color
     */
    public void setBorderColor(int mBorderColor, int mInnerColor, int shadowColor) {
        this.mBorderColor = mBorderColor;
        this.mInnerColor = mInnerColor;
        this.mShadowColor = shadowColor;
        invalidate();
    }

    /**
     * set anmimate magnify value according to item width
     *
     * @param width, the item width.
     * @return animateValue, value to be animated to.
     */
    public static float setAnimateValue(double width, double value) {
        float animateValue = 1.1f;
        if ((value - width) / 2 >= 15) {
            animateValue = 1.05f;
        }
        return animateValue;
    }

}