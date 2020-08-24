package com.hisense.storemode.manager.task;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hisense.hianidraw.cache.SeriesUtils;
import com.hisense.storemode.R;
import com.hisense.storemode.StoreModeApplication;
import com.hisense.storemode.bean.RestrictKeyEvent;
import com.hisense.storemode.manager.StoreModeManager;
import com.hisense.storemode.manager.epos.EPosManager;
import com.hisense.storemode.utils.ConstantConfig;
import com.hisense.storemode.utils.LogUtils;
import com.hisense.storemode.utils.PreferenceUtils;
import com.hisense.storemode.utils.WindowManagerUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

/**
 * Created by zhanglaizhi on 4/25/19.
 */

public class BackgroundTaskManager {

    private static final String TAG = "BackgroundTaskManager";
    private static final int EPOS_COUNTDOWN_TIME_OFFSET = 5;//second

    private PeriodicWorkRequest mPeriodicWorkRequest;

    private Handler mSubThreadHandler;
    private Handler mMainThreadHandler;

    private Runnable mEposRunnable;
    private Runnable mRestartStoreModeRunnable;
    private Runnable mAQPQRunnable;
    private Runnable mAddRestrictKeyRunnable;

//    private CountDownTimer mTimer;

    private volatile static BackgroundTaskManager sInstance;
    public static View mResumeTipView;//mResumeTipView  设置为public static,避免在mResumeTipView还在展示时，因资源紧张，被提前回收导致为null
    public static ProgressBar mResumeProgressBar;

    private CountDownTimer mCountDownTimeResume;

    public static BackgroundTaskManager getInstance() {
        if (sInstance == null) {
            synchronized (BackgroundTaskManager.class) {
                if (sInstance == null)
                    sInstance = new BackgroundTaskManager();
            }
            LogUtils.d(TAG, "testtest() getInstance() sInstance:" + sInstance);
        }
        return sInstance;
    }

    private BackgroundTaskManager() {
        HandlerThread thread = new HandlerThread(TAG);
        thread.start();
        mSubThreadHandler = new Handler(thread.getLooper());
        mMainThreadHandler = new Handler(Looper.getMainLooper());
    }

    public void startWorker() {
        LogUtils.d(TAG, "startWorker()  start Background Task   isStoreMode():" + ConstantConfig.isStoreMode());
        LogUtils.d(TAG, "startWorker() pre== mPeriodicWorkRequest == null:" + (mPeriodicWorkRequest == null) + "  " + mPeriodicWorkRequest);
        ConstantConfig.IS_STORE_MODE_BOOT_COMPLETED = true;

        if (!ConstantConfig.isStoreMode()) {
            return;
        }
        startEPosTask();
        resumeStoreModeTask();
        WorkManager.getInstance().cancelAllWorkByTag(TAG);
        if (mPeriodicWorkRequest == null) {
            mPeriodicWorkRequest = new PeriodicWorkRequest
                    .Builder(StartServiceWorker.class, 15, TimeUnit.MINUTES, 5, TimeUnit.MINUTES)
                    .addTag(TAG)
                    .build();
            LogUtils.d(TAG, " startWorker(), worker id:" + mPeriodicWorkRequest.getId());
            WorkManager.getInstance().enqueue(mPeriodicWorkRequest);
        }
        LogUtils.d(TAG, "startWorker()  end== mPeriodicWorkRequest == null:" + (mPeriodicWorkRequest == null) + "  " + mPeriodicWorkRequest);
    }

    // restart storemode process  once
    public void startOneTimeworkerRestartStoreMode() {
        OneTimeWorkRequest restartStoreModeRequest =
                new OneTimeWorkRequest.Builder(RestartStoreModeWorker.class)
                        .setInitialDelay(40 * 1000, TimeUnit.MILLISECONDS)
                        .addTag("onetime")
                        .build();
        WorkManager.getInstance().enqueue(restartStoreModeRequest);
    }

