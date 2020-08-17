package com.hisense.storemode.manager.play.player.videoplayer;

import android.media.MediaPlayer;
import android.view.SurfaceHolder;

import java.io.File;
import java.util.HashMap;

/**
 * Created by davidtps on 2019年04月28日 16时32分.
 */
public interface IVideoPlayer {

    void playVideo(SurfaceHolder holder, HashMap<String, String> map, File file);

    void stopVideo();

    void pauseVideo();

    HashMap<String, String> setHeads(File file);

    void setListener(MediaPlayer.OnPreparedListener preparedListener, MediaPlayer.OnErrorListener errorListener
            , MediaPlayer.OnCompletionListener completionListener, MediaPlayer.OnInfoListener infoListener);

    void updatePlayState(int state);

    void startPlay();
}
