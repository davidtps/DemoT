package com.hisense.storemode.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.hisense.hianidraw.cache.SeriesUtils;
import com.hisense.storemode.R;
import com.hisense.storemode.StoreModeApplication;
import com.hisense.storemode.bean.UsbMountEvent;
import com.hisense.storemode.bean.UsbUnMountEvent;
import com.hisense.storemode.manager.AppManager;
import com.hisense.storemode.manager.StoreModeManager;
import com.hisense.storemode.service.BackgroundService;
import com.hisense.storemode.service.EmptyService;
import com.hisense.storemode.ui.activity.PicListActivity;
import com.hisense.storemode.utils.AudioUtils;
import com.hisense.storemode.utils.ConstantConfig;
import com.hisense.storemode.utils.LogUtils;
import com.hisense.storemode.utils.PreferenceUtils;
import com.hisense.storemode.utils.ToastUtil;
import com.hisense.storemode.utils.WindowManagerUtils;

import org.greenrobot.eventbus.EventBus;

public class StoreModeSystemReceiver extends BroadcastReceiver {
    private String TAG = "StoreModeSystemReceiver";
    //first trigger , make up usbResPriorityCheck using
    private boolean mFirstStart = true;
    private WindowManager.LayoutParams mTipsLayoutParams;
    private WindowManager mWindowManager;
    private static View mEnergyGuideView;
    private LinearLayout mBootTipsView;
    private int mTipsShowTime = 10;
    private int mEnergyGuidanceShowTime = 5;

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtils.d(TAG, "onReceive()  isStoreMode: " + ConstantConfig.isStoreMode());
        LogUtils.d(TAG, "onReceive() receiver action :" + ((intent != null) ? intent.getAction() : "intent is null"));
        LogUtils.d(TAG, "onReceive() receiver data :" + ((intent != null) ? intent.getData() : "intent is null"));

