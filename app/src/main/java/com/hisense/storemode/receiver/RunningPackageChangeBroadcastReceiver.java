package com.hisense.storemode.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.hisense.storemode.utils.ConstantConfig;
import com.hisense.storemode.utils.LogUtils;

/**
 * @author tianpengsheng
 * create at   6/14/19 1:55 PM
 */
@Deprecated
public class RunningPackageChangeBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "RunningPackageChangeBroadcastReceiver";

    public void onReceive(Context context, Intent intent) {
        LogUtils.i(TAG, "RuuningPackageChangeRecevier============get===== ");
        if (intent != null) {
            Bundle bundle = intent.getExtras();

            String newCurrentAcvitiy = bundle.getString("class");
            LogUtils.i(TAG, "newCurrentAcvitiy = " + newCurrentAcvitiy);
            if (ConstantConfig.LIVE_TV_SETTING_ACTIVITY.equals(newCurrentAcvitiy)) {
//                EPosManager.getInstance().removeEposAndLogo();
//                LogUtils.i(TAG, "removeEposAndLogo");
            }

        }
    }
}
