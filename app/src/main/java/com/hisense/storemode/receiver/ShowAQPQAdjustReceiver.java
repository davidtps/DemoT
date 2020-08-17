package com.hisense.storemode.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;

import com.hisense.storemode.bean.ShowAQPQAdjustEvent;
import com.hisense.storemode.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;


public class ShowAQPQAdjustReceiver extends BroadcastReceiver {
    private static final String TAG = "ShowAQPQAdjustReceiver";

    private static final int KEY_SEND = 1;
    private static final int TIME_DELAY = 1000;

    private static final String KEY = "keycode";
    private static final String KEYEVENT_ACTION_TYPE = "actionType";


    private static int[] mSecretKeys = {-1, -1, -1, -1};

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

        if (intent != null
                && intent.getIntExtra(KEY, -1) != -1
                && intent.getIntExtra(KEYEVENT_ACTION_TYPE, -1) == KeyEvent.ACTION_UP) {
            int key = intent.getIntExtra(KEY, -1);
            if (key == KeyEvent.KEYCODE_DPAD_LEFT || key == KeyEvent.KEYCODE_DPAD_RIGHT
                    || key == KeyEvent.KEYCODE_0) {
                LogUtils.d(TAG, "onReceive() key: " + key);

                mSecretKeys[0] = mSecretKeys[1];
                mSecretKeys[1] = mSecretKeys[2];
                mSecretKeys[2] = mSecretKeys[3];
                mSecretKeys[3] = key;

//                if (mSecretKeys[0] == KeyEvent.KEYCODE_0
//                        && mSecretKeys[1] == KeyEvent.KEYCODE_0
//                        && mSecretKeys[2] == KeyEvent.KEYCODE_0
//                        && mSecretKeys[3] == KeyEvent.KEYCODE_0) {
//                    LogUtils.d(TAG, "onReceive() show aq pq adjust view");
//                    EventBus.getDefault().post(new ShowAQPQAdjustEvent());
//                    resetKeys();
//                }
                if (mSecretKeys[0] == KeyEvent.KEYCODE_DPAD_LEFT
                        && mSecretKeys[1] == KeyEvent.KEYCODE_DPAD_RIGHT
                        && mSecretKeys[2] == KeyEvent.KEYCODE_DPAD_LEFT
                        && mSecretKeys[3] == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    LogUtils.d(TAG, "onReceive() left show aq pq adjust view");
                    EventBus.getDefault().post(new ShowAQPQAdjustEvent());
                    resetKeys();
                }

                //make sure the key send is continuity
                if (sUIHandler.hasMessages(KEY_SEND)) {
                    LogUtils.i(TAG, "hasMessages: " + sUIHandler.hasMessages(KEY_SEND));
                    sUIHandler.removeMessages(KEY_SEND);
                } else {
                    LogUtils.i(TAG, "no message in uiHandler");
                }
                sUIHandler.sendEmptyMessageDelayed(KEY_SEND, TIME_DELAY);
            } else {
                resetKeys();
            }
        }
    }

    private static void resetKeys() {
        LogUtils.i(TAG, "reset all Keys");
        mSecretKeys[0] = -1;
        mSecretKeys[1] = -1;
        mSecretKeys[2] = -1;
        mSecretKeys[3] = -1;
    }
}