    public void stopWorker() {
        LogUtils.d(TAG, " stopWorker");
        EPosManager.getInstance().removeEposAndLogo();
        EPosManager.getInstance().stopRunningTimer();

        StartServiceWorker.stopBackGroundService();

        WorkManager.getInstance().cancelAllWorkByTag(TAG);
        LogUtils.d(TAG, "stopWorker()  mPeriodicWorkRequest == null:" + (mPeriodicWorkRequest == null) + "  " + mPeriodicWorkRequest);
        if (mPeriodicWorkRequest != null) {
            //WorkManager.getInstance().cancelAllWorkByTag(mPeriodicWorkRequest.getStringId());
            LogUtils.d(TAG, " stopWorker(), worker id:" + mPeriodicWorkRequest.getId());
            WorkManager.getInstance().cancelWorkById(mPeriodicWorkRequest.getId());
        }
    }

    public void releaseResources() {
        if (mPeriodicWorkRequest != null) {
            LogUtils.d(TAG, " releaseResources(), worker id:" + mPeriodicWorkRequest.getId());
        }
        if (mMainThreadHandler != null) {
            mMainThreadHandler.removeCallbacks(mEposRunnable);
        }
        if (mSubThreadHandler != null) {
            mSubThreadHandler.removeCallbacks(mRestartStoreModeRunnable);
            mSubThreadHandler.removeCallbacks(mAQPQRunnable);
            mSubThreadHandler.removeCallbacks(mAddRestrictKeyRunnable);
        }
//        if (mTimer != null) {
//            mTimer.cancel();
//        }

        mEposRunnable = null;
        mAQPQRunnable = null;
        mAddRestrictKeyRunnable = null;
        mRestartStoreModeRunnable = null;

    }

    public void startEPosTask() {
        LogUtils.d(TAG, "startEPosTask() is invoked.");
        LogUtils.d(TAG, "startEPosTask() mEposRunnable:" + mEposRunnable);
        LogUtils.d(TAG, "startEPosTask() mMainThreadHandler:" + mMainThreadHandler);

        if (mEposRunnable == null) {
            mEposRunnable = new EPosRunnable();
        }
        if (mMainThreadHandler == null) {
            mMainThreadHandler = new Handler(Looper.getMainLooper());
        }
        mMainThreadHandler.removeCallbacks(mEposRunnable);
        mMainThreadHandler.post(mEposRunnable);
    }

    /*
     * startAddRestrictKeyTask
     * */
    public void startAddRestrictKeyTask() {
//        int restrict_key_flag = Settings.Global.getInt(StoreModeApplication.sContext.getContentResolver(),
//                ConstantConfig.RESTRICT_KEY_FLAG, 0);
//        if (restrict_key_flag == 1) {// 1-restrict some key,0-don't restrict any key
//            return;
//        }
        LogUtils.e(TAG, "startAddRestrictKeyTask() is invoked. (1-restrict some key,0-don't restrict any key) ");
        if (mAddRestrictKeyRunnable == null) {
            mAddRestrictKeyRunnable = new AddRestrictKeyRunnable();
        }
        if (mSubThreadHandler != null) {
            mSubThreadHandler.removeCallbacks(mAddRestrictKeyRunnable);
            mSubThreadHandler.postDelayed(mAddRestrictKeyRunnable, ConstantConfig.ADD_RESTRICT_KEY_TIME * 1000);
        }
    }


    /*
     * reset AQ PQ 10min after set it
     * */
    public void adjustAQPQ() {
        LogUtils.d(TAG, "mAQPQRunnable run().  mSubThreadHandler != null >" + (mSubThreadHandler != null));
        if (mAQPQRunnable == null) {
            mAQPQRunnable = new AQPQRunnable();
        }
        if (mSubThreadHandler != null) {
            mSubThreadHandler.removeCallbacks(mAQPQRunnable);
            mSubThreadHandler.postDelayed(mAQPQRunnable, ConstantConfig.AQ_PQ_RESET_TIME * 1000);
        }
    }


