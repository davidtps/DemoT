package com.hisense.storemode.ui.dialog.debug;

import android.app.FragmentManager;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hisense.hianidraw.cache.SeriesUtils;
import com.hisense.storemode.R;
import com.hisense.storemode.StoreModeApplication;
import com.hisense.storemode.manager.StoreModeManager;
import com.hisense.storemode.utils.ConstantConfig;
import com.hisense.storemode.utils.DateUtil;
import com.hisense.storemode.utils.LogUtils;
import com.hisense.storemode.utils.PreferenceUtils;
import com.hisense.storemode.utils.ScreenUtils;
import com.hisense.storemode.utils.StringUtil;
import com.hisense.storemode.utils.ToastUtil;


/**
 * Create by xuchongxiang at 2019-05-08 7:19 PM
 */

public class DebugManager implements DebugClickListener {

    private static final String TAG = "DebugManager";
    private static final int SET_LOG_TEXT = 1000;
    private static final int SCROLL_DOWN = 1001;
    private volatile static DebugManager sInstance;
    private DebugFragment mDebugFragment;
    private View mLogView;
    private TextView mTvLogContent;
    private ScrollView mSvContent;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SET_LOG_TEXT:
                    if (mTvLogContent != null) {
                        String text = (String) msg.obj;
                        mTvLogContent.setText(text);
                    }
                    break;
                case SCROLL_DOWN:
                    if (mSvContent != null) {
                        mSvContent.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                    break;
            }
        }
    };

    private DebugManager() {

    }

    public void showDebugFragment(FragmentManager manager) {
        if (manager == null) {
            LogUtils.d(TAG, "showDebugFragment() FragmentManager is null");
            return;
        }
        DebugFragment preFragment = (DebugFragment) manager.findFragmentByTag(DebugFragment.TAG);
        if (preFragment != null && preFragment.isVisible()) {
            LogUtils.d("DebugManager", "DebugManager is show");
            ToastUtil.showToast("DebugManager is show");
            return;
        }
        if (mDebugFragment == null) {
            mDebugFragment = new DebugFragment();
        }
        mDebugFragment.setDebugClickListener(this);
        mDebugFragment.show(manager, DebugFragment.TAG);
    }

    @Override
    public void onShowLog(boolean isShowLeft) {
        if (mDebugFragment != null) {
            openLog(isShowLeft);
        }
    }

    @Override
    public void onCloseLog() {
        WindowManager wm = (WindowManager) StoreModeApplication.sContext
                .getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        try {
            if (mLogView != null) {
                wm.removeViewImmediate(mLogView);
                mLogView = null;
                mTvLogContent = null;
            }
        } catch (Exception e) {
            LogUtils.d("DebugManager", e.toString());
            ToastUtil.showToast("You need press OpenLog button");
        }
    }

    @Override
    public void onResetAqPq() {
        SeriesUtils.resetAQPQ();
    }

    @Override
    public void onPlayNext() {
        StoreModeManager.getInstance().start();
    }

    @Override
    public void onChangeSignalOrVideoTime(String signalTime, String videoTime) {
        try {
            if (StringUtil.isNotEmpty(signalTime)) {
                int st = Integer.parseInt(signalTime);
                PreferenceUtils.getInstance().save(ConstantConfig.SHAREPREF_SIGNAL_PALY_TIME, st);
            }
            if (StringUtil.isNotEmpty(videoTime)) {
                int vt = Integer.parseInt(videoTime);
//                PreferenceUtils.getInstance().save(ConstantConfig.SHAREPREF_SIGNAL_PALY_TIME, vt);
                PreferenceUtils.getInstance().save(ConstantConfig.SHAREPREF_EACH_PIC_SHOW_TIME, vt);
            }
            StoreModeManager.getInstance().start();
        } catch (NumberFormatException e) {
            ToastUtil.showToast("please input number");
        }
    }

    @Override
    public void onChangeEposPosition(String location) {
        if (StringUtil.isEmpty(location)) {
            return;
        }
        try {
            int flag = Integer.parseInt(location);
            if (flag >= 0 && flag <= 3) {
                ConstantConfig.EPOS_LAYOUT_POSITION = flag;
            } else {
                ToastUtil.showToast("please input 0 1 2 3");
            }
        } catch (NumberFormatException e) {
            ToastUtil.showToast("please input normal location");
        }
    }

    @Override
    public void onCloseDebugFragment() {
        if (mDebugFragment != null) {
            mDebugFragment.dismiss();
            mDebugFragment = null;
        }
    }

    private void openLog(boolean isShowLeft) {
        ConstantConfig.LOG_SWITCH = true;
        WindowManager wm = (WindowManager) StoreModeApplication.sContext
                .getApplicationContext().getSystemService(Context.WINDOW_SERVICE);

        try {
            if (mLogView != null) {
                wm.removeViewImmediate(mLogView);
                mLogView = null;
            }
        } catch (Exception e) {

        }

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);
        params.width = 800;
        params.height = 600;

        if (isShowLeft) {
            params.x = 0;
            params.y = 0;
        } else {
            params.x = ScreenUtils.mWidth - 800;
            params.y = 0;
        }
        params.gravity = Gravity.LEFT | Gravity.TOP;
        if (mLogView == null) {
            mLogView = LayoutInflater.from(StoreModeApplication.sContext
                    .getApplicationContext()).inflate(R.layout.item_debug_log, null);
        }
        mTvLogContent = mLogView.findViewById(R.id.tv_log_content);
        mSvContent = mLogView.findViewById(R.id.sv_content);
        mTvLogContent.setText("");
        mTvLogContent.setMovementMethod(ScrollingMovementMethod.getInstance());
        try {
            wm.addView(mLogView, params);
        } catch (Exception e) {
            ToastUtil.showToast("You need press CloseLog button");
        }
    }

    public void upDateLog(String tag, String msg) {
        upDateLog(tag, msg, null);
    }

    public void upDateLog(String tag, String msg, Exception e) {

        if (mTvLogContent == null) {
            return;
        }

        String time = DateUtil.getDateTimeFromMillis(System.currentTimeMillis());
        String text = mTvLogContent.getText().toString();
        String errorMsg = e != null ? e.toString() : "";
        String logText;
        if (text.length() >= 4000) {
            text = "";
        }
        if (StringUtil.isEmpty(text)) {
            logText = tag + " " + time + ": " + msg + " " + errorMsg + "\n";
        } else {
            logText = text + tag + " " + time + ": " + msg + " " + errorMsg + "\n";
        }

        if (Looper.myLooper() == Looper.getMainLooper()) {
            mTvLogContent.setText(logText);
            if (mSvContent != null) {
                mSvContent.fullScroll(ScrollView.FOCUS_DOWN);
            }
        } else {
            Message message = Message.obtain();
            message.what = SET_LOG_TEXT;
            message.obj = logText;
            mHandler.sendMessage(message);
            mHandler.sendEmptyMessage(SCROLL_DOWN);
        }

        /*if (mSvContent != null) {
            mSvContent.fullScroll(ScrollView.FOCUS_DOWN);
        }*/
    }

    public static DebugManager getInstance() {
        if (sInstance == null) {
            synchronized (DebugManager.class) {
                if (sInstance == null)
                    sInstance = new DebugManager();
            }
        }
        return sInstance;
    }
}
