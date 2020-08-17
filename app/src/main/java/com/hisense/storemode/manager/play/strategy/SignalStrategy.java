package com.hisense.storemode.manager.play.strategy;

import android.content.Intent;

import com.hisense.storemode.StoreModeApplication;
import com.hisense.storemode.ui.activity.MainActivity;
import com.hisense.storemode.utils.ConstantConfig;
import com.hisense.storemode.utils.LogUtils;

/**
 * Created by tianpengsheng on 2019年04月22日 17时40分.
 */
public class SignalStrategy implements IPlayStrategy {

    private static final String TAG = "SignalStrategy";

    @Override
    public void playNext() {
        LogUtils.i(TAG, "curr thread id:" + Thread.currentThread().getId());
        Intent intent = new Intent();
        intent.setClass(StoreModeApplication.sContext, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // intent.putExtra(ConstantConfig.PLAY_TYPE, ConstantConfig.PLAY_TYPE_SIGNAL);
        ConstantConfig.PLAY_TYPE = ConstantConfig.PLAY_TYPE_SIGNAL;
        ConstantConfig.FILE_PATH = "";
        ConstantConfig.FILE_PATH_RESOURCE_ID = -1;
        StoreModeApplication.sContext.startActivity(intent);

    }


}
