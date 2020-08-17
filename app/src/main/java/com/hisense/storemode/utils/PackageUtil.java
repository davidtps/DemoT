package com.hisense.storemode.utils;

import android.app.ActivityManager;
import android.content.Context;

import com.hisense.storemode.StoreModeApplication;

import java.util.List;

/**
 * Create by xuchongxiang at 2019-05-09 11:12 AM
 */

public class PackageUtil {
    private static final String TAG = "PackageUtil";

    public static String getTopAppPkgName() {
        ActivityManager am = (ActivityManager) StoreModeApplication.sContext.
                getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningTaskInfo> runningTasks = am.getRunningTasks(5);

        if (runningTasks != null && runningTasks.size() > 1) {
            LogUtils.d(TAG, runningTasks.get(0).topActivity.getPackageName());
            String pkgString = runningTasks.get(0).topActivity.getPackageName();
            LogUtils.d(TAG, "getTopAppPkgName(), pkgName= " + pkgString);
            return pkgString;
        }
        return "";
    }
}
