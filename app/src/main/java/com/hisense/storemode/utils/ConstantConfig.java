package com.hisense.storemode.utils;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.os.SystemProperties;
import android.provider.Settings;
import android.util.Log;

import com.hisense.hianidraw.cache.SeriesUtils;
import com.hisense.storemode.StoreModeApplication;
import com.mediatek.twoworlds.tv.HisenseTvAPI;
import com.mediatek.twoworlds.tv.MtkTvConfig;
import com.mediatek.twoworlds.tv.common.MtkTvConfigType;
import com.mediatek.twoworlds.tv.common.MtkTvConfigTypeBase;

import androidx.annotation.DrawableRes;

import static android.os.Environment.DIRECTORY_MOVIES;
import static android.os.Environment.DIRECTORY_PICTURES;
import static com.hisense.storemode.ui.dialog.malllogo.MallLogoAdapter.SPLIT_FLAG;

//import com.hisense.support.tv.HisenseTvAPI;


/**
 * Created by tianpengsheng on 2019年04月19日 10时52分.
 */
public class ConstantConfig {
    private static final String TAG = "ConstantConfig";
    public static boolean IS_STORE_MODE_BOOT_COMPLETED = false;//for:avoid BOOT_COMPLETED  receiver event logic
    public static boolean IS_UPDATE_OPERATION_ENABLE = true;//update operation enable,true--can update
    public static boolean IS_MAIN_ACTIVITY_INVISIBLE = false;
    public static boolean IS_HAVE_USB_PIC = false;
    public static boolean IS_HAVE_USB_VIDEO = false;
    public static boolean IS_INIT_COUNTDOWN_STOREMODE = false;
    public static String EJECT_STORAGE_PATH = "";

    //series  brand
    public static final String SERIES_US_A6101EU = "6886usa6101eu";
    public static final String SERIES_EM_USU6FUW = "6886emusu6fuw";

    //package name
    public static final String YOUTUBE_PKG_NAME = "com.google.android.youtube.tv";//youtube
    public static final String NETFLIX_PKG_NAME = "com.netflix.ninja";          //netflix
    public static final String LIVE_TV_PKG_NAME = "com.mediatek.wwtv.tvcenter";//signal play
    public static final String STORE_MODE_PKG_NAME = "com.hisense.storemode";//storemode
    public static final String MEDIA_CENTER_PKG_NAME = "com.jamdeo.tv.mediacenter";//mediacenter
    public static final String TV_SETTINGS_PKG_NAME = "com.android.tv.settings";//settings
    public static final String LIVE_TV_SETTING_ACTIVITY = "com.mediatek.wwtv.setting.LiveTvSetting";//settings activity

    //activity state
    public static final String ACTIVITY_STATE = "com.hisense.tv.ACTIVITY_STATE";
    public static final String RUNNING_PACKAGE_CATEGORY_5660 = "mtktvapi.agent";
    public static final String RUNNING_PACKAGE_CATEGORY_6886 = "com.hisense.tv.category.ACTIVITY_STATE";

    public static final String ACTIVITY_STATE_PERMISSION = "com.hisense.tv.permission.ACTIVITY_STATE";

    //key system property
    public static final String KEY_BOARD_SYSTEM_PROPERTY = "ro.product.board";//ps:[MSD6886]
    public static final String KEY_PRODUCT_NAME_SYSTEM_PROPERTY = "ro.product.hisense.model";//ps:[HU55U6FUW]
    public static final String KEY_BUILD_SOFTVERSION = "ro.build.hisense.softversion";//V0000.01.00A
    //public static final String KEY_BUILD_SOFTVERSION2 = "ro.hisense.build.softversion";//V0000.01.00A
    public static final String KEY_RECEVIER_SWITCH_SYSTEM_PROPERTY = "persist.sys.vt.storemode";
    public static final String mCurrentBoard = PropertyUtils.get(KEY_BOARD_SYSTEM_PROPERTY, "default");
    public static final String mProductName = PropertyUtils.get(KEY_PRODUCT_NAME_SYSTEM_PROPERTY, "default");
    public static final String[] mBuildSoftVersion = PropertyUtils.get(KEY_BUILD_SOFTVERSION, "V0000.01.001").split("\\.");

