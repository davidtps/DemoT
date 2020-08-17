package com.hisense.storemode.manager.play;

import com.hisense.storemode.bean.Player;
import com.hisense.storemode.manager.play.player.IPlayer;
import com.hisense.storemode.utils.ConstantConfig;
import com.hisense.storemode.utils.LogUtils;

/**
 * Created by tianpengsheng on 2019年04月18日 16时48分.
 * control playPolicy
 */
public class PlayManager {
    private static final String TAG = "PlayManager";
    private volatile static PlayManager sInstance;
    private IPlayer mCurrentPlayer;
    private PlayPolicy mPlayPolicy;

    private PlayManager() {
    }

    public static PlayManager getInstance() {
        if (sInstance == null) {
            synchronized (PlayManager.class) {
                if (sInstance == null)
                    sInstance = new PlayManager();
            }
        }
        return sInstance;
    }

    public IPlayer getCurrentPlayer() {
        LogUtils.i(TAG, "getCurrentPlayer()    mCurrentPlayer:" + mCurrentPlayer);
        return mCurrentPlayer;
    }

    public void setCurrentPlayer(IPlayer player) {
        mCurrentPlayer = player;
        LogUtils.i(TAG, "setCurrentPlayer()    mCurrentPlayer:" + mCurrentPlayer);
    }

    public void startPlay() {
        LogUtils.i(TAG, "startPlay()  mPlayPolicy == null: " + (mPlayPolicy == null) + "  mPlayPolicy:" + mPlayPolicy);
        if (mCurrentPlayer != null) {
            LogUtils.i(TAG, "startPlay()  mCurrentPlayer: " + mCurrentPlayer.toString());
        } else {
            LogUtils.i(TAG, "startPlay()  mCurrentPlayer: is null");
        }


        if (mPlayPolicy == null)
            mPlayPolicy = new PlayPolicy();
        mPlayPolicy.playNext();
    }


    //is built-in video is playing
    public boolean isBuildInVideoPlaying() {
        if (mPlayPolicy == null) return false;
        Player currentPlayerBean = mPlayPolicy.getCurrentPlayerBean();
        if (currentPlayerBean != null) {
            if (currentPlayerBean.mType == ConstantConfig.TYPE_VALUE_BUILD_IN_VIDEO && !ConstantConfig.IS_MAIN_ACTIVITY_INVISIBLE) {
                LogUtils.d(TAG, "isBuildInVideoPlaying()   is playing built-in video");
                return true;
            }
        }
        LogUtils.d(TAG, "isBuildInVideoPlaying()   is not playing built-in video ,currType:" + currentPlayerBean.mType);
        return false;
    }

}
