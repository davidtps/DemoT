package com.hisense.storemode.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.Settings;

import com.hisense.hianidraw.cache.SeriesUtils;
import com.hisense.storemode.BuildConfig;
import com.hisense.storemode.R;
import com.hisense.storemode.StoreModeApplication;
import com.hisense.storemode.manager.AppManager;
import com.hisense.storemode.manager.ReceiverManager;
import com.hisense.storemode.manager.StoreModeManager;
import com.hisense.storemode.manager.task.BackgroundTaskManager;
import com.hisense.storemode.service.BackgroundService;
import com.hisense.storemode.service.EmptyService;
import com.hisense.storemode.utils.AudioUtils;
import com.hisense.storemode.utils.ConstantConfig;
import com.hisense.storemode.utils.LogUtils;
import com.hisense.storemode.utils.PreferenceUtils;
import com.hisense.storemode.utils.ToastUtil;

public class StoreModeReceiver extends BroadcastReceiver {
    private String TAG = "StoreModeReceiver";
    public static final String ACTION_START_STORE_MODE_4K = "com.hisense.storemode.start_4k";
    public static final String ACTION_START_STORE_MODE = "com.hisense.storemode.start";
    public static final String ACTION_FINISH_STORE_MODE = "com.hisense.storemode.finish";
    public static final String ACTION_INIT_COUNTDOWN_STORE_MODE = "com.hisense.storemode.init_countdown";
    private Handler mHandler;
    private HandlerThread mHandlerThread;
    private boolean mIsInitCountDown = false;
    public static ContentObserver mContentObserver;
    //first trigger , make up usbResPriorityCheck using


    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtils.d(TAG, "onReceive()  isStoreMode: " + ConstantConfig.isStoreMode());
        LogUtils.d(TAG, "onReceive()  fte is running?" + !ConstantConfig.isFTEFinish());
        LogUtils.d(TAG, "onReceive() receiver action :" + ((intent != null) ? intent.getAction() : "intent is null"));
        if (intent == null) return;
        if (!ConstantConfig.isFTEFinish()) {//FTE is running
            switch (intent.getAction()) {
                case ACTION_START_STORE_MODE_4K: {
                    boolean tmp = intent.getBooleanExtra("storemode_tmp", false);
                    LogUtils.i(TAG, "ACTION_START_STORE_MODE_4K    storemode_tmp:" + tmp + "   platform:" + BuildConfig.CURR_PLATFORM);
                    if (tmp) {//true  right now start()
                        if (BuildConfig.CURR_PLATFORM.contains("us")) {//us have "storemode with video" mode
                            showToastMessageForFTE(ConstantConfig.MODE_STORE_4K_MODE);
                        } else {//em
                            showToastMessageForFTE(ConstantConfig.MODE_STORE_MODE);
                        }
                        ConstantConfig.setReceiverMonitoreSwitch(1);
                        AudioUtils.setStoreModeVolume();
                        StoreModeManager.getInstance().start();

                    } else {
                        observeFTEFinished(ConstantConfig.MODE_STORE_4K_MODE);
                    }
                    break;
                }
                case ACTION_START_STORE_MODE: {
                    observeFTEFinished(ConstantConfig.MODE_STORE_MODE);
                    break;
                }
                case ACTION_FINISH_STORE_MODE: {
                    SeriesUtils.modifyGoogleServiceSwitch(true);
                    observeFTEFinished(ConstantConfig.MODE_HOME_MODE);
                    break;
                }
                default: {
                    break;
                }
            }
            return;
        }