    private class EPosRunnable implements Runnable {
        @Override
        public void run() {
            //start epos
            LogUtils.d(TAG, "EPosRunnable run().");
            if (!ConstantConfig.isStoreMode()) {
                EPosManager.getInstance().removeEposAndLogo();
                return;
            }
            boolean isLiveTvTop = ConstantConfig.isLiveTvRunningTop();
            boolean isStoreModeTop = ConstantConfig.isStoreModeTop();
            boolean isScanning = SeriesUtils.isTvScanning();

            if (isScanning) {
                LogUtils.d(TAG, "EPosRunnable run()  .removeEposAndLogo  isScanning:" + isScanning);
                EPosManager.getInstance().removeEposAndLogo();
            }

            if (!isLiveTvTop && !isStoreModeTop) {
                LogUtils.d(TAG, "EPosRunnable run()  .removeEposAndLogo  isLiveTvTop:" + isLiveTvTop + "  isStoreModeTop:" + isStoreModeTop);
                EPosManager.getInstance().removeEposAndLogo();
            }

            if (!EPosManager.getInstance().isEposShowing()) {
                EPosManager.getInstance().countDownTime();
            }
//            mMainThreadHandler.removeCallbacks(mEposRunnable);
//            mMainThreadHandler.postDelayed(mEposRunnable, (ConstantConfig.EPOS_MALLLOGO_COUNTDOWN_TIME + EPOS_COUNTDOWN_TIME_OFFSET) * 1000);
        }
    }

    private class AQPQRunnable implements Runnable {
        @Override
        public void run() {
            //reset AQ PQ
            LogUtils.d(TAG, "AQPQRunnable run(), AQ_PQ reset.");
            SeriesUtils.resetAQPQ();
        }
    }

    private class AddRestrictKeyRunnable implements Runnable {
        @Override
        public void run() {
            if (ConstantConfig.isStoreModeTop()) {
                LogUtils.e(TAG, "AddRestrictKeyRunnable run(), restrict some key.  restrict_flag:1");
                EventBus.getDefault().post(new RestrictKeyEvent(1));
            }
        }
    }

    private class RestartStoreModeRunnable implements Runnable {
        @Override
        public void run() {
            boolean isFactoryMode = ConstantConfig.isFactoryMode();
            boolean isAgingMode = ConstantConfig.isAgingMode();
            boolean isStoreMode = ConstantConfig.isStoreMode();

            LogUtils.d(TAG, "RestartStoreModeRunnable  isFactoryMode():" + isFactoryMode);
            LogUtils.d(TAG, "RestartStoreModeRunnable  isAgingMode():" + isAgingMode);
            LogUtils.d(TAG, "RestartStoreModeRunnable  isStoreMode():" + isStoreMode);

            if (!isStoreMode || isFactoryMode || isAgingMode) {
                return;
            }

            countDownTimeResumeStoreMode();
        }
    }


    private void restartStoreModeOperation() {
//        if (ConstantConfig.IS_MAIN_ACTIVITY_INVISIBLE && ConstantConfig.isMediaCenterRunningTop()) {
//            //com.jamdeo.tv.mediacenter.videoResourceRelease
//            String mediaCenterAction = ConstantConfig.MEDIA_CENTER_PKG_NAME + ".videoResourceRelease";
//            Intent intent = new Intent();
//            intent.setAction(mediaCenterAction);
//            intent.setPackage(ConstantConfig.MEDIA_CENTER_PKG_NAME);
//            StoreModeApplication.sContext.sendBroadcast(intent);
//        }

        //resume storemode
//        if (ConstantConfig.IS_MAIN_ACTIVITY_INVISIBLE) {
//            if (!ConstantConfig.isTvScanning()) {
        ConstantConfig.exitEPG();
        PreferenceUtils.getInstance().save(ConstantConfig.RESET_PALY_POLICY, true);
        StoreModeManager.getInstance().start();
        LogUtils.d(TAG, "RestartStoreModeRunnable run(), storemode is restart when launcher no keyevent input after 3min.");

//            } else {
//                LogUtils.d(TAG, "RestartStoreModeRunnable run(), reset Runnable ,isTvScanning:" + ConstantConfig.isTvScanning());
//                mSubThreadHandler.removeCallbacks(mRestartStoreModeRunnable);
//                mSubThreadHandler.postDelayed(mRestartStoreModeRunnable, ConstantConfig.NO_KEY_INPUT_TIME * 1000);
//            }
//    }

    }

