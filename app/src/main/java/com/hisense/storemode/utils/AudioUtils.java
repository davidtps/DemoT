package com.hisense.storemode.utils;

import android.content.Context;
import android.media.AudioManager;

import com.hisense.hianidraw.cache.SeriesUtils;
import com.hisense.storemode.StoreModeApplication;

/**
 * Created by xuchongxiang on 2020年06月29日.
 */
public class AudioUtils {

    private static final String TAG = "AudioUtils";

    public static void setStoreModeVolume() {
        setStoreModeVolume(false);
    }

    public static void setStoreModeVolume(boolean isFromBoot) {
        if (SeriesUtils.DEFAULT_AUDIO_VOLUME == -1) return;
        if (!isFromBoot) {
            saveHomeVolume();
        }
        setStoreVolume();
    }

    public static void restoreHomeModeVolume() {
        if (SeriesUtils.DEFAULT_AUDIO_VOLUME == -1) return;
        AudioManager am = (AudioManager) StoreModeApplication.sContext.getSystemService(Context.AUDIO_SERVICE);
        int volume = (int) PreferenceUtils.getInstance().get(ConstantConfig.SHAREPREF_HOMEMODE_AUDIO_VOLUME, 0);
        am.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
        LogUtils.i(TAG, "restoreHomeModeVolume homemode volume: " + volume);
    }

    public static void updateLocalStoreModeVolume() {
        if (SeriesUtils.DEFAULT_AUDIO_VOLUME == -1) return;
        //set storemode volume
        AudioManager am = (AudioManager) StoreModeApplication.sContext.getSystemService(Context.AUDIO_SERVICE);
        int volume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        PreferenceUtils.getInstance().save(ConstantConfig.SHAREPREF_STOREMODE_AUDIO_VOLUME, volume);
        LogUtils.i(TAG, "updateLocalStoreModeVolume volume: " + volume);
    }

    private static void saveHomeVolume() {
        //get homemode volume and save
        AudioManager am = (AudioManager) StoreModeApplication.sContext.getSystemService(Context.AUDIO_SERVICE);
        int volume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        PreferenceUtils.getInstance().save(ConstantConfig.SHAREPREF_HOMEMODE_AUDIO_VOLUME, volume);
        LogUtils.i(TAG, "saveHomeVolume volume: " + volume);
    }

    private static void setStoreVolume() {
        if (SeriesUtils.DEFAULT_AUDIO_VOLUME == -1) return;
        //set storemode volume
        AudioManager am = (AudioManager) StoreModeApplication.sContext.getSystemService(Context.AUDIO_SERVICE);
        int volume = (int) PreferenceUtils.getInstance().get(ConstantConfig.SHAREPREF_STOREMODE_AUDIO_VOLUME, SeriesUtils.DEFAULT_AUDIO_VOLUME);
        am.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
        LogUtils.i(TAG, "setStoreVolume volume: " + volume);
    }
}
