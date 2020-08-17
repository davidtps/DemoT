package com.hisense.storemode.manager.play.player.videoplayer.factory;

import com.hisense.storemode.manager.StoreModeManager;
import com.hisense.storemode.manager.play.player.videoplayer.IVideoPlayer;
import com.hisense.storemode.manager.play.player.videoplayer.video.VideoCmpbPlayer;
import com.hisense.storemode.manager.play.player.videoplayer.video.VideoDefaultPlayer;
import com.hisense.storemode.manager.play.player.videoplayer.video.VideoExoPlayer;
import com.hisense.storemode.utils.ConstantConfig;
import com.hisense.storemode.utils.LogUtils;
import com.hisense.storemode.utils.StringUtil;

/**
 * Created by cuihuihui on 2019年04月28日 15时48分.
 */
public class VideoFactory {
    private static final String TAG = "VideoFactory";

    public static IVideoPlayer createVideoPlayer(String plantForm, String filePath) {
        if (StringUtil.isEmpty(filePath) || StringUtil.isEmpty(plantForm)) {
            LogUtils.d(TAG, "createVideoPlayer(), plantForm or filePath is NULL.");

            //play next
            StoreModeManager.getInstance().start();
            return null;
        }

        IVideoPlayer videoPlayer;
        switch (plantForm) {
            case ConstantConfig.PLATFORM_5660: {
                videoPlayer = new VideoCmpbPlayer();
                break;
            }

            case ConstantConfig.PLATFORM_6886: {
                videoPlayer = new VideoExoPlayer();
                break;
            }

            default: {
                LogUtils.d(TAG, "createVideoPlayer(), plantForm default");
                videoPlayer = new VideoDefaultPlayer();
                break;
            }
        }

        return videoPlayer;
    }
}
