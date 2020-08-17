package com.hisense.storemode.manager.task;

import android.content.Context;
import android.content.Intent;

import com.hisense.storemode.StoreModeApplication;
import com.hisense.storemode.service.BackgroundService;
import com.hisense.storemode.service.EmptyService;
import com.hisense.storemode.utils.ConstantConfig;
import com.hisense.storemode.utils.LogUtils;
import com.hisense.storemode.utils.ServiceUtil;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

/**
 * Created by zhanglaizhi on 4/25/19.
 */

public class StartServiceWorker extends Worker {

    private static final String TAG = "StartServiceWorker";
    private static Intent background = new Intent(StoreModeApplication.sContext, BackgroundService.class);
    private static Intent empty = new Intent(StoreModeApplication.sContext, EmptyService.class);

    public StartServiceWorker(@androidx.annotation.NonNull Context context, @androidx.annotation.NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Worker.Result doWork() {
        LogUtils.e(TAG, "StartServiceWorker  doWork()!  is_store_mode_boot_completed :" + ConstantConfig.IS_STORE_MODE_BOOT_COMPLETED);
//        if (!ConstantConfig.IS_STORE_MODE_BOOT_COMPLETED) {//be sure powerOn (boot receiver)run ModeSelectActivity
//            return Result.FAILURE;
//        }
        if (!ServiceUtil.isServiceRunning(StoreModeApplication.sContext, "com.hisense.storemode.service.BackgroundService")) {
            //start BackgroundService
            StoreModeApplication.sContext.startForegroundService(background);
            LogUtils.e(TAG, "BackgroundService is not running, restart it!");
        }
        return Result.success();
    }


    public static void stopBackGroundService() {
        StoreModeApplication.sContext.stopService(background);
        StoreModeApplication.sContext.stopService(empty);

    }

}
