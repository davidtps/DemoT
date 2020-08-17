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
import com.hisense.storemode.utils.LogUtils;

import androidx.annotation.Nullable;

/**
 * Created by zhanglaizhi on 4/25/19.
 */

public class EmptyService extends Service {
    private static final String TAG = "EmptyService";
    private static final int GRAY_SERVICE_ID = 1002;
    private static final String CHANNEL_ID = "channel_1003";
    private static final String CHANNEL_DESC = "channel_empty_service";
    private static Notification mNotification;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.d(TAG, "onStartCommand()  EmptyService");

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
