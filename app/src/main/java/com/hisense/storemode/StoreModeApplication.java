package com.hisense.storemode;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.bumptech.glide.Glide;
import com.hisense.hianidraw.cache.SeriesUtils;
import com.hisense.storemode.manager.AppManager;
import com.hisense.storemode.manager.StoreModeManager;
import com.hisense.storemode.manager.task.BackgroundTaskManager;
import com.hisense.storemode.utils.ConstantConfig;
import com.hisense.storemode.utils.CrashHandler;
import com.hisense.storemode.utils.LogUtils;
import com.hisense.storemode.utils.PreferenceUtils;
import com.hisense.storemode.utils.ScreenUtils;


/**
 * Generated application for tinker life cycle
 */
public class StoreModeApplication extends Application {

    private static final String TAG = "StoreModeApplication";
    public static StoreModeApplication sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        ScreenUtils.calculateScreenSize();
        CrashHandler.getInstance().init();
        //bugly 回收 todo---when release  need delete
//        CrashReport.initCrashReport(this, "597d044629", false);//true--- debug mode

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                AppManager.getInstance().addActivity(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                AppManager.getInstance().finishActivity(activity);
            }

        });
        boolean isStoreMode = ConstantConfig.isStoreMode();
        LogUtils.e(TAG, " store mode application started  ConstantConfig.isStoreModeTop():"
                + ConstantConfig.isStoreModeTop() + "   ConstantConfig.isStoreMode():" + isStoreMode);
        LogUtils.e(TAG, "epos update status:" + ConstantConfig.EPOS_UPDATE);
        if (ConstantConfig.EPOS_UPDATE && !ConstantConfig.isStoreModeTop() && isStoreMode) {//for silence install apk
            PreferenceUtils.getInstance().save(ConstantConfig.SHAREPREF_EPOS_UPDATE, false);
            PreferenceUtils.getInstance().save(ConstantConfig.RESET_PALY_POLICY, true);
            ConstantConfig.setReceiverMonitoreSwitch(1);
            StoreModeManager.getInstance().start();
        }

        if (!isStoreMode) {//home mode
            int flag = (int) PreferenceUtils.getInstance().get(ConstantConfig.SHAREPREF_GOOGLE_PROCESS_FLAG, 0);
            boolean googleProcesEnable = SeriesUtils.isGoogleProcessEnable();
            LogUtils.d(TAG, "sharepref_google_process_flag:" + flag + "   googleProcesEnable:" + googleProcesEnable);
            if (flag == 1 && !googleProcesEnable) {//1 is google process should enable, 0 is should disable
                SeriesUtils.modifyGoogleServiceSwitch(true);
                PreferenceUtils.getInstance().delete(ConstantConfig.SHAREPREF_GOOGLE_PROCESS_FLAG);
            }

            if (googleProcesEnable) {
                PreferenceUtils.getInstance().delete(ConstantConfig.SHAREPREF_GOOGLE_PROCESS_FLAG);
            }

        }


        Handler delay = new Handler(Looper.getMainLooper());
        delay.postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean isStoreMode = ConstantConfig.isStoreMode();

                LogUtils.d(TAG, " delay Handler started ConstantConfig.IS_INIT_COUNTDOWN_STOREMODE:"
                        + ConstantConfig.IS_INIT_COUNTDOWN_STOREMODE + "  isStoreMode:" + isStoreMode);
                if (!isStoreMode && !ConstantConfig.IS_INIT_COUNTDOWN_STOREMODE) {
                    ConstantConfig.IS_INIT_COUNTDOWN_STOREMODE = true;
                    LogUtils.d(TAG, "not store mode  exit application");
                    SeriesUtils.modifyGoogleServiceSwitch(true);
//                    ConstantConfig.setReceiverMonitoreSwitch(0);//no need repeat setting
                    BackgroundTaskManager.getInstance().stopWorker();
                    System.exit(0);
                }
            }
        }, 2000);

//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            return;
//        }
//        LeakCanary.install(this);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        LogUtils.d(TAG, "onTrimMemory  level:" + level);
        Glide.get(sContext).trimMemory(level);
    }
}