package com.hisense.storemode.manager.play.player.videoplayer.video;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.SurfaceHolder;

import com.hisense.storemode.manager.play.player.videoplayer.IVideoPlayer;
import com.hisense.storemode.utils.ConstantConfig;
import com.hisense.storemode.utils.LogUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;


/**
 * Created by cuihuihui on 2019年04月22日 11时42分.
 */
public class VideoDefaultPlayer implements IVideoPlayer {
    private static final String TAG = "VideoDefaultPlayer";
    private static final String EXTRA_DECODER_FLAG = "true";

    private int mPlayState = ConstantConfig.STATE_IDLE;
    private MediaPlayer mMediaPlayer;
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

        if (mMediaPlayer != null) {
            LogUtils.e(TAG, "playVideo(), previous player is still working and RELEASE it first, mMediaPlayer = " + mMediaPlayer);
            releasePlayer();
        }

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnPreparedListener(mPreparedListener);
        mMediaPlayer.setOnErrorListener(mErrorListener);
        mMediaPlayer.setOnInfoListener(mInfoListener);
        mMediaPlayer.setOnCompletionListener(mCompletionListener);
        try {
            mMediaPlayer.setDisplay(holder);
            mMediaPlayer.setDataSource(file.getPath());
            //  mMediaPlayer.setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).build());
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setScreenOnWhilePlaying(true);
            mMediaPlayer.prepare();

            // update state
            updatePlayState(ConstantConfig.STATE_PREPARING);

        } catch (IOException e) {
            LogUtils.d(TAG, "playVideo(), IOException = " + e.toString());

            // update state
            updatePlayState(ConstantConfig.STATE_ERROR);

            mErrorListener.onError(mMediaPlayer, MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
        }
    }

    @Override
    public void stopVideo() {
        LogUtils.d(TAG, "stopVideo()");
        releasePlayer();
    }

    @Override
    public void pauseVideo() {
        boolean state = isInPlaybackState();
        LogUtils.d(TAG, "pauseVideo(), isInPlaybackState = " + state);

        if (state) {
            mMediaPlayer.pause();
        }
    }


    @Override
    public void startPlay() {
        boolean state = isInPlaybackState();
        LogUtils.d(TAG, "startPlay(), isInPlaybackState = " + state);
        if (state) {
            mMediaPlayer.start();
        }
    }

    private void releasePlayer() {
        LogUtils.d(TAG, "releasePlayer() is invoked.");
        if (mMediaPlayer == null) {
            LogUtils.d(TAG, "releasePlayer(), mMediaPlayer is NULL.");
            return;
        }

        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
        mMediaPlayer.reset();
        mMediaPlayer.release();
        mMediaPlayer.setOnPreparedListener(null);
        mMediaPlayer.setOnErrorListener(null);
        mMediaPlayer.setOnCompletionListener(null);
        mMediaPlayer.setOnInfoListener(null);
        mMediaPlayer = null;

        // update state
        updatePlayState(ConstantConfig.STATE_IDLE);
    }

    private boolean isInPlaybackState() {
        return mMediaPlayer != null
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

