package com.hisense.storemode.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;

import com.hisense.hianidraw.cache.SeriesUtils;
import com.hisense.storemode.R;
import com.hisense.storemode.bean.ShowAQPQAdjustEvent;
import com.hisense.storemode.manager.task.BackgroundTaskManager;
import com.hisense.storemode.receiver.ShowAQPQAdjustReceiver;
import com.hisense.storemode.ui.activity.UpdatePictureActivity;
import com.hisense.storemode.ui.activity.UpdateVideoActivity;
import com.hisense.storemode.ui.dialog.malllogo.MallLogoFragment;
import com.hisense.storemode.utils.AudioUtils;
import com.hisense.storemode.utils.ConstantConfig;
import com.hisense.storemode.utils.FileUtil;
import com.hisense.storemode.utils.LogUtils;
import com.hisense.storemode.utils.PreferenceUtils;
import com.hisense.storemode.utils.StringUtil;
import com.hisense.storemode.utils.ToastUtil;
import com.hisense.storemode.utils.UpdateApkUtil;
import com.hisense.storemode.utils.UsbUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import androidx.leanback.preference.LeanbackPreferenceFragment;
import androidx.preference.Preference;
import androidx.preference.SwitchPreference;

/**
 * Create by xuchongxiang at 2019-05-10 3:14 PM
 */

public class SellerSettingFragment extends LeanbackPreferenceFragment implements Preference.OnPreferenceChangeListener {

    private static final String TAG = "SellerSettingFragment";
    private Context mContext;
    private SwitchPreference mEposSwitch;
    private Preference mBuildInPicture;
    private Preference mAutoPlaySetup;
    private Preference mBuildInVideo;
    private Preference mEposUpdate;
    private Preference mMallLogo;
    private Preference mMallLogoPositionAdjustment;
    private Preference mEposPositionAdjustment;
    private Preference mAQPQAdjust;
    private ShowAQPQAdjustReceiver mKeyReceiver;

    private boolean flag = true;//epos update only click once;
    //    private SwitchPreference mVideoUpdateSwitch;
//    private SwitchPreference mUsbPrioritySwitch;

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
        addPreferencesFromResource(R.xml.seller_settings);
        mEposSwitch = (SwitchPreference) findPreference(getString(R.string.key_epos_switch));
        mBuildInPicture = findPreference(getString(R.string.key_build_in_picture_update));
        mAutoPlaySetup = findPreference(getString(R.string.key_auto_play_setup));
        mBuildInVideo = findPreference(getString(R.string.key_build_in_video_update));
        mEposUpdate = findPreference(getString(R.string.key_epos_update));
        mMallLogo = findPreference(getString(R.string.key_mall_logo));
        mMallLogoPositionAdjustment = findPreference(getString(R.string.key_malllogo_position_adjustment));
        mEposPositionAdjustment = findPreference(getString(R.string.key_epos_position_adjustment));
        mAQPQAdjust = findPreference(getString(R.string.key_aq_pq_adjust));


//        mVideoUpdateSwitch = (SwitchPreference) findPreference(getString(R.string.key_build_in_video_switch));
//        mUsbPrioritySwitch = (SwitchPreference) findPreference(getString(R.string.key_sharepref_usb_res_priority_check));

        mAQPQAdjust.setVisible(false);

        if (ConstantConfig.is4KMode()) {
            mBuildInPicture.setVisible(false);
            mAutoPlaySetup.setVisible(false);
        } else {
            mBuildInPicture.setVisible(true);
            mAutoPlaySetup.setVisible(true);
        }