    //monitor key
    public static final String MONITOR_KEY_ACTION = "com.hisense.android.tv.monitorkeys";
    public static final String MONITOR_KEY_PERMISSION = "com.hisense.permission.monitorkeys";

    //content uri
    public static final String CONTENT_LIVE_TV_TABLE_URI = "content://com.hisense.livetv.mixbar.provider/table";

    // for intent transfer key
//    public static String PLAY_TYPE = "play_type";
    public static final String PLAY_TYPE_VIDEO = "play_type_video";
    public static final String PLAY_TYPE_PIC = "play_type_pic";
    public static final String PLAY_TYPE_SIGNAL = "play_type_signal";

    public static final String PIC_TYPE = "png-jpg";//support play picture format, "-" as delimiter
    public static final String VIDEO_TYPE = "mp4-mkv";//support play video format, "-" as delimiter
    public static final String APK_TYPE = "apk";
    public static final String BACKGROUND_PIC_NAME_PREFIX = "storemode_";//polling background picture
    public static final String BUILD_IN_PATH = "vdow/storemode";//build-in path

    public static final String RESET_PALY_POLICY = "reset_paly_policy";// need reset play policy
    public static final String CRASH_JUST_NOW = "crash_just_now";// crash just now
    public static final String RETAIL_MODE_DB_FIELD = "use_mode";// 1-store,2-store with video,0-home
    public static final String RESTRICT_KEY_FLAG = "restrict_flag";// 1-restrict some key,0-don't restrict any key
    public static final String MODE_STORE_MODE = "StoreMode";
    public static final String MODE_HOME_MODE = "HomeMode";
    public static final String MODE_STORE_4K_MODE = "Store4KMode";


    public static final String FAST_CRASH_COUNT = "fast_crash_count";
    public static final String FAST_CRASH_CURRENT_TIME = "fast_crash_current_time";
    public static final String SELLER_SETTINGS = "g_system__user_mode_seller_setting";

    //    public static  String FILE_PATH = "file_path";
//    public static  String FILE_PATH_RESOURCE_ID = "file_path_resource_id";
    public static String FILE_PATH = "";
    public static int FILE_PATH_RESOURCE_ID;
    public static String PLAY_TYPE = "";

    public static final String SYSTEM_USER_MODE_RESET = "g_system__user_mode_reset";
    public static final String PLAY_PHOTO_FLAG = "g_consumer_video__play_photo_flag";//upgrade_pq_for_photo
    public static final String COUNTRY_CODE_FLAG = "g_system__country";
    public static final int AUSTRILIA_COUNTRY_CODE = 51;
    public static final int STD_BACKLIGHT = 50;

    //------start-------SharedPreference key----------
    public static final String SHAREPREF_GOOGLE_PROCESS_FLAG = "sharepref_google_process_flag";//1 is google process should enable,0 is should disable
    public static final String SHAREPREF_EPOS_CHECK = "sharepref_epos_check";
    public static final String SHAREPREF_MALL_LOGO_CHECK = "sharepref_mall_logo_check";
    public static final String SHAREPREF_USB_VIDEO_CHECK = "sharepref_usb_video_check";
    public static final String SHAREPREF_USB_PIC_CHECK = "sharepref_usb_pic_check";
    public static final String SHAREPREF_SIGNAL_CHECK = "sharepref_signal_check";
    public static final String SHAREPREF_BUILD_IN_PIC_CHECK = "sharepref_build_in_pic_check";
    public static final String SHAREPREF_BUILD_IN_VIDEO_CHECK = "sharepref_build_in_video_check";
    public static final String SHAREPREF_USB_RES_PRIORITY_CHECK = "sharepref_usb_res_priority_check";

    public static final String SHAREPREF_SIGNAL_PALY_TIME = "sharepref_signal_paly_time";
    public static final String SHAREPREF_EACH_PIC_SHOW_TIME = "sharepref_each_pic_show_time";
    public static final String SHAREPREF_AQ_PQ_RESET_TIME = "sharepref_aq_pq_reset_time";
    public static final String SHAREPREF_EPOS_AND_MALLLOGO_COUNTDOWN_TIME = "sharepref_epos_and_malllogo_countdown_time";
    public static final String SHAREPREF_EPOS_UPDATE = "sharepref_epos_update";