        //if usb priority check is false,no need refresh play policy  immediately
        boolean usbResPriorityCheck = (Boolean) PreferenceUtils.getInstance().get(ConstantConfig.SHAREPREF_USB_RES_PRIORITY_CHECK, false);
        if (intent == null) return;
        switch (intent.getAction()) {
            case Intent.ACTION_MEDIA_UNMOUNTED: {
//
//                if (!PlayManager.getInstance().isBuildInVideoPlaying() && !ConstantConfig.isTvScanning()) {
//
//                    LogUtils.d(TAG, "onReceive()   action_media_unmounted" + "  usbResPriorityCheck:" + usbResPriorityCheck);
//                    PreferenceUtils.getInstance().save(ConstantConfig.RESET_PALY_POLICY, true);
//
//                    LogUtils.d(TAG, "media unmounted");
//                    if (usbResPriorityCheck || mFirstStart) {
//                        mFirstStart = false;
//                        StoreModeManager.getInstance().start();
//                    }
//                }
                break;
            }
            case Intent.ACTION_MEDIA_MOUNTED: {
                ConstantConfig.IS_UPDATE_OPERATION_ENABLE = true;
                LogUtils.d(TAG, "onReceive() ACTION_MEDIA_CHECKING  action_media_mounted" + "  usbResPriorityCheck:" + usbResPriorityCheck);
                if (intent.getData() == null) {
                    LogUtils.d(TAG, "case ACTION_MEDIA_MOUNTED intent.getData() == null");
                    return;
                }

                String intentData = intent.getData().toString();
                String storage = Environment.getExternalStorageDirectory().toString();
                LogUtils.d(TAG, "storage = " + storage + "  intentData:" + intentData);

                if (intentData.contains(storage)) {
                    LogUtils.d(TAG, "getData contains storage ");
                    return;
                }
                if (AppManager.getInstance().currentActivity() instanceof PicListActivity) {
                    EventBus.getDefault().post(new UsbMountEvent());
                }

                String usbpath = intentData.substring(8, intentData.length());
                LogUtils.d(TAG, "usbpath =" + usbpath);
                LogUtils.d(TAG, "ConstantConfig.currentPlayPath =" + ConstantConfig.currentPlayPath);

                if (ConstantConfig.currentPlayPath != null && ConstantConfig.currentPlayPath.contains(usbpath)) {
                    LogUtils.d(TAG, "currentPlayPath contains usbPath");
                    return;
                }

                LogUtils.d(TAG, "currentPlayPath does not contains usbPath");
                PreferenceUtils.getInstance().save(ConstantConfig.RESET_PALY_POLICY, true);
                LogUtils.d(TAG, "media mounted, reset play policy");
//                if (usbResPriorityCheck || mFirstStart) {
//                    mFirstStart = false;
//                    StoreModeManager.getInstance().start();
//                }
                break;
            }

            case Intent.ACTION_BOOT_COMPLETED: {
                handleBootCompleteEvent();

                break;
            }


            case Intent.ACTION_MEDIA_EJECT: {
                boolean isStoreMode = ConstantConfig.isStoreMode();
                ConstantConfig.EJECT_STORAGE_PATH = ((intent != null) ? intent.getData().toString() : "intent is null");
                LogUtils.d(TAG, "receiver ACTION_MEDIA_EJECT");
                LogUtils.d(TAG, "receiver ACTION_MEDIA_EJECT ConstantConfig.EJECT_STORAGE_PATH:" + ConstantConfig.EJECT_STORAGE_PATH);
                LogUtils.d(TAG, "receiver ACTION_MEDIA_EJECT isUsbResourcePlaying = " + ConstantConfig.isUsbResourcePlaying);

                if (ConstantConfig.isUsbResourcePlaying && isStoreMode) {
                    ConstantConfig.isUsbResourcePlaying = false;
                    ConstantConfig.isUsbRejecting = true;
                    PreferenceUtils.getInstance().save(ConstantConfig.RESET_PALY_POLICY, true);
                    LogUtils.d(TAG, "receiver ACTION_MEDIA_EJECT  StoreModeManager start()");
                    StoreModeManager.getInstance().start();
                }
                if (AppManager.getInstance().currentActivity() instanceof PicListActivity) {
                    EventBus.getDefault().post(new UsbUnMountEvent());
                }
                break;
            }

            default: {
                LogUtils.d(TAG, "receiver default");
                break;
            }

        }
    }

    private void handleBootCompleteEvent() {
        // boot up  start storemode
        LogUtils.d(TAG, "boot_completed ");
        boolean isFactoryMode = ConstantConfig.isFactoryMode();
        boolean isAgingMode = ConstantConfig.isAgingMode();
        boolean isStoreMode = ConstantConfig.isStoreMode();
        boolean isStoreModeTop = ConstantConfig.isStoreModeTop();
        boolean isFTEFinish = ConstantConfig.isFTEFinish();

        LogUtils.d(TAG, "ACTION_BOOT_COMPLETED  isFactoryMode():" + isFactoryMode + "  threadId:" + Thread.currentThread());
        LogUtils.d(TAG, "ACTION_BOOT_COMPLETED  isAgingMode():" + isAgingMode);
        LogUtils.d(TAG, "ACTION_BOOT_COMPLETED  isStoreMode():" + isStoreMode);
        LogUtils.d(TAG, "ACTION_BOOT_COMPLETED  isStoreModeTop():" + isStoreModeTop);
        LogUtils.d(TAG, "ACTION_BOOT_COMPLETED  isFTEFinish():" + isFTEFinish);

        if (!isFactoryMode && !isAgingMode) {
            int mUserMode = ConstantConfig.getUserMode();
            if (mUserMode == 1 || mUserMode == 2) {
                SeriesUtils.setUserMode(mUserMode);
            }
        }


        if (!isStoreMode || isFactoryMode || isAgingMode || isStoreModeTop || !isFTEFinish) {
            return;
        }

        if (isStoreMode) {
            Intent empty = new Intent(StoreModeApplication.sContext, EmptyService.class);
            StoreModeApplication.sContext.startForegroundService(empty);
            Intent background = new Intent(StoreModeApplication.sContext, BackgroundService.class);
            StoreModeApplication.sContext.startForegroundService(background);
        }

        Handler delay = new Handler(Looper.getMainLooper());
        delay.postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean isFactoryMode = ConstantConfig.isFactoryMode();
                boolean isAgingMode = ConstantConfig.isAgingMode();
                boolean isStoreMode = ConstantConfig.isStoreMode();
                boolean isStoreModeTop = ConstantConfig.isStoreModeTop();
                boolean isFTEFinish = ConstantConfig.isFTEFinish();

                LogUtils.d(TAG, "ACTION_BOOT_COMPLETED  isFactoryMode():" + isFactoryMode + "  threadId:" + Thread.currentThread());
                LogUtils.d(TAG, "ACTION_BOOT_COMPLETED  isAgingMode():" + isAgingMode);
                LogUtils.d(TAG, "ACTION_BOOT_COMPLETED  isStoreMode():" + isStoreMode);
                LogUtils.d(TAG, "ACTION_BOOT_COMPLETED  isStoreModeTop():" + isStoreModeTop);
                LogUtils.d(TAG, "ACTION_BOOT_COMPLETED  isFTEFinish():" + isFTEFinish);

                if (!isStoreMode || isFactoryMode || isAgingMode || isStoreModeTop || !isFTEFinish) {
                    return;
                }

                ConstantConfig.setReceiverMonitoreSwitch(1);
                SeriesUtils.resetAQPQ();

                ConstantConfig.exitEPG();
                ToastUtil.showToast(StoreModeApplication.sContext.getString(R.string.enter_storemode));
                ToastUtil.showToast(StoreModeApplication.sContext.getString(R.string.seller_settings_toast));

                //boot completed  start Store mode  and show Tips
                AudioUtils.setStoreModeVolume(true);
                StoreModeManager.getInstance().start();
                ConstantConfig.isAuCountryCode();
                ConstantConfig.isExceedBacklight();


                if (ConstantConfig.isAuCountryCode() && ConstantConfig.isExceedBacklight()) {
                    showEnergyGuideView();
                    delay.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            removeEnergyGuideView();

                            showTipsView();
                            countDownTimeShowTips();
                        }
                    }, mEnergyGuidanceShowTime * 1000);
                } else {
                    showTipsView();
                    countDownTimeShowTips();
                }


            }
        }, SeriesUtils.DELAY_START_STOREMODE);


