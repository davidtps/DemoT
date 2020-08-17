package com.hisense.storemode.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;

import com.hisense.storemode.manager.AppManager;
import com.hisense.storemode.ui.dialog.debug.DebugManager;
import com.hisense.storemode.utils.LogUtils;


/**
 * Created by zhangchengxian on 5/5/19.
 */

public class OpenDebugViewBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "OpenDebugViewBroadcastReceiver";

    private static final int KEY_SEND = 1;
    private static final int TIME_DELAY = 1000;
    private static final int KEYCODE_KEY_0 = 7;
    private static final int KEYCODE_KEY_1 = 8;
    private static final int KEYCODE_KEY_RED = 183;
    private static final int KEYCODE_KEY_GREEN = 184;
    private static final int KEYCODE_KEY_YELLOW = 185;
    private static final int KEYCODE_KEY_BLUE = 186;

    private static final String KEY = "keycode";
    private static final String KEYEVENT_ACTION_TYPE = "actionType";

    private static final int STOREMODE = 1;
    private static final int STOREMODE_4K = 2;

    private static int[] mSecretKeys = {0, 0, 0, 0, 0, 0};
    private DebugHandler mHandler;

    private static Handler sUIHandler = new Handler() {

        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case KEY_SEND:
                    LogUtils.i(TAG, "dispatchMessage");
//                    mKeyStr = "";
                    resetKeys();
                    break;
            }
        }
    };

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: re-format codes

//        LogUtils.d(TAG, "ConstantConfig.getUserMode()==" + ConstantConfig.getUserMode());
//        if (ConstantConfig.getUserMode() != STOREMODE && ConstantConfig.getUserMode() != STOREMODE_4K) {
//            LogUtils.d(TAG, "not storemode can't open debug view");
//            return;
//        }

//        HandlerThread thread = null;
//        if (thread == null) {
//            thread = new HandlerThread(TAG);
//            thread.start();
//            mHandler = new DebugHandler(thread.getLooper());
//        }
        if (intent != null
                && intent.getIntExtra(KEY, -1) != -1
                && intent.getIntExtra(KEYEVENT_ACTION_TYPE, -1) == KeyEvent.ACTION_UP) {
            int key = intent.getIntExtra(KEY, -1);
            if (key == KEYCODE_KEY_RED
                    || key == KEYCODE_KEY_GREEN
                    || key == KEYCODE_KEY_YELLOW
                    || key == KEYCODE_KEY_BLUE
                    || key == KEYCODE_KEY_0) {
                LogUtils.d(TAG, "onReceive() key============>" + key);

                mSecretKeys[0] = mSecretKeys[1];
                mSecretKeys[1] = mSecretKeys[2];
                mSecretKeys[2] = mSecretKeys[3];
                mSecretKeys[3] = mSecretKeys[4];
                mSecretKeys[4] = mSecretKeys[5];
                mSecretKeys[5] = key;
                for (int i = 0; i < mSecretKeys.length; i++) {
                    LogUtils.d(TAG, "mSecretKeys============>" + mSecretKeys.length);
                }
                if (mSecretKeys[0] == KEYCODE_KEY_BLUE
                        && mSecretKeys[1] == KEYCODE_KEY_GREEN
                        && mSecretKeys[2] == KEYCODE_KEY_0
                        && mSecretKeys[3] == KEYCODE_KEY_RED
                        && mSecretKeys[4] == KEYCODE_KEY_YELLOW
                        && mSecretKeys[5] == KEYCODE_KEY_0) {
                    LogUtils.d(TAG, "onReceive()  open Debug view");

                    DebugManager.getInstance().showDebugFragment(AppManager.getInstance().currentActivity().getFragmentManager());

                    resetKeys();
                }

                //make sure the key send is continuity
                if (sUIHandler.hasMessages(KEY_SEND)) {
                    LogUtils.e("storemode", "hasMessages==" + sUIHandler.hasMessages(KEY_SEND));
                    sUIHandler.removeMessages(KEY_SEND);
                } else {
                    LogUtils.e("storemode", "hasMessages==nomessage");
                }
                sUIHandler.sendEmptyMessageDelayed(KEY_SEND, TIME_DELAY);
            } else {
                resetKeys();
            }
        }
    }


    /***
     * reset keys
     * @return
     */
    private static void resetKeys() {
        LogUtils.e("resetKeys()-----isRunning");
        mSecretKeys[0] = 0;
        mSecretKeys[1] = 0;
        mSecretKeys[2] = 0;
        mSecretKeys[3] = 0;
        mSecretKeys[4] = 0;
        mSecretKeys[5] = 0;
    }


    class DebugHandler extends Handler {
//        private static final int KEY_SEND = 1;
//        private static final int TIME_DELAY = 1000;

        public DebugHandler(Looper looper) {
            super(looper);

        }


        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case KEY_SEND:
                    LogUtils.e(TAG, "dispatchMessage");
//                    resetKeys();
                    break;
            }
        }
    }
}
