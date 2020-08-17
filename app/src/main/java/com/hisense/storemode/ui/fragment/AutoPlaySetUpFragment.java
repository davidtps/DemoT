package com.hisense.storemode.ui.fragment;

import android.content.Context;
import android.os.Bundle;

import com.hisense.hianidraw.cache.SeriesUtils;
import com.hisense.storemode.R;
import com.hisense.storemode.utils.ConstantConfig;
import com.hisense.storemode.utils.PreferenceUtils;

import androidx.leanback.preference.LeanbackPreferenceFragment;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;

/**
 * Create by xuchongxiang at 2019-05-10 3:40 PM
 */

public class AutoPlaySetUpFragment extends LeanbackPreferenceFragment {

    private CheckBoxPreference mUsbVideoPreference;
    private CheckBoxPreference mUsbPicPreference;
    private CheckBoxPreference mSignalPreference;
    private CheckBoxPreference mBuildPicPreference;
    private CheckBoxPreference mBuildVideoPreference;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        initView();
        initData();
    }

    private void initView() {
        addPreferencesFromResource(R.xml.auto_setup);
        mUsbVideoPreference = (CheckBoxPreference) findPreference(getString(R.string.key_sharepref_usb_video_check));
        mUsbPicPreference = (CheckBoxPreference) findPreference(getString(R.string.key_sharepref_usb_pic_check));
        mSignalPreference = (CheckBoxPreference) findPreference(getString(R.string.key_sharepref_signal_check));
        mBuildPicPreference = (CheckBoxPreference) findPreference(getString(R.string.key_sharepref_build_in_pic_check));
        mBuildVideoPreference = (CheckBoxPreference) findPreference(getString(R.string.key_sharepref_build_in_video_check));
    }

    private void initData() {
        // usb video/ usb pic/ signal/ build pic /build video
        boolean[] arr = SeriesUtils.getAutoPlaySetupIsVisibleBooleans();

        mUsbVideoPreference.setVisible(arr[0]);
        mUsbPicPreference.setVisible(arr[1]);
        mSignalPreference.setVisible(arr[2]);
        mBuildPicPreference.setVisible(arr[3]);
        mBuildVideoPreference.setVisible(arr[4]);


        arr = SeriesUtils.getAutoPlaySetupDefaultCheckBooleans();

        if (arr[0]) {//usb video
            boolean flag = (boolean) PreferenceUtils.getInstance().get(ConstantConfig.SHAREPREF_USB_VIDEO_CHECK, false);
            mUsbVideoPreference.setChecked(flag);
        }

        if (arr[1]) {//usb pic
            boolean flag = (boolean) PreferenceUtils.getInstance().get(ConstantConfig.SHAREPREF_USB_PIC_CHECK, false);
            mUsbPicPreference.setChecked(flag);
        }

        if (arr[2]) {//signal
            boolean flag = (boolean) PreferenceUtils.getInstance().get(ConstantConfig.SHAREPREF_SIGNAL_CHECK, false);
            mSignalPreference.setChecked(flag);
        }

        if (arr[3]) {//build pic
            boolean flag = (boolean) PreferenceUtils.getInstance().get(ConstantConfig.SHAREPREF_BUILD_IN_PIC_CHECK, false);
            mBuildPicPreference.setChecked(flag);
        }

        if (arr[4]) {//build video
            boolean flag = (boolean) PreferenceUtils.getInstance().get(ConstantConfig.SHAREPREF_BUILD_IN_VIDEO_CHECK, false);
            mBuildVideoPreference.setChecked(flag);
        }
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        String key = preference.getKey();
        PreferenceUtils.getInstance().save(ConstantConfig.RESET_PALY_POLICY, true);

        if (key.equals(getString(R.string.key_sharepref_usb_video_check))) {
            boolean flag = (boolean) PreferenceUtils.getInstance().get(ConstantConfig.SHAREPREF_USB_VIDEO_CHECK, true);
            PreferenceUtils.getInstance().save(ConstantConfig.SHAREPREF_USB_VIDEO_CHECK, !flag);
            mUsbVideoPreference.setChecked(!flag);
            return true;
        }
        if (key.equals(getString(R.string.key_sharepref_usb_pic_check))) {
            boolean flag = (boolean) PreferenceUtils.getInstance().get(ConstantConfig.SHAREPREF_USB_PIC_CHECK, false);
            PreferenceUtils.getInstance().save(ConstantConfig.SHAREPREF_USB_PIC_CHECK, !flag);
            mUsbPicPreference.setChecked(!flag);
            return true;
        }
        if (key.equals(getString(R.string.key_sharepref_signal_check))) {
            boolean flag = (boolean) PreferenceUtils.getInstance().get(ConstantConfig.SHAREPREF_SIGNAL_CHECK, true);
            PreferenceUtils.getInstance().save(ConstantConfig.SHAREPREF_SIGNAL_CHECK, !flag);
            mSignalPreference.setChecked(!flag);
            return true;
        }
        if (key.equals(getString(R.string.key_sharepref_build_in_pic_check))) {
            boolean flag = (boolean) PreferenceUtils.getInstance().get(ConstantConfig.SHAREPREF_BUILD_IN_PIC_CHECK, true);
            PreferenceUtils.getInstance().save(ConstantConfig.SHAREPREF_BUILD_IN_PIC_CHECK, !flag);
            mBuildPicPreference.setChecked(!flag);
            return true;
        }
        if (key.equals(getString(R.string.key_sharepref_build_in_video_check))) {
            boolean flag = (boolean) PreferenceUtils.getInstance().get(ConstantConfig.SHAREPREF_BUILD_IN_VIDEO_CHECK, true);
            PreferenceUtils.getInstance().save(ConstantConfig.SHAREPREF_BUILD_IN_VIDEO_CHECK, !flag);
            mBuildVideoPreference.setChecked(!flag);
            return true;
        }
        return super.onPreferenceTreeClick(preference);
    }

}