//                Timer timer = new Timer();
//                timer.schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        boolean isFactoryMode = ConstantConfig.isFactoryMode();
//                        boolean isAgingMode = ConstantConfig.isAgingMode();
//                        boolean isStoreMode = ConstantConfig.isStoreMode();
//                        boolean isStoreModeTop = ConstantConfig.isStoreModeTop();
//                        boolean isFTEFinish = ConstantConfig.isFTEFinish();
//
//
//                        LogUtils.d(TAG, "ACTION_BOOT_COMPLETED  isFactoryMode():" + isFactoryMode);
//                        LogUtils.d(TAG, "ACTION_BOOT_COMPLETED  isAgingMode():" + isAgingMode);
//                        LogUtils.d(TAG, "ACTION_BOOT_COMPLETED  isStoreMode():" + isStoreMode);
//                        LogUtils.d(TAG, "ACTION_BOOT_COMPLETED  isStoreModeTop():" + isStoreModeTop);
//                        LogUtils.d(TAG, "ACTION_BOOT_COMPLETED  isFTEFinish():" + isFTEFinish);
//
//                        if (!isStoreMode || isFactoryMode || isAgingMode || isStoreModeTop || !isFTEFinish) {
//                            return;
//                        }
//
//                        //start mode select page
//                        Intent intent1 = new Intent(StoreModeApplication.sContext, ModeSelectActivity.class);
//                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        StoreModeApplication.sContext.startActivity(intent1);
//                    }
//                }, 3000);

    }

    private void showToastMessage(String mode) {
        ConstantConfig.setUserMode(mode);

    }

    public void countDownTimeShowTips() {
        new CountDownTimer(mTipsShowTime * 1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                LogUtils.d(TAG, "countDownTimeShowTips() time onFinish.");
                removeTipsView();
            }
        }.start();

    }


    public void showTipsView() {
        mBootTipsView = (LinearLayout) LayoutInflater.from(StoreModeApplication.sContext).inflate(R.layout.guide_poweron, null);
        WindowManagerUtils.getInstance().getMatchLayoutParams().x = 0;
        WindowManagerUtils.getInstance().getMatchLayoutParams().y = 0;
        WindowManagerUtils.getInstance().getWindowManager().addView(mBootTipsView, WindowManagerUtils.getInstance().getMatchLayoutParams());

    }

    public void removeTipsView() {
        if (mBootTipsView != null && mBootTipsView.isAttachedToWindow()) {
            WindowManagerUtils.getInstance().getWindowManager().removeView(mBootTipsView);
        }
    }

    /**
     * EnergyGuideView for AU Market.
     */
    public void showEnergyGuideView() {
        LogUtils.d(TAG, "resumeEnergyGuideView() start");
        if (mEnergyGuideView == null) {
            mEnergyGuideView = LayoutInflater.from(StoreModeApplication.sContext).inflate(R.layout.energy_guidance_au, null);
        }
        WindowManagerUtils.getInstance().getMatchLayoutParams().x = 0;
        WindowManagerUtils.getInstance().getMatchLayoutParams().y = 0;
        WindowManagerUtils.getInstance().getWindowManager().addView(mEnergyGuideView, WindowManagerUtils.getInstance().getMatchLayoutParams());
    }

    /**
     * remove EnergyGuideView
     */
    public void removeEnergyGuideView() {
        LogUtils.d(TAG, "removeEnergyGuideView() start");
        if (mEnergyGuideView != null && mEnergyGuideView.isAttachedToWindow()) {
            LogUtils.d(TAG, "removeEnergyGuideView execute");
            WindowManagerUtils.getInstance().getWindowManager().removeView(mEnergyGuideView);
        }
    }
}
