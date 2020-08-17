package com.hisense.storemode.service;

import android.app.Service;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.provider.Settings;

import com.hisense.hianidraw.cache.SeriesUtils;
import com.hisense.storemode.BuildConfig;
import com.hisense.storemode.R;
import com.hisense.storemode.StoreModeApplication;
import com.hisense.storemode.manager.AppManager;
import com.hisense.storemode.manager.ReceiverManager;
import com.hisense.storemode.manager.StoreModeManager;
import com.hisense.storemode.manager.task.BackgroundTaskManager;
import com.hisense.storemode.utils.ConstantConfig;
import com.hisense.storemode.utils.LogUtils;
import com.hisense.storemode.utils.PreferenceUtils;
import com.hisense.storemode.utils.ToastUtil;

import static com.hisense.storemode.receiver.StoreModeReceiver.ACTION_FINISH_STORE_MODE;
import static com.hisense.storemode.receiver.StoreModeReceiver.ACTION_INIT_COUNTDOWN_STORE_MODE;
import static com.hisense.storemode.receiver.StoreModeReceiver.ACTION_START_STORE_MODE;
import static com.hisense.storemode.receiver.StoreModeReceiver.ACTION_START_STORE_MODE_4K;

/**
 * Created by tianpengsheng on 2019年08月09日 11时30分.
 * for start Storemode
 */
public class StoreModeService extends Service {

    private static final String TAG = "StoreModeService";
    private Handler mHandler;
    private HandlerThread mHandlerThread;
    private boolean mIsInitCountDown = false;
    public static ContentObserver mContentObserver;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.d(TAG, "onStartCommand()  isStoreMode: " + ConstantConfig.isStoreMode());
        LogUtils.d(TAG, "onStartCommand()  fte is running?" + !ConstantConfig.isFTEFinish());
        LogUtils.d(TAG, "onStartCommand() receiver action :" + ((intent != null) ? intent.getAction() : "intent is null"));
        if (intent == null) return START_STICKY;

        if (!ConstantConfig.isFTEFinish()) {//FTE is running
            switch (intent.getAction()) {
                case ACTION_START_STORE_MODE_4K: {
                    boolean tmp = intent.getBooleanExtra("storemode_tmp", false);
                    LogUtils.i(TAG, "ACTION_START_STORE_MODE_4K    storemode_tmp:" + tmp);
                    if (tmp) {//true  right now start()
                        if (BuildConfig.CURR_PLATFORM.contains("us")) {//us
                            showToastMessageForFTE(ConstantConfig.MODE_STORE_4K_MODE);
                        } else {//em
                            showToastMessageForFTE(ConstantConfig.MODE_STORE_MODE);
                        }
                        ConstantConfig.setReceiverMonitoreSwitch(1);
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
            }
            return START_STICKY;
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
                StoreModeManager.getInstance().start();
                break;
            }
            case ACTION_FINISH_STORE_MODE: {
                // finish storemode
                LogUtils.d(TAG, "finish_store_mode");
                PreferenceUtils.getInstance().save(ConstantConfig.SHAREPREF_GOOGLE_PROCESS_FLAG, 1);//1 is google process should enable,0 is disable
                SeriesUtils.modifyGoogleServiceSwitch(true);
                ConstantConfig.setUserMode(ConstantConfig.MODE_HOME_MODE);
                ConstantConfig.setReceiverMonitoreSwitch(0);
                BackgroundTaskManager.getInstance().stopWorker();
                ReceiverManager.getInstance().unRegisterReceiver();
                AppManager.getInstance().AppExit(getApplicationContext());
                break;
            }
            case ACTION_START_STORE_MODE: {
                // start storemode
                LogUtils.d(TAG, "start_store_mode");
                ConstantConfig.setReceiverMonitoreSwitch(1);
                showToastMessage(ConstantConfig.MODE_STORE_MODE);
                StoreModeManager.getInstance().start();
                break;
            }
        }

        return START_STICKY;
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

        LogUtils.i(TAG, "observe FTE settings mContentObserver mode:" + mode + "   mContentObserver" + mContentObserver);
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
                        StoreModeManager.getInstance().start();
                    }
                }
            };
        }
        if (ConstantConfig.MODE_HOME_MODE.equals(mode)) {
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

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