    public static final String SHAREPREF_NO_KEY_INPUT_TIME = "sharepref_youtube_no_key_input_time";
    public static final String SHAREPREF_ADD_RESTRICT_KEY = "sharepref_add_restrict_key";

    public static final String SHAREPREF_PLAY_POLICY = "sharepref_play_policy";
    //    public static final String SHAREPREF_VIDAA_KEY_FEATURE_CHECK = "sharepref_vidaa_key_feature_check";
    public static final String SHAREPREF_PLAY_POLICY_INDEX = "sharepref_play_policy_index";
    public static final String SHAREPREF_MALL_LOGOS = "sharepref_mall_logos";
    public static final String SHAREPREF_SELECTED_MALL_LOGO = "sharepref_selected_mall_logo";
    public static final String SHAREPREF_SELECTED_MALL_LOGO_CUR_SIZE = "sharepref_selected_mall_logo_cur_size";
    public static final String SHAREPREF_SELECTED_MALL_LOGO_MAX_SIZE = "sharepref_selected_mall_logo_max_size";
    public static final String SHAREPREF_IS_INITIALIZED = "sharepref_is_initialized";

    //epos position
    public static final String SHAREPREF_EPOS_POSITION = "sharepref_epos_position";
    public static final String SHAREPREF_MALL_LOGO_POSITION = "sharepref_mall_logo_position";

    // audio value key
    public static final String SHAREPREF_HOMEMODE_AUDIO_VOLUME = "sharepref_homemode_audio_volume";
    public static final String SHAREPREF_STOREMODE_AUDIO_VOLUME = "sharepref_storemode_audio_volume";
    //------end-------sharedPreference------------


    //------ start-- maybe need modify per app version
    //    public static final String VIDEO_BUILD_IN_PATH = "vendor/vdow/storemode/video.mp4";
    public static final String VIDEO_USB_UPDATE_NAME = "StoreModeDemo.mkv"; //usb default update video name
    public static final String VIDEO_USB_UPDATE_NAME2 = "StoreModeDemo.mp4"; //usb default update video name
    public static final String APK_USB_UPDATE_NAME = "StoreMode.apk"; //usb default update video name
    public static final int MALL_LOGO_ADD_MAX_SIZE = 20;
    public static final int MALL_LOGO_DEFAULT_ALLOW_SELECT_MAX_SIZE = 20;
    public static final int BUIDL_IN_PIC_UPDATE_MAX_NUMBER = 6;

    public static final int AQ_PQ_RESET_TIME_DEFAULT = 10 * 60;//unit is second
    public static final int EACH_PIC_SHOW_TIME_DEFAULT = 10;//unit is second
    public static final int SIGNAL_PLAY_TIME_DEFAULT = 5 * 60;//unit is second
    public static final int EPOS_AND_MALLLOGO_COUNTDOWN_TIME_DEFAULT = 15;//unit is second
    public static final int NO_KEY_INPUT_TIME_DEFAULT = 3 * 60;//unit is second
    public static final int ADD_RESTRICT_KEY_TIME_DEFAULT = 5 * 60;//unit is second

    public static final String PICTURE_BUILD_IN_PATH = StoreModeApplication.sContext.getExternalFilesDir(DIRECTORY_PICTURES).getPath();
    public static final String VIDEO_BUILD_IN_UPDATE_PATH = StoreModeApplication.sContext.getExternalFilesDir(DIRECTORY_MOVIES).getPath();
    public static final String VIDEO_BUILD_IN_UPDATE_FILE_PATH = StoreModeApplication.sContext.getExternalFilesDir(DIRECTORY_MOVIES).getPath() + "/video.mp4";

    //------ end-- maybe need modify per app version


