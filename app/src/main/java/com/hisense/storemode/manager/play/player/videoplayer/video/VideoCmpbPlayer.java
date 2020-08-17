package com.hisense.storemode.manager.play.player.videoplayer.video;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.SurfaceHolder;

import com.hisense.storemode.StoreModeApplication;
import com.hisense.storemode.manager.play.player.videoplayer.IVideoPlayer;
import com.hisense.storemode.utils.ConstantConfig;
import com.hisense.storemode.utils.LogUtils;
import com.mediatek.MtkMediaPlayer;
import com.mediatek.mtkaudiopatchmanager.MtkAudioPatchManager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;


/**
 * Created by cuihuihui on 2019年04月22日 11时42分.
 */
public class VideoCmpbPlayer implements IVideoPlayer {
    private static final String TAG = "VideoCmpbPlayer";
    private static final String EXTRA_DECODER_FLAG = "true";

    // must keep sync with MTK player
    private static final int MEDIAPLAYER_TYPE = 6; // CMPB_PLAYER

    private MtkAudioPatchManager mMtkAudioPatchManager;
    private MtkMediaPlayer mMtkMediaPlayer;
    private int mPlayState = ConstantConfig.STATE_IDLE;
    private MediaPlayer.OnPreparedListener mPreparedListener;
    private MediaPlayer.OnCompletionListener mCompletionListener;
    private MediaPlayer.OnErrorListener mErrorListener;
    private MediaPlayer.OnInfoListener mInfoListener;


    @Override
    public HashMap setHeads(File playFile) {
        LogUtils.d(TAG, "setHeads() is invoked.");

        HashMap headerMaps = new HashMap<String, String>();
        headerMaps.put(ConstantConfig.RESOURCE_MANAGER, playFile.getAbsoluteFile());
        headerMaps.put(ConstantConfig.OUT_PATH, ConstantConfig.OUTPUT_VIDEO_MAIN);
        headerMaps.put(ConstantConfig.VIDEO_DECODER, EXTRA_DECODER_FLAG);
        headerMaps.put(ConstantConfig.AUDIO_DECODER, EXTRA_DECODER_FLAG);

        return headerMaps;
    }

    @Override
    public void setListener(MediaPlayer.OnPreparedListener preparedListener, MediaPlayer.OnErrorListener errorListener
            , MediaPlayer.OnCompletionListener completionListener, MediaPlayer.OnInfoListener infoListener) {
        mPreparedListener = preparedListener;
        mErrorListener = errorListener;
        mCompletionListener = completionListener;
        mInfoListener = infoListener;
    }

    @Override
    public void playVideo(SurfaceHolder holder, HashMap<String, String> map, File file) {
        LogUtils.d(TAG, "playVideo(), holder = " + holder + "; map = " + map + "; file = " + file);
        if (holder == null || file == null) {
            return;
        }

        if (mMtkMediaPlayer != null) {
            LogUtils.e(TAG, "playVideo(), previous cmpb player is still working and RELEASE it first, mMtkMediaPlayer = " + mMtkMediaPlayer);
            releaseCbmpPlayer();
        }

        // create player
        mMtkMediaPlayer = new MtkMediaPlayer(MEDIAPLAYER_TYPE);
        mMtkMediaPlayer.setOnPreparedListener(mPreparedListener);
        mMtkMediaPlayer.setOnErrorListener(mErrorListener);
        mMtkMediaPlayer.setOnInfoListener(mInfoListener);
        mMtkMediaPlayer.setOnCompletionListener(mCompletionListener);

        try {
            mMtkMediaPlayer.setDisplay(holder);
            mMtkMediaPlayer.setDataSource(StoreModeApplication.sContext, Uri.fromFile(file), map);
            mMtkMediaPlayer.setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).build());
            mMtkMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            if (mMtkAudioPatchManager == null) {
                mMtkAudioPatchManager = new MtkAudioPatchManager(StoreModeApplication.sContext);
            }
            mMtkAudioPatchManager.createAudioPatch();

            mMtkMediaPlayer.setScreenOnWhilePlaying(true);
            mMtkMediaPlayer.prepare();

            // update state
            updatePlayState(ConstantConfig.STATE_PREPARING);

        } catch (IOException e) {
            LogUtils.d(TAG, "playVideo(), openVideo IOException = " + e.toString());

            // update state
            updatePlayState(ConstantConfig.STATE_ERROR);

            mErrorListener.onError(mMtkMediaPlayer, MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
        }
    }

    @Override
    public void stopVideo() {
        LogUtils.d(TAG, "stopVideo() is invoked.");

        releaseCbmpPlayer();
    }

    @Override
    public void pauseVideo() {
        boolean state = isInPlaybackState();
        LogUtils.d(TAG, "pauseVideo(), isInPlaybackState = " + state);

        if (state) {
            mMtkMediaPlayer.pause();
        }
    }

    private void releaseCbmpPlayer() {
        LogUtils.d(TAG, "releaseCbmpPlayer() is invoked.");

        // Step1: relase Audio
        if (mMtkAudioPatchManager != null) {
            mMtkAudioPatchManager.releaseAudioPatch();
            mMtkAudioPatchManager = null;
        }

        // Step2: relase Player
        if (mMtkMediaPlayer == null) {
            LogUtils.d(TAG, "releaseCbmpPlayer(), mMtkMediaPlayer is null");
            return;
        }
////use player.release  , don't user player.stop
//        if (mMtkMediaPlayer.isPlaying()) {
//            mMtkMediaPlayer.stop();
//        }
        mMtkMediaPlayer.reset();
        mMtkMediaPlayer.release();
        mMtkMediaPlayer.setOnPreparedListener(null);
        mMtkMediaPlayer.setOnErrorListener(null);
        mMtkMediaPlayer.setOnCompletionListener(null);
        mMtkMediaPlayer.setOnInfoListener(null);
        mMtkMediaPlayer = null;

        // Step3: update state
        updatePlayState(ConstantConfig.STATE_IDLE);
    }

    @Override
    public void startPlay() {
        boolean state = isInPlaybackState();
        LogUtils.d(TAG, "startPlay(), isInPlaybackState = " + state);
        if (state) {
            mMtkMediaPlayer.start();
        }
    }

    private boolean isInPlaybackState() {
        return mMtkMediaPlayer != null
                && mPlayState != ConstantConfig.STATE_ERROR
                && mPlayState != ConstantConfig.STATE_IDLE
                && mPlayState != ConstantConfig.STATE_PREPARING;
    }

    @Override
    public void updatePlayState(int state) {
        LogUtils.d(TAG, "updatePlayState(), mPlayState is changed from [" + mPlayState + "] to [" + state + "].");
        mPlayState = state;
    }
}

