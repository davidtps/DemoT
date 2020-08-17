package com.hisense.storemode.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import com.hisense.storemode.StoreModeApplication;
import com.hisense.storemode.manager.StoreModeManager;
import com.hisense.storemode.manager.task.BackgroundTaskManager;
import com.hisense.storemode.utils.ConstantConfig;
import com.hisense.storemode.utils.LogUtils;
import com.hisense.storemode.utils.PackageUtil;
import com.hisense.storemode.utils.PreferenceUtils;

import androidx.annotation.Nullable;

/**
 * Created by zhanglaizhi on 4/25/19.
 */

public class BackgroundService extends Service {
    private static final String TAG = "BackgroundService";
    private static final String STOREMODE_PACKAGE = "com.hisense.storemode";
    private static final int GRAY_SERVICE_ID = 1001;
    private static final String CHANNEL_ID = "channel_1002";
    private static final String CHANNEL_DESC = "channel_background_service";
    private static Notification mNotification;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.d(TAG, "onStartCommand()  StoreMode IS_STORE_MODE_BOOT_COMPLETED:" + ConstantConfig.IS_STORE_MODE_BOOT_COMPLETED);

        if (!STOREMODE_PACKAGE.equals(PackageUtil.getTopAppPkgName())) {
            LogUtils.d(TAG, "StoreMode is died, not restart it,only start task");
            PreferenceUtils.getInstance().save(ConstantConfig.RESET_PALY_POLICY, true);
        }

        boolean isCrashJustNow = (boolean) PreferenceUtils.getInstance().get(ConstantConfig.CRASH_JUST_NOW, false);
        if (isCrashJustNow) {
            LogUtils.d(TAG, "StoreMode is crashed, now in backgroundservice start storemode  isCrashJustNow:" + isCrashJustNow);
            StoreModeManager.getInstance().start();
            PreferenceUtils.getInstance().save(ConstantConfig.CRASH_JUST_NOW, false);
        }

        BackgroundTaskManager.getInstance().startEPosTask();
        if (!(BackgroundTaskManager.mResumeTipView != null && BackgroundTaskManager.mResumeTipView.isAttachedToWindow())) {
            BackgroundTaskManager.getInstance().resumeStoreModeTask();
        }

        Intent innerIntent = new Intent(this, GrayInnerService.class);
        startService(innerIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_DESC, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);

            mNotification = new Notification.Builder(StoreModeApplication.sContext)
                    .setChannelId(CHANNEL_ID)
                    .build();
            startForeground(GRAY_SERVICE_ID, mNotification);
        } else {//18--26
            startForeground(GRAY_SERVICE_ID, new Notification());
        }


        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        LogUtils.d(TAG, "onDestroy()");
        BackgroundTaskManager.getInstance().releaseResources();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    /**
     * 给 API >= 18 的平台上用的灰色保活手段
     */
    public static class GrayInnerService extends Service {

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForeground(GRAY_SERVICE_ID, mNotification);
            } else {
                startForeground(GRAY_SERVICE_ID, new Notification());
            }
            stopForeground(true);
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

    }
}