    //------start----------need debug dialog modify ---
    public static int AQ_PQ_RESET_TIME = (Integer) PreferenceUtils.getInstance().get(SHAREPREF_AQ_PQ_RESET_TIME, AQ_PQ_RESET_TIME_DEFAULT);//second
    public static int SIGNAL_PLAY_TIME = (Integer) PreferenceUtils.getInstance().get(SHAREPREF_SIGNAL_PALY_TIME, SIGNAL_PLAY_TIME_DEFAULT);//second
    public static int EACH_PIC_SHOW_TIME = (Integer) PreferenceUtils.getInstance().get(SHAREPREF_EACH_PIC_SHOW_TIME, EACH_PIC_SHOW_TIME_DEFAULT);//second
    public static int EPOS_MALLLOGO_COUNTDOWN_TIME = (Integer) PreferenceUtils.getInstance().get(SHAREPREF_EPOS_AND_MALLLOGO_COUNTDOWN_TIME, EPOS_AND_MALLLOGO_COUNTDOWN_TIME_DEFAULT);//second
    public static boolean EPOS_UPDATE = (Boolean) PreferenceUtils.getInstance().get(SHAREPREF_EPOS_UPDATE, false);//default false

    public static int NO_KEY_INPUT_TIME = (Integer) PreferenceUtils.getInstance().get(SHAREPREF_NO_KEY_INPUT_TIME, NO_KEY_INPUT_TIME_DEFAULT);//second
    public static int ADD_RESTRICT_KEY_TIME = (Integer) PreferenceUtils.getInstance().get(SHAREPREF_ADD_RESTRICT_KEY, ADD_RESTRICT_KEY_TIME_DEFAULT);//second
    public static int MALL_LOGO_ALLOW_SELECT_MAX_SIZE = (Integer) PreferenceUtils.getInstance().get(SHAREPREF_SELECTED_MALL_LOGO_MAX_SIZE, MALL_LOGO_DEFAULT_ALLOW_SELECT_MAX_SIZE);

    public static boolean LOG_SWITCH = true;//BuildConfig.DEBUG;
    public static int EPOS_LAYOUT_POSITION = (int) PreferenceUtils.getInstance().get(ConstantConfig.SHAREPREF_EPOS_POSITION, 0);// left-0  top-1  right-2  bottom-3
    public static int MALL_LOGO_POSITION = (int) PreferenceUtils.getInstance().get(ConstantConfig.SHAREPREF_MALL_LOGO_POSITION, 1);// left-0  top-1  right-2  bottom-3
    //------end------------need debug dialog modify ---


    //------start player type value
    /**
     * // 1:usb video
     * // 2:build-in video
     * // 3:usb pic
     * // 4:build-in pic
     * // 5:signal
     * // 6:apk drawable pic---if no build-in pic,pic default show
     * // 7:all pic res
     */
    public static final int TYPE_VALUE_USB_VIDEO = 1;
    public static final int TYPE_VALUE_BUILD_IN_VIDEO = 2;
    public static final int TYPE_VALUE_USB_PIC = 3;
    public static final int TYPE_VALUE_BUILD_IN_PIC = 4;
    public static final int TYPE_VALUE_SIGNAL = 5;
    public static final int TYPE_VALUE_APK_DRAWABLE_PIC = 6;
    public static final int TYPE_VALUE_ALL_PIC_RES = 7;

    //------end player type value

    //for select pic self from usb
    public static final int SELECT_PIC_FROM_USB_REQUEST_CODE = 10011;
    public static final String SELECT_PIC_PATH_KEY = "pic_path";

