package com.hisense.storemode.manager;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.hisense.storemode.utils.LogUtils;

import java.util.Stack;

/**
 * 应用程序Activity管理类：用于Activity管理和应用程序退出
 */
public class AppManager {
    private volatile static AppManager sInstance;
    private Stack<Activity> mActivityStack;

    public static AppManager getInstance() {
        if (sInstance == null) {
            synchronized (AppManager.class) {
                if (sInstance == null)
                    sInstance = new AppManager();
            }
        }
        return sInstance;
    }


    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (mActivityStack == null) {
            mActivityStack = new Stack<>();
        }
        mActivityStack.add(activity);
    }


    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        if (mActivityStack == null) {
            return null;
        }
        if (mActivityStack.size() != 0) {
            return mActivityStack.lastElement();
        }
        return null;
    }

    /**
     * 是否存在指定的activity
     */
    public boolean isActivityExists(Class cla) {
        if (mActivityStack == null || mActivityStack.size() == 0) {
            return false;
        }
        for (Activity activity : mActivityStack) {
            if (activity.getClass().getName().equals(cla.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取上一个activity
     */
    public Activity getPreviousActivity() {
        if (mActivityStack == null) {
            return null;
        }
        if (mActivityStack.size() > 1) {
            return mActivityStack.get(mActivityStack.size() - 2);
        }
        return null;
    }

    /**
     * 关闭activity的数量
     *
     * @param count
     */
    public void finishActivitys(int count) {
        for (int i = 0; i < count; i++) {
            Activity activity = mActivityStack.pop();
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = mActivityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            mActivityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }


    /**
     * 特定activity的数量
     *
     * @param cls
     * @return
     */
    public int activityCount(Class<?> cls) {
        int temp = 0;
        for (Activity activity : mActivityStack) {
            if (activity.getClass().equals(cls)) {
                temp++;
            }
        }
        return temp;
    }

    public void test() {
        for (int i = 0; i < mActivityStack.size(); i++) {
            LogUtils.d("actvity stack:" + mActivityStack.get(i).getClass());
        }
    }

    /**
     * 特定需求，删除指定页面及上面的页面
     *
     * @param cls
     */
    public void delActivity(Class<?> cls) {
        for (int i = 0; i < mActivityStack.size(); i++) {
            if (mActivityStack.get(i).getClass().equals(cls)) {
                mActivityStack.get(i).finish();
                mActivityStack.remove(i);
                mActivityStack.get(i).finish();
                mActivityStack.remove(i);
                break;
            }
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : mActivityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = mActivityStack.size(); i < size; i++) {
            if (null != mActivityStack.get(i)) {
                mActivityStack.get(i).finish();
            }
        }
        mActivityStack.clear();
    }

    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context
                    .ACTIVITY_SERVICE);
            activityMgr.restartPackage(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
        }
    }

    public boolean isAppExit(Context context, Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        ComponentName componentName = intent.resolveActivity(context.getPackageManager());
        return componentName != null;
    }
}