        mBuildInVideo.setVisible(SeriesUtils.getAutoPlaySetupIsVisibleBooleans()[4]);
        mBuildInPicture.setVisible(SeriesUtils.getAutoPlaySetupIsVisibleBooleans()[3]);
    }

    private void initData() {
        boolean isEposSwitch = (boolean) PreferenceUtils.getInstance().get(ConstantConfig.SHAREPREF_EPOS_CHECK, true);
        if (SeriesUtils.SHOW_EPOS_FUNCTION) {
            mEposSwitch.setChecked(isEposSwitch);
            mEposPositionAdjustment.setVisible(SeriesUtils.SHOW_EPOS_POSITION_ADJUST_FUNCTION);
        } else {
            mEposSwitch.setVisible(false);
            mMallLogo.setVisible(false);
            mEposUpdate.setVisible(false);
            mMallLogoPositionAdjustment.setVisible(false);
            mEposPositionAdjustment.setVisible(false);
        }
//        boolean isBuildInVideoSwitch = (boolean) PreferenceUtils.getInstance().get(ConstantConfig.SHAREPREF_BUILD_IN_VIDEO_CHECK, true);
//        mVideoUpdateSwitch.setChecked(isBuildInVideoSwitch);
//        boolean isUsbPrioritySwitch = (boolean) PreferenceUtils.getInstance().get(ConstantConfig.SHAREPREF_USB_RES_PRIORITY_CHECK, true);
//        mUsbPrioritySwitch.setChecked(isUsbPrioritySwitch);
    }

    private void initListener() {
        mEposSwitch.setOnPreferenceChangeListener(this);
//        mVideoUpdateSwitch.setOnPreferenceChangeListener(this);
//        mUsbPrioritySwitch.setOnPreferenceChangeListener(this);

        EventBus.getDefault().register(this);
        if (SeriesUtils.SPECIAL_AQPQ_ADJUST) {
            LogUtils.d(TAG, "initListener registerKeyReceiver");
            if (mKeyReceiver == null) {
                mKeyReceiver = new ShowAQPQAdjustReceiver();
            }
            IntentFilter filter = new IntentFilter();
            filter.addAction(ConstantConfig.MONITOR_KEY_ACTION);
            mContext.registerReceiver(mKeyReceiver, filter,
                    ConstantConfig.MONITOR_KEY_PERMISSION, null);
        }
    }

    @Subscribe
    public void showAQPQAdjustView(ShowAQPQAdjustEvent event) {
        LogUtils.d(TAG, "showAQPQAdjustView() ShowAQPQAdjustEvent");
        if (!mAQPQAdjust.isShown()) {
            mAQPQAdjust.setVisible(true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (SeriesUtils.SPECIAL_AQPQ_ADJUST) {
            mContext.unregisterReceiver(mKeyReceiver);
            if (mAQPQAdjust.isShown()) {
                AudioUtils.updateLocalStoreModeVolume();
            }
        }
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        String key = preference.getKey();
        if (key.equals(getString(R.string.key_build_in_video_update))) {
            Intent video = new Intent(mContext, UpdateVideoActivity.class);
            video.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(video);
            return true;
        }
        if (key.equals(getString(R.string.key_build_in_picture_update))) {
            Intent picture = new Intent(mContext, UpdatePictureActivity.class);
            picture.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(picture);
            return true;
        }
        if (key.equals(getString(R.string.key_vidaa_feature_demo))) {
//            ToastUtil.showToast("Demo");
            return true;
        }
        if (key.equals(getString(R.string.key_aq_pq_adjust))) {
            AqPqAdjustFragment fragment = new AqPqAdjustFragment();
            getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fl_content, fragment, "AqPqAdjustFragment")
                    .commitAllowingStateLoss();
            return true;
        }
        if (key.equals(getString(R.string.key_mall_logo))) {
            MallLogoFragment fragment = new MallLogoFragment();
            fragment.show(getFragmentManager(), "malllogo");
            return true;
        }
        if (key.equals(getString(R.string.key_auto_play_setup))) {
            AutoPlaySetUpFragment fragment = new AutoPlaySetUpFragment();
            getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fl_content, fragment, "AutoPlaySetUpFragment")
                    .commitAllowingStateLoss();
            return true;
        }
        if (key.equals(getString(R.string.key_epos_position_adjustment))) {
            EposPositionAdjustFragment fragment = new EposPositionAdjustFragment();
            getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fl_content, fragment, "EposPositionAdjustment")
                    .commitAllowingStateLoss();
            return true;
        }
        if (key.equals(getString(R.string.key_malllogo_position_adjustment))) {
            MallLogoPositionAdjustFragment fragment = new MallLogoPositionAdjustFragment();
            getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fl_content, fragment, "MallLogoPositionAdjustFragment")
                    .commitAllowingStateLoss();
            return true;
        }
        if (key.equals(getString(R.string.key_epos_update))) {
            eposUpdate();
            return true;
        }

        return super.onPreferenceTreeClick(preference);

    }

    private void eposUpdate() {
        List<UsbUtil.UsbPathAndLabel> mUsbFilePaths = UsbUtil.getUsbPath();
        if (mUsbFilePaths.size() == 0) {
            ToastUtil.showToast(getString(R.string.usb_not_found));
            return;
        }
        String apkUpdatePath = "";
        for (int i = 0; i < mUsbFilePaths.size(); i++) {
            for (UsbUtil.UsbPathAndLabel usbFile : mUsbFilePaths) {
                // FileUtil.getFilesPathByTypeAndName  mType:pic and video  /  file name:**
                List<String> paths = FileUtil.getFilesPathByTypeAndName(usbFile.mPath, ConstantConfig.APK_TYPE, ConstantConfig.APK_USB_UPDATE_NAME);
                if (paths.size() != 0) {
                    for (String filePath : paths) {
                        apkUpdatePath = filePath;
                        LogUtils.d(TAG, "eposUpdate()  apkUsbPath find apk update file :" + filePath);
                    }
                } else {
                    LogUtils.d(TAG, "eposUpdate()  no find apk update file  ");
                }
            }
        }


        if (StringUtil.isEmpty(apkUpdatePath)) {
            ToastUtil.showToast(String.format(getString(R.string.not_find_update_file_tips), ConstantConfig.APK_USB_UPDATE_NAME));
        } else {
            UpdateApkUtil updateApkUtil = new UpdateApkUtil();
//            updateApkUtil.Upgrade(apkUpdatePath);
//            updateApkUtil.installApkInSilence(apkUpdatePath,"com.hisense.storemode");

            if (flag) {
                flag = false;
                ToastUtil.showToastTime(getString(R.string.epos_updating_tips), 20000, new Handler());
                BackgroundTaskManager.getInstance().startOneTimeworkerRestartStoreMode();

                HandlerThread handlerThread = new HandlerThread(TAG);
                handlerThread.start();
                Handler handler = new Handler(handlerThread.getLooper());
                String finalApkUpdatePath = apkUpdatePath;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        LogUtils.e(TAG, "eposUpdate() update apk for android P start !");
                        updateApkUtil.installP(finalApkUpdatePath);//for android P
                        SeriesUtils.modifyGoogleServiceSwitch(true);
                        PreferenceUtils.getInstance().save(ConstantConfig.SHAREPREF_EPOS_UPDATE, true);//EPOS_UPDATE status

                    }
                });
            }
        }

    }


    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String key = preference.getKey();
        boolean value = (boolean) newValue;

        if (key.equals(getString(R.string.key_epos_switch))) {
            LogUtils.d(TAG, "onPreferenceChange() E-Pos: " + value);
            PreferenceUtils.getInstance().save(ConstantConfig.SHAREPREF_EPOS_CHECK, value);
            return true;
        }
//        if (key.equals(getString(R.string.key_build_in_video_switch))) {
//            PreferenceUtils.getInstance().save(ConstantConfig.SHAREPREF_BUILD_IN_VIDEO_CHECK, value);
//            return true;
//        }
//        if (key.equals(getString(R.string.key_sharepref_usb_res_priority_check))) {
//            PreferenceUtils.getInstance().save(ConstantConfig.SHAREPREF_USB_RES_PRIORITY_CHECK, value);
//            return true;
//        }
        return false;
    }
}
