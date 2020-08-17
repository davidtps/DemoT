package com.hisense.storemode.manager.play.player.videoplayer;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.SystemClock;
import android.view.SurfaceHolder;

import com.hisense.storemode.manager.AppManager;
import com.hisense.storemode.manager.StoreModeManager;
import com.hisense.storemode.manager.play.player.IPlayer;
import com.hisense.storemode.manager.play.player.videoplayer.factory.VideoFactory;
import com.hisense.storemode.ui.fragment.VideoFragment;
import com.hisense.storemode.utils.ConstantConfig;
import com.hisense.storemode.utils.LogUtils;
import com.hisense.storemode.utils.PreferenceUtils;

import java.io.File;
import java.util.HashMap;

/**
 * Created by cuihuihui on 2019年04月22日 11时42分.
 */
public class VideoPlayer implements IPlayer {
    private static final String TAG = "VideoPlayer";
    private Handler mDelayHandler = null;
    private String mPlantForm;
    private String mVideoFilePath;

    private IVideoPlayer mVideoPlayer;
    private SurfaceHolder mSurfaceHolder;
    private VideoFragment mFragment;
    private long lastClickTime = 0;
    private int duration = 500;//time interval

//    private boolean mIsError = false;

    private MediaPlayer.OnPreparedListener mPreparedListener = mp -> {
        LogUtils.d(TAG, "OnPreparedListener onPrepared()");
        if (mVideoPlayer != null) {
            //update state
            mVideoPlayer.updatePlayState(ConstantConfig.STATE_PREPARED);
            mVideoPlayer.startPlay();
        }
    };

    private MediaPlayer.OnErrorListener mErrorListener = (mp, what, extra) -> {
//        ToastUtil.showToast("error video code:" + what);
        LogUtils.d(TAG, "mErrorListener onError()  what:" + what);
        stop();
        //palymanager next
//        mIsError = true;
        LogUtils.d(TAG, "mErrorListener onError()  playpolicy reset:" + what);
//        PreferenceUtils.getInstance().save(ConstantConfig.RESET_PALY_POLICY, true);
        if (what != 4000) {//4000 code:player is preempted  don't start play
            StoreModeManager.getInstance().start();
        }
        return false;
    };
    private MediaPlayer.OnInfoListener mInfoListener = (mp, what, extra) -> {
        LogUtils.d(TAG, "mInfoListener onInfo() what = " + what + ", extra = " + extra);
        //first frame of video
        if (what == 3) {
            if (mFragment != null) {
                mFragment.showLoading(false);
            }
        } else if (what == -5000) {//special handle
            LogUtils.d(TAG, "mInfoListener onInfo() what = " + what + "special handle stop() invoke and start() next policy; ");
            stop();
            StoreModeManager.getInstance().start();
        }
        return false;
    };

    private MediaPlayer.OnCompletionListener mCompletionListener = mp -> {
        LogUtils.d(TAG, "mCompletionListener onCompletion()");
//        if (mIsError) {
//            return;
//        }
        if (mFragment != null) {
            LogUtils.d(TAG, "mCompletionListener onCompletion() mFragment != null");
            LogUtils.d(TAG, "mCompletionListener onCompletion() mFragment.mIsActive = " + mFragment.mIsActive);

            if (mFragment.mIsActive) {
                StoreModeManager.getInstance().start();

            } else {
                if (mVideoFilePath != null && mSurfaceHolder != null) {
                    playNext(mVideoFilePath);
                    start(mSurfaceHolder);
                }
            }
        } else {
            LogUtils.d(TAG, "mCompletionListener onCompletion() mFragment == null");

            StoreModeManager.getInstance().start();
        }

    };

    MediaPlayer.OnSeekCompleteListener mOnSeekCompleteListener = new MediaPlayer.OnSeekCompleteListener() {
        @Override
        public void onSeekComplete(MediaPlayer mp) {
            LogUtils.d(TAG, "OnSeekCompleteListener");
        }
    };


    public VideoPlayer(String plantForm, String videoPath) {
        mPlantForm = plantForm;
        mVideoFilePath = videoPath;
    }

    @Override
    public void start(Object object) {
        LogUtils.d(TAG, "start() is invoked.");

        if (!(object instanceof SurfaceHolder)) {
            LogUtils.d(TAG, "start(), the object is not instanceof SurfaceHolder.");
            return;
        }
        mSurfaceHolder = (SurfaceHolder) object;

        // Step1: get Video file
        File file = new File(mVideoFilePath);
        boolean isExisted = (file != null && file.exists());
        if (!isExisted) {
            LogUtils.d(TAG, "start(), mVideoFilePath is not exists, mVideoFilePath = " + mVideoFilePath);
            PreferenceUtils.getInstance().save(ConstantConfig.RESET_PALY_POLICY, true);
            StoreModeManager.getInstance().start();
            return;
        } else {
            LogUtils.d(TAG, "start(), mVideoFilePath is exists, mVideoFilePath = " + mVideoFilePath);
        }

        // Step2: get Player
        mVideoPlayer = VideoFactory.createVideoPlayer(mPlantForm, mVideoFilePath);
        if (mVideoPlayer == null) {
            LogUtils.e(TAG, "start(), mVideoPlayer is NULL, mPlantForm = [" + mPlantForm + "]; mVideoFilePath = [" + mVideoFilePath + "]");
            return;
        }


        // Step3: start to play
        HashMap<String, String> maps = mVideoPlayer.setHeads(file);
        mVideoPlayer.setListener(mPreparedListener, mErrorListener, mCompletionListener, mInfoListener);
        if (AppManager.getInstance().currentActivity() == null) {
            LogUtils.d(TAG, "start() currentActivity = null");
            return;
        }
//        AppManager.getInstance().currentActivity().runOnUiThread(() -> {
//            LogUtils.d(TAG, "start()  playvideo");
//            mVideoPlayer.playVideo(mSurfaceHolder, maps, file);
//        });
        if (mDelayHandler == null) {
            mDelayHandler = new Handler(AppManager.getInstance().currentActivity().getMainLooper());
        }

        if (isFastTrigger()) {
            mDelayHandler.postDelayed(() -> {
                LogUtils.d(TAG, "start()  playvideo  mDelayHandler:" + mDelayHandler.toString());
                if (mVideoPlayer != null)
                    mVideoPlayer.playVideo(mSurfaceHolder, maps, file);
            }, 1000);
        }

    }


    public boolean isFastTrigger() {
        long time = SystemClock.uptimeMillis();
        LogUtils.d(TAG, "isFastTrigger() :duration:" + (time - lastClickTime));
        if (time - lastClickTime > duration) {
            lastClickTime = time;
            return true;
        }
        return false;
    }

    @Override
    public void stop() {
        LogUtils.d(TAG, "stop() is invoked.  mVideoPlayer:" + mVideoPlayer);

        if (mVideoPlayer != null) {
            mVideoPlayer.stopVideo();
            mVideoPlayer = null;
        }
    }

    @Override
    public void playNext(Object newFilePath) {
        LogUtils.d(TAG, "playNext(), newFilePath = " + newFilePath);
        if (!(newFilePath instanceof String)) {
            LogUtils.d(TAG, "playNext(), newFilePath is not String.");
            return;
        }

        stop();
        mVideoFilePath = (String) newFilePath;
        LogUtils.d(TAG, "playNext(), mVideoFilePath =" + mVideoFilePath);

    }


    public IVideoPlayer getCurrentVideoPlayer() {
        return mVideoPlayer;
    }

    public void setFragment(VideoFragment fragment) {
        LogUtils.d(TAG, "setFragment() ");

        mFragment = fragment;
    }

}

