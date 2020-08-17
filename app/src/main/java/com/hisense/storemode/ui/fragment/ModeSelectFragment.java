package com.hisense.storemode.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;

import com.hisense.storemode.R;
import com.hisense.storemode.manager.StoreModeManager;
import com.hisense.storemode.manager.task.BackgroundTaskManager;
import com.hisense.storemode.utils.ConstantConfig;
import com.hisense.storemode.utils.LogUtils;
import com.hisense.storemode.utils.PackageUtil;
import com.hisense.storemode.widget.ModeSelectGuidanceStylist;

import java.util.List;

import androidx.leanback.app.GuidedStepFragment;
import androidx.leanback.widget.GuidanceStylist;
import androidx.leanback.widget.GuidedAction;

/**
 * Create by xuchongxiang at 2019-05-09 10:38 AM
 */

public class ModeSelectFragment extends GuidedStepFragment {

    private static final String TAG = "ModeSelectFragment";
    private static final int STORE_MODE = 1000;
    private static final int HOME_MODE = 1001;
    private static final int STORE_MODE_4K = 1002;
    private static final String OUR_PACKAGENAME = "com.hisense.storemode";

    private ModeSelectGuidanceStylist mModeSelectGuidanceStylist;
    private BroadcastReceiver mKeyReceiver;
    private boolean mIsNetFlixOrYouTubePowerOn;
    private boolean mIsNeedListenKeyEvent;
    private CountDownTimer mNormalTimer;
    private CountDownTimer mSpecialTimer;

    @Override
    public void onCreateActions(List<GuidedAction> actions, Bundle savedInstanceState) {
        super.onCreateActions(actions, savedInstanceState);
        IntentFilter filter = new IntentFilter(ConstantConfig.MONITOR_KEY_ACTION);
        mKeyReceiver = new KeyReceiver();
        getActivity().registerReceiver(mKeyReceiver, filter, ConstantConfig.MONITOR_KEY_PERMISSION, null);
        actions.add(new GuidedAction.Builder(getActivity()).title(R.string.user_mode).id(HOME_MODE).build());
        actions.add(new GuidedAction.Builder(getActivity()).title(R.string.store_mode).id(STORE_MODE).build());
        actions.add(new GuidedAction.Builder(getActivity()).title(R.string.store_mode_with_video).id(STORE_MODE_4K).build());
        checkCountTimer();
    }

    @Override
    public GuidanceStylist onCreateGuidanceStylist() {
        mModeSelectGuidanceStylist = new ModeSelectGuidanceStylist();
        return mModeSelectGuidanceStylist;
    }

    @Override
    public GuidanceStylist.Guidance onCreateGuidance(Bundle savedInstanceState) {
        return new GuidanceStylist.Guidance(getString(R.string.use_mode), "100", "", null);
    }

    @Override
    public void onGuidedActionClicked(GuidedAction action) {
        super.onGuidedActionClicked(action);
        if (action.getId() == STORE_MODE) {
            startStoreMode();
        } else if (action.getId() == HOME_MODE) {
            startHomeMode();
        } else if (action.getId() == STORE_MODE_4K) {
            startStoreMode4K();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mIsNetFlixOrYouTubePowerOn) {
            if (mNormalTimer != null) {
                mNormalTimer.cancel();
                mNormalTimer.start();
            }
        }
    }

    private void startStoreMode() {
        ConstantConfig.setReceiverMonitoreSwitch(1);
        ConstantConfig.setUserMode(ConstantConfig.MODE_STORE_MODE);
        StoreModeManager.getInstance().start();
        getActivity().finish();
    }

    private void startStoreMode4K() {
        ConstantConfig.setReceiverMonitoreSwitch(1);
        ConstantConfig.setUserMode(ConstantConfig.MODE_STORE_4K_MODE);
        StoreModeManager.getInstance().start();
        getActivity().finish();
    }


    private void startHomeMode() {
        // set to user mode
        ConstantConfig.setReceiverMonitoreSwitch(0);
        ConstantConfig.setUserMode(ConstantConfig.MODE_HOME_MODE);
        cancelNormalTimer();
        cancelSpecialTimer();
        getActivity().finish();
        BackgroundTaskManager.getInstance().stopWorker();
    }

    private void cancelNormalTimer() {
        if (mNormalTimer != null) {
            mNormalTimer.cancel();
        }
    }

    private void cancelSpecialTimer() {
        if (mSpecialTimer != null) {
            mSpecialTimer.cancel();
        }
    }

    private void checkCountTimer() {
        if (mNormalTimer == null) {
            mNormalTimer = new CountDownTimer(60 * 1000, 600) {
                @Override
                public void onTick(long millisUntilFinished) {
                    String packName = PackageUtil.getTopAppPkgName();
                    if (!packName.equals(OUR_PACKAGENAME) && (packName.equals(ConstantConfig.NETFLIX_PKG_NAME)
                            || packName.equals(ConstantConfig.YOUTUBE_PKG_NAME))) {
                        mIsNetFlixOrYouTubePowerOn = true;
                        mIsNeedListenKeyEvent = true;
                        LogUtils.d(TAG, "checkCountTimer()    mNormalTimer: netfix or youtube package is top" + (millisUntilFinished / 1800));
                        cancel();
                        if (mSpecialTimer != null) {
                            mSpecialTimer.cancel();
                            mSpecialTimer.start();
                        }
                    }
                    LogUtils.d(TAG, "checkCountTimer()    mNormalTimer:" + (millisUntilFinished / 600));
                    mModeSelectGuidanceStylist.setPbProgress((int) (100 - (millisUntilFinished / 600)));
                }

                @Override
                public void onFinish() {
                    mIsNetFlixOrYouTubePowerOn = false;
                    mIsNeedListenKeyEvent = false;
                    startStoreMode();
                }
            };
        }
        if (mSpecialTimer == null) {
            mSpecialTimer = new CountDownTimer(60 * 1000 * 3, 1800) {
                @Override
                public void onTick(long millisUntilFinished) {
                    LogUtils.d(TAG, "checkCountTimer()    mSpecialTimer:" + (millisUntilFinished / 1800));
                    mModeSelectGuidanceStylist.setPbProgress((int) (100 - (millisUntilFinished / 1800)));
                }

                @Override
                public void onFinish() {
                    mIsNetFlixOrYouTubePowerOn = false;
                    mIsNeedListenKeyEvent = false;
                    startStoreMode();
                }
            };
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mKeyReceiver);

        cancelNormalTimer();
        cancelSpecialTimer();
    }

    public class KeyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null) {
                return;
            }
            int keycode = intent.getIntExtra("keycode", -1);
            LogUtils.d(TAG, "KeyReceiver,onReceive()   keycode:" + keycode);
            if (keycode != -1 && keycode != KeyEvent.KEYCODE_BACK && keycode != KeyEvent.KEYCODE_DPAD_CENTER
                    && keycode != KeyEvent.KEYCODE_DPAD_UP && keycode != KeyEvent.KEYCODE_DPAD_DOWN) {
                if (mIsNetFlixOrYouTubePowerOn && mSpecialTimer != null) {
                    mSpecialTimer.cancel();
                    mSpecialTimer.start();
                }
            }
        }
    }
}