    public static void initSharedPreference() {//need depend on menu function
        //only init once
        if (!(Boolean) PreferenceUtils.getInstance().get(SHAREPREF_IS_INITIALIZED, false)) {
            boolean[] defaultChecks = SeriesUtils.getAutoPlaySetupDefaultCheckBooleans();

            PreferenceUtils.getInstance().save(SHAREPREF_IS_INITIALIZED, true);
            PreferenceUtils.getInstance().save(RESET_PALY_POLICY, true);
            //if usb priority check is false,no need refresh play policy  immediately
            PreferenceUtils.getInstance().save(SHAREPREF_USB_RES_PRIORITY_CHECK, false);//usb video resourse priority

            LogUtils.d(TAG, "initSharedPreference(), getUserMode(): " + getUserMode());
//            if (!is4KMode()) {//2:4k_mode  not 4k mode,all selected
            PreferenceUtils.getInstance().save(SHAREPREF_USB_VIDEO_CHECK, defaultChecks[0]);//usb video
            PreferenceUtils.getInstance().save(SHAREPREF_USB_PIC_CHECK, defaultChecks[1]);//usb pic
            PreferenceUtils.getInstance().save(SHAREPREF_SIGNAL_CHECK, defaultChecks[2]);//signal
            PreferenceUtils.getInstance().save(SHAREPREF_BUILD_IN_PIC_CHECK, defaultChecks[3]);// build-in pic
            PreferenceUtils.getInstance().save(SHAREPREF_BUILD_IN_VIDEO_CHECK, defaultChecks[4]);//build-in video
//            }

            PreferenceUtils.getInstance().save(SHAREPREF_MALL_LOGO_CHECK, true);//logo is show
            PreferenceUtils.getInstance().save(SHAREPREF_EPOS_CHECK, SeriesUtils.SHOW_EPOS_FUNCTION);//epos is show


            PreferenceUtils.getInstance().save(SHAREPREF_EACH_PIC_SHOW_TIME, EACH_PIC_SHOW_TIME_DEFAULT);//each pic show time
            PreferenceUtils.getInstance().save(SHAREPREF_SIGNAL_PALY_TIME, SIGNAL_PLAY_TIME_DEFAULT);//signal play time
            PreferenceUtils.getInstance().save(SHAREPREF_AQ_PQ_RESET_TIME, AQ_PQ_RESET_TIME_DEFAULT);//aq-pq reset time
            PreferenceUtils.getInstance().save(SHAREPREF_EPOS_AND_MALLLOGO_COUNTDOWN_TIME, EPOS_AND_MALLLOGO_COUNTDOWN_TIME_DEFAULT);//epos and malllogo countdown time

            PreferenceUtils.getInstance().save(SHAREPREF_NO_KEY_INPUT_TIME, NO_KEY_INPUT_TIME_DEFAULT);//youtube_no_key_input_time
            PreferenceUtils.getInstance().save(SHAREPREF_ADD_RESTRICT_KEY, ADD_RESTRICT_KEY_TIME_DEFAULT);//frame detect time

            PreferenceUtils.getInstance().save(SHAREPREF_SELECTED_MALL_LOGO_MAX_SIZE, MALL_LOGO_DEFAULT_ALLOW_SELECT_MAX_SIZE);

//            if (SeriesUtils.DEFAULT_MALL_LOGO != -1) {
//                PreferenceUtils.getInstance().save(ConstantConfig.SHAREPREF_SELECTED_MALL_LOGO, getStringUriFromDrawable(SeriesUtils.DEFAULT_MALL_LOGO));
//            }

            if (StringUtil.isNotEmpty(SeriesUtils.DEFAULT_MALL_LOGO)) {
                String[] mallLogoStrings = SeriesUtils.DEFAULT_MALL_LOGO.split("-");
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < mallLogoStrings.length; i++) {
                    LogUtils.d(TAG, "default_mall_logo   malllogostrings[i]:" + mallLogoStrings[i]);
                    String pic = getStringUriFromDrawable(Integer.parseInt((mallLogoStrings[i])));
                    LogUtils.d(TAG, "default_mall_logo   picstrings:" + pic);
                    sb.append(pic);
                    sb.append(SPLIT_FLAG);

                }
                PreferenceUtils.getInstance().save(ConstantConfig.SHAREPREF_SELECTED_MALL_LOGO, sb.toString());
            }

        }
        SeriesUtils.initSpecialSeriesFunction();
    }

    //for StoreMode with video  only paly built-in video
