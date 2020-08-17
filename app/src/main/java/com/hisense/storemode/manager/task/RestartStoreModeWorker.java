package com.hisense.storemode.manager.task;

import android.content.Context;

import com.hisense.storemode.utils.LogUtils;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

/**
 * restart storemode process, by StoreModeApplication restart storemode app
 *
 * @author tianpengsheng
 * create at   4/28/20 3:34 PM
 */

public class RestartStoreModeWorker extends Worker {

    private static final String TAG = "RestartStoreModeWorker";

    public RestartStoreModeWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        LogUtils.d(TAG, "RestartStoreModeWorker   doWork start");
//        if (ConstantConfig.EPOS_UPDATE && !ConstantConfig.isStoreModeTop() && ConstantConfig.isStoreMode()) {//for silence install apk
//            PreferenceUtils.getInstance().save(ConstantConfig.SHAREPREF_EPOS_UPDATE, false);
//            PreferenceUtils.getInstance().save(ConstantConfig.RESET_PALY_POLICY, true);
//            ConstantConfig.setReceiverMonitoreSwitch(1);
//            Handler handler = new Handler(Looper.getMainLooper());
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    LogUtils.d(TAG, "ConstantConfig.isStoreModeTop():" + ConstantConfig.isStoreModeTop());
//                    if (!ConstantConfig.isStoreModeTop())
//                        StoreModeManager.getInstance().start();
//                }
//            }, 3000);
//        }
        return Result.success();
    }


}
