package com.hisense.storemode.ui.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.hisense.avmiddleware.AvMiddleWareManager;
import com.hisense.hianidraw.cache.SeriesUtils;
import com.hisense.storemode.R;
import com.hisense.storemode.utils.AudioUtils;
import com.hisense.storemode.utils.ConstantConfig;

import androidx.leanback.preference.LeanbackPreferenceFragment;
import androidx.preference.Preference;

/**
 * @author tianpengsheng
 * create at   6/11/19 1:50 PM
 */

public class AqPqAdjustFragment extends LeanbackPreferenceFragment {

    private static final String TAG = "AqPqAdjustFragment";
    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        initView();
        initData();
        initListener();
    }

    private void initView() {
        addPreferencesFromResource(R.xml.aq_pq_ajust);
    }

    private void initData() {

    }

    private void initListener() {
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        String key = preference.getKey();
        if (key.equals(getString(R.string.key_picture_ajust))) {
            Intent intent = new Intent();
            switch (Build.VERSION.SDK_INT) {
                case Build.VERSION_CODES.O:
                case Build.VERSION_CODES.O_MR1:
                    intent.setAction("com.mediatek.wwtv.setting.PICTURE_SETTINGS");
                    intent.setComponent(new ComponentName(ConstantConfig.LIVE_TV_PKG_NAME,
                            "com.mediatek.wwtv.setting.PictureActivity"));
                    break;
                default://p and q
                    intent.setAction("android.settings.PICTURE_SETTINGS");
//                    intent.setPackage(ConstantConfig.TV_SETTINGS_PKG_NAME);
//                    intent.setComponent(new ComponentName(ConstantConfig.TV_SETTINGS_PKG_NAME,
//                            "com.android.tv.settings.partnercustomizer.picture.PictureActivity"));
                    break;
            }
            startActivity(intent);
            return true;
        }
        if (key.equals(getString(R.string.key_sound_ajust))) {
            Intent intent = new Intent();
            switch (Build.VERSION.SDK_INT) {
                case Build.VERSION_CODES.O:
                case Build.VERSION_CODES.O_MR1:
                    intent.setAction("com.mediatek.wwtv.setting.Sound_SETTINGS");
                    intent.setComponent(new ComponentName(ConstantConfig.LIVE_TV_PKG_NAME,
                            "com.mediatek.wwtv.setting.SoundActivity"));
                    break;
                default://p and q
                    intent.setAction("android.settings.SOUND_SETTINGS");
//                    intent.setPackage(ConstantConfig.TV_SETTINGS_PKG_NAME);
//                    intent.setComponent(new ComponentName(ConstantConfig.TV_SETTINGS_PKG_NAME,
//                            "com.android.tv.settings.device.sound.SoundActivity"));
                    break;
            }
            startActivity(intent);
            return true;
        }
        return super.onPreferenceTreeClick(preference);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AudioUtils.updateLocalStoreModeVolume();
        SeriesUtils.backupStoreSetting();
    }
}
