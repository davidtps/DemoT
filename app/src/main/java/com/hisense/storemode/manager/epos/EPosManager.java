package com.hisense.storemode.manager.epos;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hisense.hianidraw.AniDrawable;
import com.hisense.hianidraw.cache.SeriesUtils;
import com.hisense.storemode.StoreModeApplication;
import com.hisense.storemode.manager.AppManager;
import com.hisense.storemode.ui.activity.MainActivity;
import com.hisense.storemode.ui.dialog.malllogo.MallLogoAdapter;
import com.hisense.storemode.utils.ConstantConfig;
import com.hisense.storemode.utils.GlideUtils;
import com.hisense.storemode.utils.LogUtils;
import com.hisense.storemode.utils.PreferenceUtils;
import com.hisense.storemode.utils.ScreenUtils;
import com.hisense.storemode.utils.StringUtil;

import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by tianpengsheng on 2019年04月18日 16时47分.
 * to control the UI showing of EPos and MallLogo
 */
public class EPosManager {
    private static final String TAG = "EPosManager";
    private static final String LOGO_TAG = "showLogo";
    private static final String EPOS_TAG = "showEpos";
    public static final int MALLLOGO_TOP_AND_RIGHT_INTERVAL = 20;
//    private static final int EPOS_MIN_WIDTH = 175;

    private static final int EPOS_PENDDING_RIGHT = 410;

    private volatile static EPosManager sInstance;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLogoLayoutParams;
    private WindowManager.LayoutParams mEposLayoutParams;
    private TextView mEposView;
    private CountDownTimer mEopsTimer;
    private CountDownTimer mLogoTimer;
    private ImageView mMallLogo;
    private AniDrawable mAniDrawable;
    private LogoShowRunnable mLogoShowRunnable;
    private Handler mMainHandler;
    //    private ObjectAnimator mAlpha;

    private int mLogoShowTime = 70;//show  60s
    private int mLogoHideTime = 10;//hide  10s
    private int mCurrentLogoIndex = -1;

    private EPosManager() {
    }

    public static EPosManager getInstance() {
        if (sInstance == null) {
            synchronized (EPosManager.class) {
                if (sInstance == null)
                    sInstance = new EPosManager();
            }
        }
        return sInstance;
    }

    public void stopRunningTimer() {
        LogUtils.d(TAG, "stopRunningTimer()");
        if (mLogoTimer != null) {
            mLogoTimer.cancel();
        }
        if (mEopsTimer != null) {
            mEopsTimer.cancel();
        }
    }

