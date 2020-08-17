package com.hisense.storemode.utils;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;

import com.hisense.storemode.StoreModeApplication;
import com.hisense.storemode.manager.StoreModeManager;

/**
 * Created by tianpengsheng on 2019年07月26日 14时13分.
 */
public class WindowManagerUtils {

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mMatchLayoutParams;
    private volatile static WindowManagerUtils sInstance;
    private WindowManager.LayoutParams mWrapLayoutParams;

    private WindowManagerUtils() {
    }

    public static WindowManagerUtils getInstance() {
        if (sInstance == null) {
            synchronized (StoreModeManager.class) {
                if (sInstance == null)
                    sInstance = new WindowManagerUtils();
            }
        }
        return sInstance;
    }

    public WindowManager getWindowManager() {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) StoreModeApplication.sContext.getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }


    //full screen
    public WindowManager.LayoutParams getMatchLayoutParams() {
        if (mMatchLayoutParams == null) {

            mMatchLayoutParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    0, PixelFormat.TRANSPARENT);
            mMatchLayoutParams.flags =
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                            | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                            | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
            mMatchLayoutParams.gravity = Gravity.CENTER;

        }
        return mMatchLayoutParams;
    }

    //for tv info
    public WindowManager.LayoutParams getWrapLayoutParams() {
        if (mWrapLayoutParams == null) {

            mWrapLayoutParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    0, PixelFormat.TRANSPARENT);
            mWrapLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
            mWrapLayoutParams.gravity = Gravity.TOP | Gravity.LEFT;

        }
        return mWrapLayoutParams;
    }
}
