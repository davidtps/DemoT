package com.hisense.storemode.ui.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.Settings;

import com.bumptech.glide.Glide;
import com.hisense.hianidraw.cache.SeriesUtils;
import com.hisense.storemode.R;
import com.hisense.storemode.StoreModeApplication;
import com.hisense.storemode.bean.RestrictKeyEvent;
import com.hisense.storemode.manager.AppManager;
import com.hisense.storemode.manager.ReceiverManager;
import com.hisense.storemode.manager.StoreModeManager;
import com.hisense.storemode.manager.epos.EPosManager;
import com.hisense.storemode.manager.play.PlayManager;
import com.hisense.storemode.manager.play.player.IPlayer;
import com.hisense.storemode.manager.play.player.PicturePlayer;
import com.hisense.storemode.manager.task.BackgroundTaskManager;
import com.hisense.storemode.ui.fragment.PicFragment;
import com.hisense.storemode.ui.fragment.SignalFragment;
import com.hisense.storemode.ui.fragment.VideoFragment;
import com.hisense.storemode.utils.ConstantConfig;
import com.hisense.storemode.utils.LogUtils;
import com.hisense.storemode.utils.PreferenceUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import androidx.fragment.app.Fragment;
import me.yokeyword.fragmentation.SupportFragment;

public class MainActivity extends BaseActivity {
    private String TAG = "MainActivity";

    private String mPlayType = "";
    private String mPath = "";
    private int mResourceId = -1;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void afterCreate() {
        LogUtils.d(TAG, "afterCreate()");
        initPlayType(getIntent());
        EventBus.getDefault().register(this);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogUtils.d(TAG, "onNewIntent()  intent:" + intent);
        initPlayType(intent);
    }

    private synchronized void initPlayType(Intent intent) {

        LogUtils.d(TAG, "intent != null  " + (intent != null));
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
//                mPlayType = bundle.getString(ConstantConfig.PLAY_TYPE, "");
//                mPath = bundle.getString(ConstantConfig.FILE_PATH, "");
//                mResourceId = bundle.getInt(ConstantConfig.FILE_PATH_RESOURCE_ID, -1);


                LogUtils.d(TAG, "bundle != null  initPlayType  play type:" + mPlayType);
            }

        }

        mPlayType = ConstantConfig.PLAY_TYPE;
        mPath = ConstantConfig.FILE_PATH;
        mResourceId = ConstantConfig.FILE_PATH_RESOURCE_ID;

        LogUtils.d(TAG, "initPlayType  play type:" + mPlayType);
        SupportFragment topFragment = (SupportFragment) getTopFragment();

        int size = getSupportFragmentManager().getFragments().size();
        for (int i = 0; i < size; i++) {
            LogUtils.d(TAG, "--------fragment is: " + i + "  " + getSupportFragmentManager().getFragments().get(i));
        }
        LogUtils.e(TAG, "initPlayType start fragments size:" + size);
        if (size > 0) {
            getSupportFragmentManager().popBackStackImmediate(null, 1);
        }
        LogUtils.e(TAG, "getTopFragment is null :" + mPlayType);
        switch (mPlayType) {//monitor  fragment  count ----pop  or load or witch
            case ConstantConfig.PLAY_TYPE_VIDEO:
                //  loadRootFragment(R.id.fl_container, new VideoFragment());
                addBackgroundFragment(VideoFragment.newInstance(mPath));
                break;
            case ConstantConfig.PLAY_TYPE_PIC:
//                if (mResourceId != -1) {
//                    addBackgroundFragment(PicFragment.newInstance(mResourceId));
//                } else {
//                    addBackgroundFragment(PicFragment.newInstance(mPath));
//                }
                addBackgroundFragment(new PicFragment());
                // loadRootFragment(R.id.fl_container, new PicFragment());
                break;
            case ConstantConfig.PLAY_TYPE_SIGNAL:
                //loadRootFragment(R.id.fl_container, new SignalFragment());
                addBackgroundFragment(new SignalFragment());
                break;
//            }
            default:
                break;
        }


        if (topFragment != null) {
            LogUtils.e(TAG, "initPlayType  end fragments size:"
                    + getSupportFragmentManager().getFragments().size()
                    + "topfragment:" + topFragment.toString());
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        ConstantConfig.IS_MAIN_ACTIVITY_INVISIBLE = false;

        SeriesUtils.modifyGoogleServiceSwitch(false);
    }

    public void addBackgroundFragment(final Fragment fragment) {

        String tag = fragment.getClass().getName();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_container, fragment, tag)
                .addToBackStack(tag)
                .commitAllowingStateLoss();

    }

    @Override
    protected void onStop() {
        super.onStop();
        ConstantConfig.IS_MAIN_ACTIVITY_INVISIBLE = true;
        boolean isLiveTvTop = ConstantConfig.isLiveTvRunningTop();
        LogUtils.d(TAG, "onStop()  isLiveTvTop:" + isLiveTvTop);
        if (!isLiveTvTop) {
            EPosManager.getInstance().removeEposAndLogo();
        }

        IPlayer iPlayer = PlayManager.getInstance().getCurrentPlayer();
        if (iPlayer != null && !(iPlayer instanceof PicturePlayer)) {
            LogUtils.d(TAG, "onStop() iplayer:" + iPlayer);
            iPlayer.stop();
        }

        BackgroundTaskManager.getInstance().resumeStoreModeTask();

        SeriesUtils.modifyGoogleServiceSwitch(true);
    }

    @Override
    public void onBackPressedSupport() {

        LogUtils.d(TAG, "onBackPressedSupport()");
        if (EPosManager.getInstance().isEposShowing()) {
            LogUtils.d(TAG, "onBackPressedSupport epos is showing");
            EPosManager.getInstance().countDownTime();
        } else {

            LogUtils.d(TAG, "onBackPressedSupport epos is not showing");
            if (!ConstantConfig.isFTEFinish()) {
                ConstantConfig.setUserMode(ConstantConfig.MODE_HOME_MODE);
                ConstantConfig.setReceiverMonitoreSwitch(0);
                BackgroundTaskManager.getInstance().stopWorker();
                ReceiverManager.getInstance().unRegisterReceiver();
                AppManager.getInstance().AppExit(this);
            }
            finish();
        }
    }

    @Subscribe
    public void restrictButtonEvent(RestrictKeyEvent event) {
        if (event != null) {
            Settings.Global.putInt(StoreModeApplication.sContext.getContentResolver(),
                    ConstantConfig.RESTRICT_KEY_FLAG, event.restrict_flag);
            LogUtils.e(TAG, "restrictButtonEvent  restrict_flag:" + event.restrict_flag);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        LogUtils.d(TAG, "onDestroy() ");
        IPlayer iPlayer = PlayManager.getInstance().getCurrentPlayer();
        if (iPlayer != null && (iPlayer instanceof PicturePlayer)) {
            LogUtils.d(TAG, "onDestroy() iplayer:" + iPlayer);
            iPlayer.stop();
        }
        Glide.get(this).clearMemory();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LogUtils.d(TAG, "onConfigurationChanged()");
        EPosManager.getInstance().setAniDrawable(null);
        EPosManager.getInstance().countDownTime();
        PreferenceUtils.getInstance().save(ConstantConfig.RESET_PALY_POLICY, true);
        StoreModeManager.getInstance().start();
    }

}