        switch (intent.getAction()) {
            case ACTION_INIT_COUNTDOWN_STORE_MODE: {
                mIsInitCountDown = true;
                ConstantConfig.IS_INIT_COUNTDOWN_STOREMODE = true;
                ToastUtil.showToast(StoreModeApplication.sContext.getString(R.string.init_storemode_countdown));
                break;
            }
            case ACTION_START_STORE_MODE_4K: {
                // start 4kmode
                LogUtils.d(TAG, "start_store_mode_4k");
                ConstantConfig.setReceiverMonitoreSwitch(1);
                showToastMessage(ConstantConfig.MODE_STORE_4K_MODE);
                AudioUtils.setStoreModeVolume();
                StoreModeManager.getInstance().start();
                break;
            }
            case ACTION_FINISH_STORE_MODE: {
                // finish storemode
                LogUtils.d(TAG, "finish_store_mode");
                PreferenceUtils.getInstance().save(ConstantConfig.SHAREPREF_GOOGLE_PROCESS_FLAG, 1);//1 is google process should enable,0 is disable
                SeriesUtils.modifyGoogleServiceSwitch(true);
                ConstantConfig.setUserMode(ConstantConfig.MODE_HOME_MODE);
                AudioUtils.restoreHomeModeVolume();
                ConstantConfig.setReceiverMonitoreSwitch(0);
                BackgroundTaskManager.getInstance().stopWorker();
                ReceiverManager.getInstance().unRegisterReceiver();
                AppManager.getInstance().AppExit(context);
                break;
            }
            case ACTION_START_STORE_MODE: {
                // start storemode
                LogUtils.d(TAG, "start_store_mode");
                ConstantConfig.setReceiverMonitoreSwitch(1);
                showToastMessage(ConstantConfig.MODE_STORE_MODE);
                AudioUtils.setStoreModeVolume();
                StoreModeManager.getInstance().start();
                break;
            }
            default: {
                break;
            }
        }

    }

    private void showToastMessage(String mode) {
        Intent intent = new Intent(StoreModeApplication.sContext, BackgroundService.class);
        StoreModeApplication.sContext.startForegroundService(intent);
        Intent empty = new Intent(StoreModeApplication.sContext, EmptyService.class);
        StoreModeApplication.sContext.startForegroundService(empty);


        ConstantConfig.setUserMode(mode);
        if (mIsInitCountDown) {
            mIsInitCountDown = false;
            ToastUtil.showToast(StoreModeApplication.sContext.getString(R.string.seller_settings_toast));
        } else {
            ConstantConfig.exitEPG();
            ToastUtil.showToast(StoreModeApplication.sContext.getString(R.string.enter_storemode));
            ToastUtil.showToast(StoreModeApplication.sContext.getString(R.string.seller_settings_toast));
        }
    }

    private void showToastMessageForFTE(String mode) {
        Intent intent = new Intent(StoreModeApplication.sContext, BackgroundService.class);
        StoreModeApplication.sContext.startForegroundService(intent);
        Intent empty = new Intent(StoreModeApplication.sContext, EmptyService.class);
        StoreModeApplication.sContext.startForegroundService(empty);


        ConstantConfig.setUserMode(mode);
        if (mIsInitCountDown) {
            mIsInitCountDown = false;
        } else {
            ToastUtil.showToast(StoreModeApplication.sContext.getString(R.string.enter_storemode));
        }
    }

    private void observeFTEFinished(String mode) {
        ConstantConfig.setUserMode(mode);
        if (mHandlerThread == null) {
            mHandlerThread = new HandlerThread(TAG);
            mHandlerThread.start();
        }

        if (mHandler == null)
            mHandler = new Handler(mHandlerThread.getLooper());

        LogUtils.i(TAG, "observe FTE settings mContentObserver mode:" + mode + " mContentObserver:" + mContentObserver);
        if (mContentObserver == null) {
            mContentObserver = new ContentObserver(mHandler) {
                @Override
                public void onChange(boolean selfChange) {
                    super.onChange(selfChange);
                    if (!ConstantConfig.isFTEFinish()) {
                        LogUtils.d(TAG, "startStoreModeForFTE(), onFinish(), FTE is running.");
                    } else {
                        LogUtils.d(TAG, "startStoreModeForFTE(), onChange(), FTE is stop running. start storeMode");
                        showToastMessage(ConstantConfig.MODE_STORE_MODE);
                        ConstantConfig.setReceiverMonitoreSwitch(1);
                        AudioUtils.setStoreModeVolume();
                        StoreModeManager.getInstance().start();
                    }
                }
            };
        }
        if (ConstantConfig.MODE_HOME_MODE.equals(mode)) {
            ConstantConfig.setReceiverMonitoreSwitch(0);
            LogUtils.d(TAG, "unregisterContentObserver  mContentObserver" + mContentObserver);
            StoreModeApplication.sContext.getContentResolver().unregisterContentObserver(mContentObserver);
        } else {
            LogUtils.d(TAG, "registerContentObserver  mContentObserver" + mContentObserver);
            StoreModeApplication.sContext.getContentResolver().registerContentObserver(
                    Settings.Secure.getUriFor("tv_user_setup_complete"), false, mContentObserver);

            Intent empty = new Intent(StoreModeApplication.sContext, EmptyService.class);
            StoreModeApplication.sContext.startForegroundService(empty);
            Intent intent = new Intent(StoreModeApplication.sContext, BackgroundService.class);
            StoreModeApplication.sContext.startForegroundService(intent);
        }

    }


}
