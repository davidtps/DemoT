package com.hisense.storemode.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInstaller;

import com.hisense.storemode.R;
import com.hisense.storemode.StoreModeApplication;
import com.hisense.storemode.manager.StoreModeManager;
import com.hisense.storemode.utils.ConstantConfig;
import com.hisense.storemode.utils.LogUtils;
import com.hisense.storemode.utils.PreferenceUtils;
import com.hisense.storemode.utils.ToastUtil;

/**
 * Created by tianpengsheng on 2019年09月18日 14时23分.
 * dont work
 */
public class InstallResultReceiver extends BroadcastReceiver {
    private static final String TAG = "InstallResultReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtils.e(TAG, "InstallResultReceiver   onReceive  intent:" + intent);
        if (intent != null) {
            final int status = intent.getIntExtra(PackageInstaller.EXTRA_STATUS,
                    PackageInstaller.STATUS_FAILURE);

            LogUtils.e(TAG, "install status:" + status);

            if (status == PackageInstaller.STATUS_SUCCESS) {
                if (ConstantConfig.isStoreModeTop()) {
                    LogUtils.e(TAG, "storemode is already start install status:" + status);
                    return;
                }
                // success
                LogUtils.e(TAG, "install app success");
                PreferenceUtils.getInstance().save(ConstantConfig.RESET_PALY_POLICY, true);
                ConstantConfig.setReceiverMonitoreSwitch(1);
//                showToastMessage();
                StoreModeManager.getInstance().start();
            } else {
                LogUtils.e(TAG, "install app faile:" + intent.getStringExtra(PackageInstaller.EXTRA_STATUS_MESSAGE));
            }
        }
    }

    private void showToastMessage() {
//        ToastUtil.showToast("E-pos update success.");
        ToastUtil.showToast(StoreModeApplication.sContext.getString(R.string.enter_storemode));
        ToastUtil.showToast(StoreModeApplication.sContext.getString(R.string.seller_settings_toast));
    }
}
