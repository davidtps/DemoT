package com.hisense.storemode.utils;

import android.util.Log;

import com.hisense.storemode.ui.dialog.debug.DebugManager;


public class LogUtils {
    public static final String TAG = "StoreMode";
    public static boolean DEBUG = ConstantConfig.LOG_SWITCH;

    public static void d(String msg) {
        if (DEBUG) {
            DebugManager.getInstance().upDateLog(TAG, msg);
            Log.d(TAG, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (DEBUG) {
            DebugManager.getInstance().upDateLog(TAG, msg);
            Log.d(tag, msg);
        }
    }

    public static void e(String msg) {
        if (DEBUG) {
            DebugManager.getInstance().upDateLog(TAG, msg);
            Log.e(TAG, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (DEBUG) {
            DebugManager.getInstance().upDateLog(TAG, msg);
            Log.e(tag, msg);
        }
    }

    public static void e(String tag, String msg, Exception e) {
        if (DEBUG) {
            DebugManager.getInstance().upDateLog(tag, msg, e);
            Log.e(tag, msg, e);
        }
    }

    public static void i(String msg) {
        if (DEBUG) {
            DebugManager.getInstance().upDateLog(TAG, msg);
            Log.i(TAG, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (DEBUG) {
            DebugManager.getInstance().upDateLog(TAG, msg);
            Log.i(tag, msg);
        }
    }

    public static void printLog(String msg) {
        if (DEBUG) {
            printOutToConsole(msg);
        }
    }

    public static void printLog(String msg, Exception e) {
        if (DEBUG) {
            printOutToConsole(msg + Log.getStackTraceString(e));
        }
    }

    public static void printLog(String msg, Throwable e) {
        if (DEBUG) {
            printOutToConsole(msg + Log.getStackTraceString(e));
        }
    }


    public static void printOutToConsole(String msg) {
        int length = msg.length();
        int offset = 4000;
        if (length > offset) {// 解决报文过长，打印不全的问题！
            int n = 0;
            for (int i = 0; i < length; i += offset) {
                n += offset;
                if (n > length)
                    n = length;
                DebugManager.getInstance().upDateLog(TAG + "json:", msg.substring(i, n));
                Log.d(TAG + "json:", msg.substring(i, n));
            }
        } else {
            DebugManager.getInstance().upDateLog(TAG + "json:", msg);
            Log.d(TAG + "json:", msg);
        }
    }
}
