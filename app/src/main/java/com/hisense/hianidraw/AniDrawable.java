package com.hisense.hianidraw;

import android.animation.Animator;
import android.animation.TimeAnimator;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.Drawable;

import com.hisense.hianidraw.cache.SeriesUtils;
import com.hisense.hianidraw.draw.DrawElement;
import com.hisense.hianidraw.draw.DrawHelper;
import com.hisense.hianidraw.draw.DrawStage;
import com.hisense.storemode.utils.LogUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Create by xuchongxiang at 2019-05-07 5:20 PM
 */

public class AniDrawable extends Drawable implements Animatable, TimeAnimator.TimeListener {

    private static final String TAG = "AniDrawable";
    private long mCutTime;
    private long mPeriodInTime = SeriesUtils.MPERIODTIME_JSON;
    private long mPeriodTotalTime = mPeriodInTime * 7;
    private long mPeriodTime = mPeriodTotalTime + 30000;
    private long mTime = 1L;
    private int mAlphaIn = 255;
    private Paint mPaint = new Paint();
    private DrawStage mCurrentStage;
    private boolean mLoop = SeriesUtils.EPOS_PLAY_REPEAT;
    private TimeAnimator mAnimator = new TimeAnimator();
    private List<Animatable2.AnimationCallback> mAnimationCallbacks = new ArrayList<>();
    private List<DrawElement> mElements = new ArrayList<>();
    private boolean mStoped;
    private DrawHelper mHelper;
    private AnimationTimeCallback mAnimationTimeCallback;

    public final void setCutTime(long cutTime) {
        mCutTime = cutTime;
    }

    public final long getCutTime() {
        return mCutTime;
    }

    public final long getPeriodInTime() {
        return mPeriodInTime;
    }

    public final long getPeriodTotalTime() {
        return mPeriodTotalTime;
    }

    public final long getPeriodTime() {
        return mPeriodTime;
    }

    public final DrawHelper getHelper() {
        return mHelper;
    }

    public AniDrawable(DrawHelper helper) {
        super();
        mHelper = helper;
        List<DrawElement> e = helper.getElements();
        Collections.sort(e);
        mElements.addAll(e);
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                LogUtils.d(TAG, "epos onAnimationStart ");
                if (mAnimationTimeCallback != null) {
                    mAnimationTimeCallback.onAnimationStart();
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                LogUtils.d(TAG, "epos onAnimationEnd ");
                if (mAnimationTimeCallback != null) {
                    mAnimationTimeCallback.onAnimationEnd();
                }
                if (!mStoped) {
                    LogUtils.d(TAG, "epos animation end ,and restart play  inner");
                    start();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                LogUtils.d(TAG, "epos onAnimationCancel ");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                LogUtils.d(TAG, "epos onAnimationRepeat ");
            }
        });
        mAnimator.setTimeListener(this);
        mAnimator.setDuration(mPeriodTime);
    }

    @Override
    public void onTimeUpdate(TimeAnimator animation, long totalTime, long deltaTime) {
//        LogUtils.d(TAG, "epos onTimeUpdate()  totalTime:" + totalTime + "  deltaTime:" + deltaTime);
        long innerTime = totalTime % this.mPeriodTime;
        if (innerTime > mPeriodTotalTime) {
            innerTime = mPeriodTotalTime;
        }
        if (getCallback() == null) {
            return;
        }
        if (!mLoop && totalTime > mHelper.getDuration()) {
            this.stop();
            return;
        }
        mTime = totalTime == 0L ? 1L : totalTime;
        mTime = innerTime % mHelper.getDuration();

        if (mCutTime + deltaTime > 10) {
            invalidateSelf();
        } else {
            mCutTime = deltaTime;
        }
    }

    @Override
    public void start() {
        mStoped = false;
        mAnimator.start();
        LogUtils.d(TAG, "epos drawable start() ;");
    }

    @Override
    public void stop() {
        mStoped = true;
        mAnimator.cancel();
        LogUtils.d(TAG, "epos drawable stop() ;");
    }

    @Override
    public boolean isRunning() {
        return mAnimator.isRunning();
    }

    @Override
    public void draw(Canvas canvas) {
        long startTime = System.currentTimeMillis();
        if (canvas != null) {
            for (DrawElement item : mElements) {
                item.fit(mTime);
                item.onDraw(canvas, mTime, mPaint);

            }
        }
    }

    @Override
    public void setAlpha(int alpha) {
        mAlphaIn = alpha;
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public interface AnimationTimeCallback {
        void onAnimationStart();

        void onAnimationEnd();
    }

    public void setAnimationTimeCallback(AnimationTimeCallback callback) {
        mAnimationTimeCallback = callback;
    }

}
