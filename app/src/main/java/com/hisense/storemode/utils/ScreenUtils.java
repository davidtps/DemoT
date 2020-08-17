package com.hisense.storemode.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

import com.hisense.storemode.StoreModeApplication;


/**
 * Created by tianpengsheng.
 */
public class ScreenUtils {

    /**
     * APP显示区域宽
     */
    public static int mWidth = 0;

    /**
     * APP显示区域高
     */
    public static int mHeight = 0;//这里获取的高度是包含状态栏的,但是不包括虚拟键盘高度

    /**
     * 屏幕每英寸像素点数量
     */
    public static int mDpi = 0;
    /**
     * 状态栏高度
     */
    public static int mStatusBarHeight;


    public static final void calculateScreenSize() {
        if (mWidth > 0) {
            return;
        }
        WindowManager wm = (WindowManager) StoreModeApplication.sContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metric = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metric);
        mDpi = metric.densityDpi;
        mWidth = metric.widthPixels;
        mHeight = metric.heightPixels;
        LogUtils.d("屏幕宽度：" + mWidth + "  屏幕高度：" + mHeight);
        mStatusBarHeight = getStatusBarHeight();
    }

    public static int getStatusBarHeight() {
        int result = 0;
        int resourceId = StoreModeApplication.sContext.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            result = StoreModeApplication.sContext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 将dp值转换为像素值
     *
     * @param size
     * @return
     */
    public static int getDpToPx(int size) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, StoreModeApplication.sContext
                .getResources().getDisplayMetrics());
    }

    /**
     * 将sp值转换为像素值
     *
     * @param size
     * @return
     */
    public static int getSpToPx(int size) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, size, StoreModeApplication.sContext
                .getResources().getDisplayMetrics());
    }


}