    public void countDownTime() {
        removeEposAndLogo();
        if (!ConstantConfig.isStoreMode()) return;
        boolean isEposCheck = (boolean) PreferenceUtils.getInstance().get(ConstantConfig.SHAREPREF_EPOS_CHECK, true);
        if (!isEposCheck) {//show epos switch is close
            return;
        }
//        removeEpos();
        if (mLogoTimer != null) {
            mLogoTimer.cancel();
        }
        if (mEopsTimer != null) {
            mEopsTimer.cancel();
            mEopsTimer.start();
        } else {
            mEopsTimer = new CountDownTimer(ConstantConfig.EPOS_MALLLOGO_COUNTDOWN_TIME * 1000, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    LogUtils.d(TAG, "time down:" + (millisUntilFinished / 1000) + " thread id:" + Thread.currentThread().getId());
                }

                @Override
                public void onFinish() {
                    LogUtils.d(TAG, "time down finish to showEposAndLogo(): thread id:" + Thread.currentThread().getId());
                    if (!ConstantConfig.isStoreMode()) {
                        cancel();
                        return;
                    }
                    showEposAndLogo();
                    countDownTimeShowLogo();
//                    showEpos();
                }
            }.start();
        }

    }

    public void setEposPosition(int position) {
        LogUtils.d(TAG, "setEposPosition POSITION = " + position);
        ConstantConfig.EPOS_LAYOUT_POSITION = position;
        PreferenceUtils.getInstance().save(ConstantConfig.SHAREPREF_EPOS_POSITION, position);
        LogUtils.d(TAG, "setEposPosition ConstantConfig.EPOS_LAYOUT_POSITION = " + ConstantConfig.EPOS_LAYOUT_POSITION);
        mAniDrawable = null;
        countDownTime();

    }

    public void setLogoPosition(int position) {
        LogUtils.d(TAG, "setLogoPosition POSITION = " + position);
        ConstantConfig.MALL_LOGO_POSITION = position;
        PreferenceUtils.getInstance().save(ConstantConfig.SHAREPREF_MALL_LOGO_POSITION, position);
        LogUtils.d(TAG, "setLogoPosition ConstantConfig.MALL_LOGO_POSITION = " + ConstantConfig.MALL_LOGO_POSITION);
        mAniDrawable = null;
        countDownTime();

    }

    public void setAniDrawable(AniDrawable aniDrawable) {
        if (aniDrawable == null && mAniDrawable != null) {
            mAniDrawable.stop();
            LogUtils.d(TAG, "mAniDrawable  resource release.");
        }
        mAniDrawable = aniDrawable;
    }

    public void countDownTimeShowLogo() {
        if (!ConstantConfig.isStoreMode()) return;
        boolean isEposCheck = (boolean) PreferenceUtils.getInstance().get(ConstantConfig.SHAREPREF_EPOS_CHECK, true);
        if (!isEposCheck) {//show epos switch is close
            return;
        }
        if (mLogoTimer != null) {
            mLogoTimer.cancel();
            mLogoTimer.start();
        } else {
            mLogoTimer = new CountDownTimer(mLogoShowTime * 1000, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
//                    LogUtils.d(TAG, "countDownTimeLogo()  time down:" + (millisUntilFinished / 1000) + " thread id:" + Thread.currentThread().getId());
                }

                @Override
                public void onFinish() {
                    LogUtils.d(TAG, "countDownTimeLogo()   time down finish to showLogo(): thread id:" + Thread.currentThread().getId());
                    if (mMallLogo != null && mMallLogo.isAttachedToWindow()) {
                        removeLogo();
                        startDelayShowLogo();
                    }
                    if (!ConstantConfig.isStoreMode()) {
                        cancel();
                    }
                }
            }.start();
        }

    }

    public void showEpos() {
        if (preShowDetect(EPOS_TAG)) return;
        mAniDrawable = SeriesUtils.getAniDrawable(mAniDrawable);

        if (mEposView == null) {
            mEposView = new TextView(StoreModeApplication.sContext);
            mEposView.setWidth(SeriesUtils.EPOS_MIN_WIDTH);
            mEposView.setHeight(ScreenUtils.mHeight);
        }
        LogUtils.d(TAG, "showEpos ConstantConfig.EPOS_LAYOUT_POSITION = " + ConstantConfig.EPOS_LAYOUT_POSITION);
        switch (ConstantConfig.EPOS_LAYOUT_POSITION) {

            case 0://left
                LogUtils.d(TAG, "case 0");
                getEposWindowLayoutParams().width = SeriesUtils.EPOS_MIN_WIDTH;
                getEposWindowLayoutParams().x = 0;
                getEposWindowLayoutParams().y = 0;
                mEposView.setWidth(SeriesUtils.EPOS_MIN_WIDTH);
                mEposView.setHeight(ScreenUtils.mHeight);
                break;
            case 1://top
                LogUtils.d(TAG, "case 1");

                getEposWindowLayoutParams().width = ScreenUtils.mWidth;
                getEposWindowLayoutParams().x = 0;
                getEposWindowLayoutParams().y = 0;
                mEposView.setWidth(ScreenUtils.mWidth);
                mEposView.setHeight(SeriesUtils.EPOS_MIN_HEIGHT);
                break;
            case 2://right
                LogUtils.d(TAG, "case 2");
                getEposWindowLayoutParams().width = SeriesUtils.EPOS_MIN_WIDTH;
                getEposWindowLayoutParams().x = ScreenUtils.mWidth - SeriesUtils.EPOS_MIN_WIDTH - SeriesUtils.EPOS_LEFT_MARGIN;
                getEposWindowLayoutParams().y = 0;
                mEposView.setWidth(SeriesUtils.EPOS_MIN_WIDTH);
                mEposView.setHeight(ScreenUtils.mHeight);
                LogUtils.d(TAG, " case 2  EPos  positon:  x=" + getEposWindowLayoutParams().x + "  y=" + getEposWindowLayoutParams().y
                        + ",ScreenUtils.mWidth = " + ScreenUtils.mWidth + ", mEposView.getWidth() = " + mEposView.getWidth());
                break;
            case 3://bottom
                LogUtils.d(TAG, "case 3");
                getEposWindowLayoutParams().y = ScreenUtils.mHeight - SeriesUtils.EPOS_MIN_HEIGHT;
                getEposWindowLayoutParams().width = ScreenUtils.mWidth;
                getEposWindowLayoutParams().x = 0;
                mEposView.setWidth(ScreenUtils.mWidth);
                mEposView.setHeight(SeriesUtils.EPOS_MIN_HEIGHT);
                LogUtils.d(TAG, " case 3  EPos  positon:  x=" + getEposWindowLayoutParams().x + "  y=" + getEposWindowLayoutParams().y
                        + ",ScreenUtils.mWidth = " + ScreenUtils.mWidth + ", mEposView.getWidth() = " + mEposView.getWidth());
                LogUtils.d(TAG, " case 3  EPos  positon:  mEposView.getHeight()=" + mEposView.getHeight() + "mEposView.getWidth() = " + mEposView.getWidth());
                break;
            default:
                getEposWindowLayoutParams().x = 0;
                getEposWindowLayoutParams().y = 0;
                break;
        }
        LogUtils.d(TAG, "   EPos  positon:  x=" + getEposWindowLayoutParams().x + "  y=" + getEposWindowLayoutParams().y
                + ",ScreenUtils.mHeight = " + ScreenUtils.mHeight + ", mEposView.getHeight() = " + mEposView.getHeight());
        mEposView.setBackground(mAniDrawable);

        removeEpos();
        getWindowManager().addView(mEposView, getEposWindowLayoutParams());
        mAniDrawable.start();
//        getWindowLayoutParams().x = 200;
//        getWindowLayoutParams().y = 200;
//        getWindowManager().updateViewLayout(mEposView, getWindowLayoutParams());

    }

