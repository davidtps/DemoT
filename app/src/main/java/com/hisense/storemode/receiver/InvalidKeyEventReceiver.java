package com.hisense.storemode.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.hisense.storemode.R;
import com.hisense.storemode.StoreModeApplication;
import com.hisense.storemode.manager.play.PlayManager;
import com.hisense.storemode.manager.play.player.IPlayer;
import com.hisense.storemode.utils.ConstantConfig;
import com.hisense.storemode.utils.LogUtils;
import com.hisense.storemode.utils.ToastUtil;

/**
 * Created by tianpengsheng on 5/7/19.
 */

public class InvalidKeyEventReceiver extends BroadcastReceiver {

    private static final String TAG = "InvalidKeyEventReceiver";
    public static final String INVALID_ACTION = "com.hisense.ACTION_KEYEVENT_INVALID";

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtils.d(TAG, "onReceive  isFactoryMode:" + ConstantConfig.isFactoryMode());
        if (ConstantConfig.isFactoryMode()) return;
        if (intent == null) return;
        if (INVALID_ACTION.equals(intent.getAction())) {
//            ToastUtil.showToast(StoreModeApplication.sContext.getString(R.string.invalid_button_press_tips));
            ToastUtil.showToastTime(StoreModeApplication.sContext.getString(R.string.invalid_button_press_tips), 1000, new Handler(Looper.getMainLooper()));
        } else if (Intent.ACTION_SHUTDOWN.equals(intent.getAction())) {
            IPlayer iPlayer = PlayManager.getInstance().getCurrentPlayer();
            if (iPlayer != null) {
                LogUtils.d(TAG, "onreceiver() iplayer:" + iPlayer);
                iPlayer.stop();
            }

        }
    }
}
