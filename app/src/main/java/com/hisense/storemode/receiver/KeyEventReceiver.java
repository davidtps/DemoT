package com.hisense.storemode.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;

import com.bumptech.glide.Glide;
import com.hisense.storemode.BuildConfig;
import com.hisense.storemode.StoreModeApplication;
import com.hisense.storemode.manager.AppManager;
import com.hisense.storemode.manager.epos.EPosManager;
import com.hisense.storemode.manager.play.PlayManager;
import com.hisense.storemode.manager.play.player.IPlayer;
import com.hisense.storemode.manager.play.player.videoplayer.IVideoPlayer;
import com.hisense.storemode.manager.play.player.videoplayer.VideoPlayer;
import com.hisense.storemode.manager.task.BackgroundTaskManager;
import com.hisense.storemode.utils.ConstantConfig;
import com.hisense.storemode.utils.LogUtils;

import java.util.Locale;

/**
 * Created by zhanglaizhi on 5/7/19.
 */

public class KeyEventReceiver extends BroadcastReceiver {

    private static final String TAG = "KeyEventReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtils.d(TAG, "onReceive " + intent.getIntExtra("keycode", -1) + "  languagetag:" + Locale.getDefault().toLanguageTag());
        if (intent == null) return;
        int keycode = intent.getIntExtra("keycode", -1);

        if (
//                keycode != KeyEvent.KEYCODE_VOLUME_UP
//                && keycode != KeyEvent.KEYCODE_VOLUME_DOWN
                keycode != KeyEvent.KEYCODE_VOLUME_MUTE
                        && keycode != KeyEvent.KEYCODE_BACK) {
            EPosManager.getInstance().removeEposAndLogo();
        }
        BackgroundTaskManager.getInstance().startEPosTask();

        if (keycode == KeyEvent.KEYCODE_HOME) {
            Activity activity = AppManager.getInstance().currentActivity();
            if (activity != null) {
                Glide.get(activity).clearMemory();
            } else {
                Glide.get(StoreModeApplication.sContext).clearMemory();
            }
        }
        //no key input 5min
        BackgroundTaskManager.getInstance().startAddRestrictKeyTask();
        //no key input 10min
        BackgroundTaskManager.getInstance().adjustAQPQ();
        //no key input 3min resumt storemode
        BackgroundTaskManager.getInstance().resumeStoreModeTask();

        if (keycode != KeyEvent.KEYCODE_BACK) {
            BackgroundTaskManager.getInstance().removeResumeDialog();
        }


        //katniss function  video pause/start  for:U6FUW
        if (ConstantConfig.SERIES_EM_USU6FUW.equals(BuildConfig.CURR_PLATFORM)) {

            IPlayer iPlayer = PlayManager.getInstance().getCurrentPlayer();
            IVideoPlayer videoPlayer = null;
            if (iPlayer != null && (iPlayer instanceof VideoPlayer)) {
                LogUtils.d(TAG, "onReceive() iplayer:" + iPlayer);
                videoPlayer = ((VideoPlayer) iPlayer).getCurrentVideoPlayer();
            }
            if (videoPlayer == null) {
                return;
            }
            if (keycode == KeyEvent.KEYCODE_MEDIA_PAUSE) {
                LogUtils.d(TAG, "onReceive KeyEvent.KEYCODE_MEDIA_PAUSE  code:" + intent.getIntExtra("keycode", -1));
                videoPlayer.pauseVideo();

            } else if (keycode == KeyEvent.KEYCODE_MEDIA_PLAY) {
                LogUtils.d(TAG, "onReceive KeyEvent.KEYCODE_MEDIA_PLAY code:" + intent.getIntExtra("keycode", -1));
                videoPlayer.startPlay();
            }
        }
    }
}