//    public static void setPlayPolicyForStoreMode4k() {
//        PreferenceUtils.getInstance().save(RESET_PALY_POLICY, true);
//
//        PreferenceUtils.getInstance().save(SHAREPREF_USB_PIC_CHECK, false);//usb pic
//        PreferenceUtils.getInstance().save(SHAREPREF_USB_VIDEO_CHECK, false);//usb video
//        PreferenceUtils.getInstance().save(SHAREPREF_BUILD_IN_VIDEO_CHECK, true);//build-in video
//        PreferenceUtils.getInstance().save(SHAREPREF_BUILD_IN_PIC_CHECK, false);// build-in pic
//        PreferenceUtils.getInstance().save(SHAREPREF_SIGNAL_CHECK, false);//signal
//    }

    public static int getFacCountryCode() {
        int facCCode = MtkTvConfig.getInstance().getConfigValue(MtkTvConfigTypeBase.CFG_FACTORY_FAC_TV_CPUNTRY);

        LogUtils.d(TAG, "current factory country code. facCCode: " + facCCode);

        return facCCode;
    }

    //exit epg
    public static void exitEPG() {
        boolean isEpg = SystemProperties.getBoolean("sys.hisense.tv.epg", false);
        LogUtils.d(TAG, "exitEPG, isEpg:" + isEpg);
        if (isEpg) {
            Intent intent = new Intent("com.hisense.tv.epg");
            intent.setPackage(ConstantConfig.LIVE_TV_PKG_NAME);
            intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
            StoreModeApplication.sContext.sendBroadcast(intent);
        }
    }

    public static boolean isOnlySignalChecked() {
//        boolean isPicCheck = (boolean) PreferenceUtils.getInstance().get(SHAREPREF_USB_PIC_CHECK, true);//usb pic
//        boolean isVideoCheck = (boolean) PreferenceUtils.getInstance().get(SHAREPREF_USB_VIDEO_CHECK, true);//usb video
        boolean isBuiltInVideoCheck = (boolean) PreferenceUtils.getInstance().get(SHAREPREF_BUILD_IN_VIDEO_CHECK, true);//build-in video
        boolean isBuiltInPicCheck = (boolean) PreferenceUtils.getInstance().get(SHAREPREF_BUILD_IN_PIC_CHECK, true);// build-in pic
        boolean isSignalCheck = (boolean) PreferenceUtils.getInstance().get(SHAREPREF_SIGNAL_CHECK, true);//signal

        return !(IS_HAVE_USB_PIC || IS_HAVE_USB_VIDEO) && !isBuiltInVideoCheck && !isBuiltInPicCheck && isSignalCheck;
    }

    public static String getStringUriFromDrawable(@DrawableRes int id) {
        Resources resources = StoreModeApplication.sContext.getResources();
        String path = ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + resources.getResourcePackageName(id) + "/"
                + resources.getResourceTypeName(id) + "/"
                + resources.getResourceEntryName(id);
        return path;
    }

    public static void setUserMode(String tag) {
        switch (tag) {
            case MODE_STORE_MODE:
                PreferenceUtils.getInstance().save(RESET_PALY_POLICY, true);
                SeriesUtils.setUserMode(1);
                break;
            case MODE_HOME_MODE:
                SeriesUtils.setUserMode(0);
                break;
            case MODE_STORE_4K_MODE:
//                setPlayPolicyForStoreMode4k();
                PreferenceUtils.getInstance().save(RESET_PALY_POLICY, true);
                SeriesUtils.setUserMode(2);
                break;
        }
    }

    public static boolean isFactoryMode() {//factory mode
        LogUtils.d(TAG, "isFactoryMode() monitor");
        boolean isFac = HisenseTvAPI.getInstance(StoreModeApplication.sContext).isFactoryMode();
        return isFac;
    }

    public static boolean isAgingMode() {// old mode
        LogUtils.d(TAG, "isAgingMode() monitor");
        boolean isAge = HisenseTvAPI.getInstance(StoreModeApplication.sContext).isAgingMode();
        return isAge;
    }

    //start play photo value should 1; stop play photo value should 0;
    public static void setPlayPhotoFlag(int value) {
        LogUtils.d(TAG, "setPlayPhotoFlag    value:" + value);
        MtkTvConfig.getInstance().setConfigValue(PLAY_PHOTO_FLAG, value);
    }

    //BroadcastReceiver Monitor Switch   1:open  0:close
    public static void setReceiverMonitoreSwitch(int value) {
        LogUtils.d(TAG, "setReceiverMonitoreSwitch(), value: " + value);
        MtkTvConfig.getInstance().setConfigValue(SELLER_SETTINGS, value);
        boolean isSucc = HisenseTvAPI.getInstance(StoreModeApplication.sContext).setStoreMode(value);
        LogUtils.d(TAG, "setReceiverMonitoreSwitch(), isSucc: " + isSucc);
        if (isSucc) {
            switch (value) {
                case 0:
                    PropertyUtils.set(KEY_RECEVIER_SWITCH_SYSTEM_PROPERTY, "false");
                    break;
                case 1:
                    PropertyUtils.set(KEY_RECEVIER_SWITCH_SYSTEM_PROPERTY, "true");
                    break;
                default:
                    PropertyUtils.set(KEY_RECEVIER_SWITCH_SYSTEM_PROPERTY, "false");
                    break;
            }
        }
//        else {
//            setReceiverMonitoreSwitch(value);
//        }

//        int relValue = MtkTvConfig.getInstance().setConfigValue("g_system__user_mode_select", value);
//        LogUtils.d(TAG, "g_system__user_mode_select  relavalue:" + relValue + "    switch_property:" + PropertyUtils.get(KEY_RECEVIER_SWITCH_SYSTEM_PROPERTY, "default"));

    }

    public static boolean isAuCountryCode() {
        int countryCode = Settings.Global.getInt(StoreModeApplication.sContext.getContentResolver(), COUNTRY_CODE_FLAG, -1);
        LogUtils.d(TAG, "isAuCountryCode(), auCode should is :51, curr countryCode: " + countryCode);
        if (countryCode == AUSTRILIA_COUNTRY_CODE && countryCode != -1) {
            return true;
        }
        return false;
    }

    public static boolean isExceedBacklight() {
        int disp_backlight = MtkTvConfig.getInstance().getConfigValue(MtkTvConfigTypeBase.CFG_DISP_DISP_DISP_BACK_LIGHT);
        LogUtils.d(TAG, "isExceedBacklight(), STD_BACKLIGHT should is :50, curr disp_backlight: " + disp_backlight);
        if (disp_backlight > STD_BACKLIGHT) {
            return true;
        }
        return false;
    }

    public static String getInputresolution() {
        int[] values = HisenseTvAPI.getInstance(StoreModeApplication.sContext).getConfigIntArrayValue(MtkTvConfigType.CFG_DISP_DISP_VIDEO_FORMAT, 4);
        if (values == null || values.length < 4) {
            Log.d(TAG, "getInputresolution: values == null || values.length < 4");
            return "";
        }
        if (values[1] == 0) return "";
        StringBuilder builder = new StringBuilder();
        builder.append(values[1]).append('*').append(values[2])
                .append(values[3] == 1 ? 'p' : 'l').append('@')
                .append(values[0]).append("Hz");
        Log.d(TAG, "getInputresolution: builder.toString()---->" + builder.toString());
        return builder.toString();
    }

    /**
     * 0-home mode
     * 1-store mode
     * 2-4k mode
     */
    public static int getUserMode() {
        int retailModeEnable = Settings.Global.getInt(StoreModeApplication.sContext.getContentResolver(),
                RETAIL_MODE_DB_FIELD, -1);
        LogUtils.d(TAG, "getUserMode() current store mode value: " + retailModeEnable);
        return retailModeEnable;
    }

    public static boolean is4KMode() {
        return getUserMode() == 2;
    }

    // true --store mode
    public static boolean isStoreMode() {
        int retailModeEnable = Settings.Global.getInt(StoreModeApplication.sContext.getContentResolver(),
                RETAIL_MODE_DB_FIELD, -1);
        LogUtils.d(TAG, "isStoreMode() current store mode value: " + retailModeEnable);
        return retailModeEnable == 1 || retailModeEnable == 2;
    }

    //0 ----finished  1 ----running
    public static boolean isFTEFinish() {
        int inFTE = Settings.Secure.getInt(StoreModeApplication.sContext.getContentResolver(), "tv_user_setup_complete", 0);
        return inFTE == 1;
    }

    public static Boolean isLiveTvRunningTop() {
        return false;
//        String packName = PackageUtil.getTopAppPkgName();
//        return LIVE_TV_PKG_NAME.equals(packName);
    }

    public static Boolean isMediaCenterRunningTop() {
        String packName = PackageUtil.getTopAppPkgName();
        return MEDIA_CENTER_PKG_NAME.equals(packName);
    }

    public static Boolean isStoreModeTop() {
        String packName = PackageUtil.getTopAppPkgName();
        return STORE_MODE_PKG_NAME.equals(packName);
    }


    public static String getRunningPackageCategory() {
        String category = ConstantConfig.RUNNING_PACKAGE_CATEGORY_6886;
        switch (mCurrentBoard) {
            case "MT5660":
                category = ConstantConfig.RUNNING_PACKAGE_CATEGORY_5660;
                break;
            case "MSD6886"://6886 us
                category = ConstantConfig.RUNNING_PACKAGE_CATEGORY_6886;
                break;
        }
        return category;
    }

    public static String getCurrentVideoBuildInPath() {
        String path = "vdow/storemode/video.mp4";
//        switch (mCurrentBoard) {
//            case "MT5660":
//                path = "vdow/storemode/video.mp4";
//                break;
//            case "MSD6886"://6886 us
//                path = "vendor/vdow/storemode/video.mp4";
//                break;
//        }
//        LogUtils.d(TAG, "getCurrentPlatform() ro.product.board  value:" + mCurrentBoard + "  built-in videoPlay path:" + path);
        return path;
    }

    public static String getCurrentPlatform() {
        String currentPlatform = PLATFORM_6886;
        switch (mCurrentBoard) {
            case "MT5660":
                currentPlatform = PLATFORM_5660;
                break;
            case "MSD6886":
                currentPlatform = PLATFORM_6886;
                break;
        }
        LogUtils.d(TAG, "getCurrentPlatform() ro.product.board  value:" + mCurrentBoard + "  currentPlatform:" + currentPlatform);
        return currentPlatform;
    }

    //  unit:ms
    public static void setScreenSaverTime(long time) {
        Settings.System.putLong(StoreModeApplication.sContext.getContentResolver(),
                Settings.System.SCREEN_OFF_TIMEOUT, time);

    }

    public final static String PLATFORM_6886 = "6886";
    public final static String PLATFORM_5660 = "5660";
    public final static String PLANTFORM_NORMAL_PICTURE = "normal_picture";
    public final static String PLANTFORM_4K_PICTURE = "4k_picture";

    public static final String VIDEO_STATE_ERROR = "videoState.error";
    public static final String VIDEO_STATE_OK = "videoState.ok";
    public static final String VIDEO_STATE_NOT_FOUND = "VideoState.notFound";

    /*** obtain TV singal ***/
    public static final int DB_AIR_SVLID = 1;
    public static final int DB_CAB_SVLID = 2;
    public static final int DB_SAT_SVLID = 3;
    public static final int DB_SAT_PRF_SVLID = 4;
    public static final int DB_CI_PLUS_SVLID_AIR = 5;
    public static final int DB_CI_PLUS_SVLID_CAB = 6;
    public static final int DB_CI_PLUS_SVLID_SAT = 7;
    /*tunner mode value:0:T,1:C,2:S,3:used by set general S in wizard and menu*/
    public static final int DB_AIR_OPTID = 0;
    public static final int DB_CAB_OPTID = 1;
    public static final int DB_SAT_OPTID = 2;
    public static final int DB_GENERAL_SAT_OPTID = 3;///maybe used by set tuner mode,but should no be used by get tuner mode

    public static final int BRDCST_TYPE_ATV = 1;
    public static final int BRDCST_TYPE_DTV = 0;
    public static final String RESOURCE_MANAGER = "X-tv-resource-manager-client-priority";
    public static final String OUT_PATH = "X-tv-out-path";
    public static final String OUTPUT_VIDEO_MAIN = "OUTPUT_VIDEO_MAIN";
    public static final String VIDEO_DECODER = "X-tv-assign-software-video-decoder";
    public static final String AUDIO_DECODER = "X-tv-assign-software-audio-decoder";
    public static final int STATE_ERROR = -1;
    public static final int STATE_IDLE = 0;
    public static final int STATE_PREPARING = 1;
    public static final int STATE_PREPARED = 2;
    public static boolean isUsbResourcePlaying = false;
    public static boolean isUsbRejecting = false;
    public static String currentPlayPath;


}