    public void removeResumeDialog() {
        LogUtils.d(TAG, "testtest() removeResumeDialog() outer mResumeTipView:" + mResumeTipView);
        if (mResumeTipView != null && mResumeTipView.isAttachedToWindow()) {
            LogUtils.d(TAG, "testtest() removeResumeDialog() inner mResumeTipView:" + mResumeTipView.toString());
            WindowManagerUtils.getInstance().getWindowManager().removeView(mResumeTipView);
            mResumeTipView = null;
        }
    }

    /*
     * restart storemode no key event
     * */
    public void resumeStoreModeTask() {
        LogUtils.d(TAG, "resumeStoreModeTask()  mSubThreadHandler != null >" + (mSubThreadHandler != null));
        if (mCountDownTimeResume != null) {
            mCountDownTimeResume.cancel();
        }
        if (mRestartStoreModeRunnable == null) {
            mRestartStoreModeRunnable = new RestartStoreModeRunnable();
        }
        if (mSubThreadHandler != null) {
            mSubThreadHandler.removeCallbacks(mRestartStoreModeRunnable);
            mSubThreadHandler.postDelayed(mRestartStoreModeRunnable, ConstantConfig.NO_KEY_INPUT_TIME * 1000);
        }
    }

    public void initResumeLayout() {
        if (mResumeTipView == null || mResumeProgressBar == null) {
            mResumeTipView = LayoutInflater.from(StoreModeApplication.sContext).inflate(R.layout.guide_resume_storemode, null);
            mResumeTipView.setFocusable(true);
            TextView title = mResumeTipView.findViewById(R.id.guide_restart_title);
            title.setSelected(true);
            mResumeProgressBar = mResumeTipView.findViewById(R.id.resume_progressbar);

            mResumeTipView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                        removeResumeDialog();
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    private void countDownTimeResumeStoreMode() {
        if (!ConstantConfig.isStoreModeTop() && ConstantConfig.IS_MAIN_ACTIVITY_INVISIBLE) {
            if (!SeriesUtils.isTvScanning()) {
                initResumeLayout();
                WindowManagerUtils.getInstance().getMatchLayoutParams().x = 0;
                WindowManagerUtils.getInstance().getMatchLayoutParams().y = 0;
                WindowManagerUtils.getInstance().getWindowManager().addView(mResumeTipView, WindowManagerUtils.getInstance().getMatchLayoutParams());
                LogUtils.d(TAG, "testtest() countDownTimeResumeStoreMode()   mResumeTipView:" + mResumeTipView.toString());

                mCountDownTimeResume = new CountDownTimer(15 * 1000, 150) {

                    @Override
                    public void onTick(long millisUntilFinished) {
                        if (mResumeProgressBar != null) {
                            mResumeProgressBar.setProgress((int) (100 - (millisUntilFinished / 150)));
                        }
                    }

                    @Override
                    public void onFinish() {
                        LogUtils.d(TAG, "countDownTimeResumeStoreMode() time onFinish.");
                        removeResumeDialog();
                        restartStoreModeOperation();
                    }
                };
                mCountDownTimeResume.start();

            } else {
                LogUtils.d(TAG, "countDownTimeResumeStoreMode()  mSubThreadHandler != null >" + (mSubThreadHandler != null) + " isTvScanning:" + SeriesUtils.isTvScanning());
                if (mSubThreadHandler != null) {
                    mSubThreadHandler.removeCallbacks(mRestartStoreModeRunnable);
                    mSubThreadHandler.postDelayed(mRestartStoreModeRunnable, ConstantConfig.NO_KEY_INPUT_TIME * 1000);
                }
            }
        }
    }
}
