package com.hisense.storemode.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by zhanglaizhi on 5/14/19.
 */

public class ServiceUtil {
    public static boolean isServiceRunning(Context context, String className) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(Integer.MAX_VALUE);
        if (serviceList != null) {
            for (int i = 0; i < serviceList.size(); i++) {
                ActivityManager.RunningServiceInfo serviceInfo = serviceList.get(i);
                String name = serviceInfo.service.getClassName();
                if (name.equals(className)) {
                    return true;
                }
            }
        }
        return false;
    }
}
