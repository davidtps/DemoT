package com.hisense.storemode.manager;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;

import com.hisense.storemode.StoreModeApplication;
import com.hisense.storemode.receiver.InvalidKeyEventReceiver;
import com.hisense.storemode.receiver.KeyEventReceiver;
import com.hisense.storemode.receiver.OpenDebugViewBroadcastReceiver;
import com.hisense.storemode.receiver.RestrictButtonBroadcastReceiver;
import com.hisense.storemode.receiver.RunningPackageChangeBroadcastReceiver;
import com.hisense.storemode.utils.ConstantConfig;
import com.hisense.storemode.utils.LogUtils;

/**
 * Created by tianpengsheng on 2020年03月06日 14时36分.
 * all receiver register and unregister manager
 */
public class ReceiverManager {
    private static final String TAG = "ReceiverManager";
    private volatile static ReceiverManager sInstance;

    private RunningPackageChangeBroadcastReceiver mPackageChangeReceiver;
    private OpenDebugViewBroadcastReceiver mOpenDebugiewBroadcastReceiver;
    private BroadcastReceiver mKeyReceiver;
    private InvalidKeyEventReceiver mInvalidKeyEventReceiver;
    private RestrictButtonBroadcastReceiver mRestrictButtonReceiver;


    public static ReceiverManager getInstance() {
        if (sInstance == null) {
            synchronized (ReceiverManager.class) {
                if (sInstance == null) {
                    sInstance = new ReceiverManager();
                }
            }
        }
        return sInstance;
    }

    public void unRegisterReceiver() {
        try {
            unRegisterInvalidKeyEventReceiver();
            unRegisterKeyReceiver();
            unRegisterOpenDebugViewStartBroadCastReceiver();
            unRegisterRestrictButtonReceiver();
        } catch (IllegalArgumentException e) {
            LogUtils.d(TAG, "IllegalArgumentException, receiver not register" + e.getMessage());
        }

    }

    public void registerReceiver() {
        registerKeyReceiver();
        registerInvalidKeyEventReceiver();
        registerOpenDebugViewStartBroadCastReceiver();
        registerRestrictButtonReceiver();
    }

    public void registerRestrictButtonReceiver() {
        LogUtils.e(TAG, "registerRestrictButtonReceiver()");
        if (mRestrictButtonReceiver == null) {
            mRestrictButtonReceiver = new RestrictButtonBroadcastReceiver();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConstantConfig.MONITOR_KEY_ACTION);
        StoreModeApplication.sContext.registerReceiver(mRestrictButtonReceiver, filter,
                ConstantConfig.MONITOR_KEY_PERMISSION, null);
    }

    public void unRegisterRestrictButtonReceiver() {
        LogUtils.e(TAG, "unRegisterRestrictButtonReceiver()  mRestrictButtonReceiver==null:" + (mRestrictButtonReceiver == null));
        if (mRestrictButtonReceiver != null)
            StoreModeApplication.sContext.unregisterReceiver(mRestrictButtonReceiver);
    }


    /**
     * register registerInvalidKeyEventReceiver
     */
    public void registerInvalidKeyEventReceiver() {
        LogUtils.e(TAG, "registerInvalidKeyEventReceiver()");
        if (mInvalidKeyEventReceiver == null) {
            mInvalidKeyEventReceiver = new InvalidKeyEventReceiver();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SHUTDOWN);
        filter.addAction(InvalidKeyEventReceiver.INVALID_ACTION);
        StoreModeApplication.sContext.registerReceiver(mInvalidKeyEventReceiver, filter);
    }


    public void unRegisterInvalidKeyEventReceiver() {
        LogUtils.e(TAG, "unRegisterInvalidKeyEventReceiver()  mInvalidKeyEventReceiver==null:" + (mInvalidKeyEventReceiver == null));
        if (mInvalidKeyEventReceiver != null)
            StoreModeApplication.sContext.unregisterReceiver(mInvalidKeyEventReceiver);
    }


    /**
     * register  KeyEventReceiver
     */
    public void registerKeyReceiver() {
        LogUtils.d(TAG, "registerKeyReceiver");
        if (mKeyReceiver == null) {
            mKeyReceiver = new KeyEventReceiver();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConstantConfig.MONITOR_KEY_ACTION);
        StoreModeApplication.sContext.registerReceiver(mKeyReceiver, filter,
                ConstantConfig.MONITOR_KEY_PERMISSION, null);
    }

    public void unRegisterKeyReceiver() {
        LogUtils.d(TAG, "unRegisterKeyReceiver  mKeyReceiver != null:" + (mKeyReceiver != null));
        if (mKeyReceiver != null)
            StoreModeApplication.sContext.unregisterReceiver(mKeyReceiver);
    }

    /**
     * register mLogOpenViewStartBroadCastReceiver
     */
    public void registerOpenDebugViewStartBroadCastReceiver() {
        LogUtils.e(TAG, "registerLogOpenViewStartBroadCastReceiver");
        if (mOpenDebugiewBroadcastReceiver == null) {
            mOpenDebugiewBroadcastReceiver = new OpenDebugViewBroadcastReceiver();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConstantConfig.MONITOR_KEY_ACTION);
        StoreModeApplication.sContext.registerReceiver(mOpenDebugiewBroadcastReceiver, filter, ConstantConfig.MONITOR_KEY_PERMISSION, null);


    }

    /**
     * unregister LogOpenViewStart broadcast
     */
    public void unRegisterOpenDebugViewStartBroadCastReceiver() {
        LogUtils.e(TAG, "unRegisterLogOpenViewStartBroadCastReceiver  mOpenDebugiewBroadcastReceiver==null:" + (mOpenDebugiewBroadcastReceiver == null));
        if (mOpenDebugiewBroadcastReceiver != null) {
            StoreModeApplication.sContext.unregisterReceiver(mOpenDebugiewBroadcastReceiver);
        }
    }

    public void registerPackageChangeReceiver() {
        LogUtils.d(TAG, "registerPackageChangeReceiver");
        mPackageChangeReceiver = new RunningPackageChangeBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConstantConfig.ACTIVITY_STATE);
        filter.addCategory(ConstantConfig.getRunningPackageCategory());
        StoreModeApplication.sContext.registerReceiver(mPackageChangeReceiver, filter,
                ConstantConfig.ACTIVITY_STATE_PERMISSION, null);
    }

    public void unregisterPackageChangeReceiver() {
        LogUtils.d(TAG, "registerPackageChangeReceiver mPackageChangeReceiver != null:" + (mPackageChangeReceiver != null));
        if (mPackageChangeReceiver != null)
            StoreModeApplication.sContext.unregisterReceiver(mPackageChangeReceiver);
        mPackageChangeReceiver = null;
    }
}