//    private AniDrawable getAniDrawable(AniDrawable mAniDrawable) {
//        LogUtils.d(TAG, "getAniDrawable mAniDrawable= " + mAniDrawable);
//        if (mAniDrawable == null) {
//            Gson gson = new Gson();
//            Root result = null;
//            InputStream inputStream = null;
//            AssetManager assetManager = StoreModeApplication.sContext.getResources().getAssets();
//            try {
//                if (ConstantConfig.EPOS_LAYOUT_POSITION == 0 || ConstantConfig.EPOS_LAYOUT_POSITION == 2) {
//                    inputStream = assetManager.open("epos_vertical.json");
//
//                } else if (ConstantConfig.EPOS_LAYOUT_POSITION == 1 || ConstantConfig.EPOS_LAYOUT_POSITION == 3) {
//                    inputStream = assetManager.open("epos_horizontal.json");
//
//                }
//                result = gson.fromJson(new InputStreamReader(inputStream), Root.class);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            DrawHelper drawHelper = new DrawHelper(result);
//            drawHelper.setDrawableCache(new AndroidTvDrawableCache());
//            drawHelper.getDrawableCache().injectContext(StoreModeApplication.sContext);
//            drawHelper.setDuration(result.getDuration());
//
//            mAniDrawable = new AniDrawable(drawHelper);
//            mAniDrawable.setAnimationTimeCallback(new AniDrawable.AnimationTimeCallback() {
//                @Override
//                public void onAnimationStart() {
//
//                }
//
//                @Override
//                public void onAnimationEnd() {
//                    SeriesUtils.autoTransformEposPosition();
//                }
//            });
//        }
//
//        return mAniDrawable;
//    }


    public void showLogo() {
        if (preShowDetect(LOGO_TAG)) return;
        LogUtils.d(TAG, "showLogo() current thread id:" + Thread.currentThread().getId());
        int logoWidth = ScreenUtils.getDpToPx(190);
        int logoHeight = ScreenUtils.getDpToPx(90);
        if (mMallLogo == null) {
            mMallLogo = new ImageView(StoreModeApplication.sContext);
            mMallLogo.setScaleType(ImageView.ScaleType.FIT_CENTER);
            LinearLayout.LayoutParams logoParams = new LinearLayout.LayoutParams(
                    logoWidth + ScreenUtils.getDpToPx(MALLLOGO_TOP_AND_RIGHT_INTERVAL)
                    , logoHeight);
            mMallLogo.setPadding(0, 0, ScreenUtils.getDpToPx(MALLLOGO_TOP_AND_RIGHT_INTERVAL), 0);
            mMallLogo.setLayoutParams(new LinearLayout.LayoutParams(logoParams));

        }


        String logoPaths = (String) PreferenceUtils.getInstance().get(ConstantConfig.SHAREPREF_SELECTED_MALL_LOGO, "");
        String[] logoPathArrays = logoPaths.split(MallLogoAdapter.SPLIT_FLAG);

        mCurrentLogoIndex++;
        if (mCurrentLogoIndex >= logoPathArrays.length) {
            mCurrentLogoIndex = 0;
        }

        String logoPath = logoPathArrays[mCurrentLogoIndex];


        LogUtils.d(TAG, "showLogo() logo imageview width:" + logoWidth + "  height:" + logoHeight + "  logoPath:" + logoPath);
        if (StringUtil.isNotEmpty(logoPath)) {
            GlideUtils.loadImageForMallLogo(logoPath, mMallLogo, logoWidth, logoHeight);
        } else {
            mMallLogo.setImageDrawable(null);
//            mMallLogo.setImageResource(R.drawable.ic_launcher);
        }

        switch (ConstantConfig.EPOS_LAYOUT_POSITION) {
            case 0://left
                if (ConstantConfig.MALL_LOGO_POSITION == 1) {//top
                    setLogoRightTop(logoWidth);
                } else if (ConstantConfig.MALL_LOGO_POSITION == 3) {//bottom
                    setLogoRightBottom(logoWidth, logoHeight);
                }
                break;
            case 1://top
                if (ConstantConfig.MALL_LOGO_POSITION == 0) {//left
                    setLogoLeftBottom(logoHeight);
                } else if (ConstantConfig.MALL_LOGO_POSITION == 2) {//right
                    setLogoRightBottom(logoWidth, logoHeight);
                }
                break;
            case 2://right
                if (ConstantConfig.MALL_LOGO_POSITION == 1) {//top
                    setLogoLeftTop();
                } else if (ConstantConfig.MALL_LOGO_POSITION == 3) {//bottom
                    setLogoLeftBottom(logoHeight);
                }
                break;
            case 3://bottom
                if (ConstantConfig.MALL_LOGO_POSITION == 0) {//left
                    setLogoLeftTop();
                } else if (ConstantConfig.MALL_LOGO_POSITION == 2) {//right
                    setLogoRightTop(logoWidth);
                }
                break;
        }

        removeLogo();
        LogUtils.d(TAG, "   EPos Logo positon:  x=" + getWindowLayoutParams().x + "  y=" + getWindowLayoutParams().y
                + "  logo image width:" + logoWidth + "   height:" + logoHeight);
        getWindowManager().addView(mMallLogo, getWindowLayoutParams());

    }

    public void setLogoRightTop(int logoWidth) {
        getWindowLayoutParams().x = ScreenUtils.mWidth - logoWidth;
        getWindowLayoutParams().y = ScreenUtils.getDpToPx(MALLLOGO_TOP_AND_RIGHT_INTERVAL);
    }

    public void setLogoRightBottom(int logoWidth, int logoHeight) {
        getWindowLayoutParams().x = ScreenUtils.mWidth - logoWidth;
        getWindowLayoutParams().y = ScreenUtils.mHeight - logoHeight - ScreenUtils.getDpToPx(MALLLOGO_TOP_AND_RIGHT_INTERVAL);
    }

    public void setLogoLeftTop() {
        getWindowLayoutParams().x = ScreenUtils.getDpToPx(MALLLOGO_TOP_AND_RIGHT_INTERVAL);
        getWindowLayoutParams().y = ScreenUtils.getDpToPx(MALLLOGO_TOP_AND_RIGHT_INTERVAL);
    }

    public void setLogoLeftBottom(int logoHeight) {
        getWindowLayoutParams().x = ScreenUtils.getDpToPx(MALLLOGO_TOP_AND_RIGHT_INTERVAL);
        getWindowLayoutParams().y = ScreenUtils.mHeight - logoHeight - ScreenUtils.getDpToPx(MALLLOGO_TOP_AND_RIGHT_INTERVAL);
    }


    public void startDelayShowLogo() {
        LogUtils.d(TAG, "mLogoHideTime");
        if (mMainHandler == null) {
            mMainHandler = new Handler(Looper.getMainLooper());
        }
        if (mLogoShowRunnable == null) {
            mLogoShowRunnable = new LogoShowRunnable();
        }
        LogUtils.d(TAG, "startDelayShowLogo() LogoShowRunnable  mLogoShowRunnable:" + mLogoShowRunnable);
        LogUtils.d(TAG, "startDelayShowLogo()  LogoShowRunnable this:" + this);
        mMainHandler.removeCallbacks(mLogoShowRunnable);
        mMainHandler.postDelayed(mLogoShowRunnable, mLogoHideTime * 1000);
    }

    private class LogoShowRunnable implements Runnable {
        @Override
        public void run() {
            LogUtils.d(TAG, "LogoShowRunnable run(), showLogo().");
            showLogo();
            countDownTimeShowLogo();
        }
    }


    public void removeEpos() {
        if (mEposView != null && mEposView.isAttachedToWindow())
            getWindowManager().removeView(mEposView);
    }


    public void removeLogo() {
        if (mMallLogo != null && mMallLogo.isAttachedToWindow())
            getWindowManager().removeView(mMallLogo);
    }

    public void showEposAndLogo() {
//        if (preShowDetect("showEposAndLogo")) return;
        showEpos();
        showLogo();
    }

    public void removeEposAndLogo() {
        LogUtils.d(TAG, "removeEposAndLogo()");
        removeEpos();
        removeLogo();
    }

    public boolean isEposShowing() {
        if (mEposView != null && mEposView.isAttachedToWindow()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isMallLogoShowing() {
        if (mMallLogo != null && mMallLogo.isAttachedToWindow()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isEposAndLogoShowing() {
        return isEposShowing() && isMallLogoShowing();
    }

    //true: no show epos and logo
    private boolean preShowDetect(String tag) {
        boolean isEposCheck = (boolean) PreferenceUtils.getInstance().get(ConstantConfig.SHAREPREF_EPOS_CHECK, true);
        if (!isEposCheck) {//show epos switch is close
            return true;
        }

        if (!ConstantConfig.isStoreMode()) {
            return true;
        }

        //signal is showing,need show epos and logo
        boolean isLiveTvTop = ConstantConfig.isLiveTvRunningTop();
        boolean isTvScanning = SeriesUtils.isTvScanning();

        LogUtils.d(TAG, "preShowDetect()  isLiveTvTop = " + isLiveTvTop + "   isTvScanning = " + isTvScanning);
        if (isLiveTvTop && !isTvScanning) {
            return false;
        }

        Activity activity = AppManager.getInstance().currentActivity();
        if (!(activity instanceof MainActivity)) {
            countDownTime();
            return true;
        }
        SupportFragment topFragment = (SupportFragment) ((SupportActivity) activity).getTopFragment();

        if (!(topFragment != null && topFragment.isSupportVisible())) {
            countDownTime();
            LogUtils.d(TAG, tag + "()  no fragment is visialbe ,don't show EPos");
            return true;
        }
//
        if (LOGO_TAG.equals(tag)) {//already show,don't change other logo show
            if (mMallLogo != null && mMallLogo.isAttachedToWindow()) {
                return true;
            }
        }
        LogUtils.d(TAG, tag + "()  top fragment [" + topFragment.toString() + "] is visialbe  show EPos");
        return false;
    }

    private WindowManager getWindowManager() {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) StoreModeApplication.sContext.getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }

    private WindowManager.LayoutParams getWindowLayoutParams() {
        if (mLogoLayoutParams == null) {

            mLogoLayoutParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                    0, PixelFormat.TRANSPARENT);
            mLogoLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
            mLogoLayoutParams.gravity = Gravity.TOP | Gravity.LEFT;

        }
        return mLogoLayoutParams;
    }

    private WindowManager.LayoutParams getEposWindowLayoutParams() {
        if (mEposLayoutParams == null) {

            mEposLayoutParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                    0, PixelFormat.TRANSPARENT);
            mEposLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
            mEposLayoutParams.gravity = Gravity.TOP | Gravity.LEFT;

        }
        return mEposLayoutParams;
    }

}
