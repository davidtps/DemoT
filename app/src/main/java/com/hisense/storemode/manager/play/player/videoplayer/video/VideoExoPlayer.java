package com.hisense.storemode.manager.play.player.videoplayer.video;

import android.media.MediaPlayer;
import android.net.Uri;
import android.view.SurfaceHolder;

import com.hisense.storemode.StoreModeApplication;
import com.hisense.storemode.manager.play.player.videoplayer.IVideoPlayer;
import com.hisense.storemode.utils.ConstantConfig;
import com.hisense.storemode.utils.LogUtils;
import com.mediatek.ExoMediaPlayer;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by cuihuihui on 2019年04月22日 11时42分.
 */
public class VideoExoPlayer implements IVideoPlayer {
    private static final String TAG = "VideoExoPlayer";
    private static final String EXTRA_DECODER_FLAG = "true";
    private int mPlayState = ConstantConfig.STATE_IDLE;

    // private MtkAudioPatchManager mMtkAudioPatchManager;
    private ExoMediaPlayer mExoMediaPlayer;
    private MediaPlayer.OnPreparedListener mPreparedListener;
    private MediaPlayer.OnCompletionListener mCompletionListener;
    private MediaPlayer.OnErrorListener mErrorListener;
    private MediaPlayer.OnInfoListener mInfoListener;


    @Override
    public HashMap setHeads(File file) {
        LogUtils.d(TAG, "setHeads() is invoked.");

        HashMap headerMaps = new HashMap<String, String>();
        headerMaps.put(ConstantConfig.RESOURCE_MANAGER, file.getAbsoluteFile());
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

        if (mExoMediaPlayer != null) {
            LogUtils.e(TAG, "playVideo(), previous ExoPlayer is still working and RELEASE it first, mExoMediaPlayer = " + mExoMediaPlayer);
            releaseExoPlayer();
        }

        mExoMediaPlayer = new ExoMediaPlayer();
        mExoMediaPlayer.setOnPreparedListener(mPreparedListener);
        mExoMediaPlayer.setOnErrorListener(mErrorListener);
        mExoMediaPlayer.setOnInfoListener(mInfoListener);
        mExoMediaPlayer.setOnCompletionListener(mCompletionListener);
        try {
            mExoMediaPlayer.setSurface(holder.getSurface());
            mExoMediaPlayer.setDataSource(StoreModeApplication.sContext, Uri.fromFile(file), map);
            //  mExoMediaPlayer.setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).build());
            //    mExoMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

//            if (mMtkAudioPatchManager == null) {
//                mMtkAudioPatchManager = new MtkAudioPatchManager(StoreModeApplication.sContext);
//            }
//            mMtkAudioPatchManager.createAudioPatch();
            mExoMediaPlayer.setPlayerRole(ExoMediaPlayer.PlayerRole.ROLE_VIDEO_PLAYBACK);
            //   mExoMediaPlayer.setScreenOnWhilePlaying(true);
            mExoMediaPlayer.prepareAsync();

            // update state
            updatePlayState(ConstantConfig.STATE_PREPARING);

        } catch (IOException e) {
            LogUtils.d(TAG, "playVideo(), IOException = " + e.toString());

            // update state
            updatePlayState(ConstantConfig.STATE_ERROR);

            mErrorListener.onError(mExoMediaPlayer, MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
        }
    }

    @Override
    public void stopVideo() {
        LogUtils.d(TAG, "stopVideo()");

        releaseExoPlayer();
    }

    @Override
    public void pauseVideo() {
        boolean state = isInPlaybackState();
        LogUtils.d(TAG, "pauseVideo(), isInPlaybackState = " + state);

        if (state) {
            mExoMediaPlayer.pause();
        }
    }

    @Override
    public void startPlay() {
        boolean state = isInPlaybackState();
        LogUtils.d(TAG, "startPlay(), isInPlaybackState = " + state);

        if (state) {
            mExoMediaPlayer.start();
        }
    }

    private void releaseExoPlayer() {
        LogUtils.d(TAG, "releaseExoPlayer()");

        // Step1: release Audio
//        if (mMtkAudioPatchManager != null) {
//            mMtkAudioPatchManager.releaseAudioPatch();
//            mMtkAudioPatchManager = null;
//        }

        // Step2: release Player
        if (mExoMediaPlayer == null) {
            LogUtils.d(TAG, "releaseExoPlayer() mExoMediaPlayer is null");
            return;
        }
//use player.release  , don't user player.stop
//        if (mExoMediaPlayer.isPlaying()) {
//            mExoMediaPlayer.stop();
//        }
        mExoMediaPlayer.reset();
        mExoMediaPlayer.release();
        mExoMediaPlayer.setOnPreparedListener(null);
        mExoMediaPlayer.setOnErrorListener(null);
        mExoMediaPlayer.setOnCompletionListener(null);
        mExoMediaPlayer.setOnInfoListener(null);
        mExoMediaPlayer = null;
//
//        if (mMtkAudioPatchManager != null) {
//            mMtkAudioPatchManager.releaseAudioPatch();
//            mMtkAudioPatchManager = null;
//        }

        // Step3: update state
        updatePlayState(ConstantConfig.STATE_IDLE);
    }

    private boolean isInPlaybackState() {
        return mExoMediaPlayer != null
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

