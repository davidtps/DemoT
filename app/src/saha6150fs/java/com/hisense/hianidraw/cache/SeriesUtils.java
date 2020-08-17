package com.hisense.hianidraw.cache;

import android.app.ActivityManager;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.provider.Settings;

import com.google.gson.Gson;
import com.hisense.avmiddleware.AvMiddleWareManager;
import com.hisense.hianidraw.AniDrawable;
import com.hisense.hianidraw.draw.DrawHelper;
import com.hisense.hianidraw.json.hisense838.Root;
import com.hisense.storemode.R;
import com.hisense.storemode.StoreModeApplication;
import com.hisense.storemode.manager.epos.EPosManager;
import com.hisense.storemode.manager.task.BackgroundTaskManager;
import com.hisense.storemode.utils.ConstantConfig;
import com.hisense.storemode.utils.LogUtils;
import com.hisense.storemode.utils.PreferenceUtils;
import com.hisense.storemode.utils.PropertyUtils;
import com.mediatek.twoworlds.tv.MtkTvConfig;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by tianpengsheng on 2019年10月12日 17时18分.
 */
public class SeriesUtils {

    private static final String TAG = "SeriesUtils";
    public static final int EPOS_MIN_HEIGHT = 144;//px
    public static final int EPOS_MIN_WIDTH = 349;//299;//px
    public static final boolean SHOW_EPOS_FUNCTION = true;//true  show  ;false don't show epos function
    public static final boolean SHOW_EPOS_POSITION_ADJUST_FUNCTION = false;//true  show  ;false don't show epos position function
    public static final boolean OLED_SERIES = false;//true  no usb pic and built-in pic function
    public static final int UPDATE_VIDEO_FILE_MAX_SIZE = 450;//unit is M
    public static final long MPERIODTIME_JSON = 96600L;
    public static final int EPOS_LEFT_MARGIN = 50;//px
    public static final int DELAY_START_STOREMODE = 40000;//ms
    public static final boolean EPOS_PLAY_REPEAT = false;//epos play repeat
    public static boolean DEFAULT_PLAY_SOURECE_BUILT_IN_VIDEO = false;//  没有选择任何资源时，true 默认播放内置视频,false 默认播放内置图片
    private static boolean isSupportNewAqPqJar = true;// is support new aqpq jar
    public static int DEFAULT_AUDIO_VOLUME = -1;   //default audio volume ; -1 means don't set default volume
    public static boolean SPECIAL_AQPQ_ADJUST = false;   //special_aqpq_adjust . true open this function;false close this function;
    public static final boolean SELECT_PIC_SELF_FROM_USB = false;//true:select mallLogo picture by self ;false select mallLogo picture by DMP

    //default mall logo
    public static final String DEFAULT_MALL_LOGO = "";//if no default set value :"",split:-  ps:R.drawable.soccer_1 + "-" + R.drawable.soccer_2

    //apk local R.drawable.* resource
    public static int[] LOCAL_DRAWABLE_RESOURCE = new int[]
            {
                    R.drawable.soccer_1, R.drawable.soccer_2,
                    R.drawable.soccer_4, R.drawable.soccer_5
            };

    //left--0  top--1  right--2  bottom--3
    public static boolean[] getEposPositionBooleans() {
        boolean[] arr = new boolean[]{true, false, true, false};
        return arr;
    }

    //left--0  top--1  right--2  bottom--3
    public static boolean[] getMallLogoPositionBooleans() {
        boolean[] arr = new boolean[]{false, true, false, true};
        return arr;
    }


    //usb video/ usb pic/ signal/ build pic /build video (default isCheck)
    public static boolean[] getAutoPlaySetupDefaultCheckBooleans() {
        boolean[] arr = new boolean[]{true, true, true, true, true};
        return arr;
    }

    //isVisible    usb video/ usb pic/ signal/ build pic /build video
    public static boolean[] getAutoPlaySetupIsVisibleBooleans() {
        boolean[] arr = new boolean[]{true, true, true, true, true};
        return arr;
    }

    /**
     * 0,1,2
     *
     * @param modeValue
     */
    public static void setUserMode(int modeValue) {
        if (isSupportNewAqPqJar) {
            AvMiddleWareManager.setUserMode(modeValue);
        }
        Settings.Global.putInt(StoreModeApplication.sContext.getContentResolver(),
                ConstantConfig.RETAIL_MODE_DB_FIELD, modeValue);


    }

    /**
     * reset ap pq
     */
    public static void resetAQPQ() {
        if (isSupportNewAqPqJar) {
            AvMiddleWareManager.resetStoreSetting(1);
        } else {
            MtkTvConfig.getInstance().setConfigValue(ConstantConfig.SYSTEM_USER_MODE_RESET, 1);
        }
    }

    /**
     * back up AQPQ
     */
    public static void backupStoreSetting() {
    }

    public static void initSpecialSeriesFunction() {
        int epos_position = (int) PreferenceUtils.getInstance().get(ConstantConfig.SHAREPREF_EPOS_POSITION, 0);
        int malllogo_position = (int) PreferenceUtils.getInstance().get(ConstantConfig.SHAREPREF_MALL_LOGO_POSITION, 1);


        if (epos_position == 1 || epos_position == 3) {
            ConstantConfig.EPOS_LAYOUT_POSITION = 0;
            PreferenceUtils.getInstance().save(ConstantConfig.SHAREPREF_EPOS_POSITION, 0);//left
        }
        if (malllogo_position == 0 || malllogo_position == 2) {
            ConstantConfig.MALL_LOGO_POSITION = 1;
            PreferenceUtils.getInstance().save(ConstantConfig.SHAREPREF_MALL_LOGO_POSITION, 1);//top
        }

    }

    //aoto transform epos postion
    public static void autoTransformEposPosition() {
        int epos_position = (int) PreferenceUtils.getInstance().get(ConstantConfig.SHAREPREF_EPOS_POSITION, 0);
        if (epos_position == 0) {//left
            ConstantConfig.EPOS_LAYOUT_POSITION = 2;
            PreferenceUtils.getInstance().save(ConstantConfig.SHAREPREF_EPOS_POSITION, 2);//right
        } else if (epos_position == 2) {//right
            ConstantConfig.EPOS_LAYOUT_POSITION = 0;
            PreferenceUtils.getInstance().save(ConstantConfig.SHAREPREF_EPOS_POSITION, 0);//left
        }

        EPosManager.getInstance().removeEposAndLogo();
        BackgroundTaskManager.getInstance().startEPosTask();
    }

    /**
     * false means disable
     *
     * @param enable
     */
    public static void modifyGoogleServiceSwitch(boolean enable) {
        LogUtils.d(TAG, "modifyGoogleServiceSwitch()  enable:" + enable);
        String gmsPackage = "com.google.android.gms";
        String googleVideoPackage = "com.google.android.videos";
//        String googlePlayStorePackage = "com.android.vending";
        if (enable) {
            modifyProcessLimit(-1);
            StoreModeApplication.sContext.getPackageManager().setApplicationEnabledSetting(gmsPackage,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED, 0);
            StoreModeApplication.sContext.getPackageManager().setApplicationEnabledSetting(googleVideoPackage,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED, 0);
//            StoreModeApplication.sContext.getPackageManager().setApplicationEnabledSetting(googlePlayStorePackage,
//                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED, 0);
        } else {
            modifyProcessLimit(0);
            StoreModeApplication.sContext.getPackageManager().setApplicationEnabledSetting(gmsPackage,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER, 0);
            StoreModeApplication.sContext.getPackageManager().setApplicationEnabledSetting(googleVideoPackage,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER, 0);
//            StoreModeApplication.sContext.getPackageManager().setApplicationEnabledSetting(googlePlayStorePackage,
//                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER, 0);
        }
    }

    public static boolean isGoogleProcessEnable() {//true  is enable status;false is disable status;
        String gmsPackage = "com.google.android.gms";
        String googleVideoPackage = "com.google.android.videos";
//        String googlePlayStorePackage = "com.android.vending";

        int gmsPackageStatus = StoreModeApplication.sContext.getPackageManager().getApplicationEnabledSetting(gmsPackage);
        int googleVideoPackageStatus = StoreModeApplication.sContext.getPackageManager().getApplicationEnabledSetting(googleVideoPackage);
//        int googlePlayStorePackageStatus = StoreModeApplication.sContext.getPackageManager().getApplicationEnabledSetting(googlePlayStorePackage);

        LogUtils.d(TAG, "modifyGoogleServiceSwitch() 3 is diable,1 is enable  gmsPackageStatus:" + gmsPackageStatus);
        LogUtils.d(TAG, "modifyGoogleServiceSwitch() 3 is diable,1 is enable  googleVideoPackageStatus:" + googleVideoPackageStatus);
//        LogUtils.d(TAG, "modifyGoogleServiceSwitch() 3 is diable,1 is enable  googlePlayStorePackageStatus:" + googlePlayStorePackageStatus);

        return gmsPackageStatus == 1 && googleVideoPackageStatus == 1;
    }

    public static Boolean isTvScanning() {
        boolean isTVScaning = PropertyUtils.getBoolean("sys.hisense.tv.scanning", false);
        LogUtils.d(TAG, "PropertyUtils.get [sys.hisense.tv.scanning]:" + isTVScaning);
        return isTVScaning;
    }

    /**
     * -1 is standard limit; 0 is no background progress;
     * cts test  remove
     *
     * @param limit
     */
    @Deprecated
    public static void modifyProcessLimit(int limit) {
        LogUtils.d(TAG, "modifyProcessLimit()  limit:" + limit);
        try {
            ActivityManager.getService().setProcessLimit(limit);
        } catch (Exception e) {
            LogUtils.d(TAG, "modifyProcessLimit()  ExceptionException:" + e.getMessage());
        }
    }

    public static AniDrawable getAniDrawable(AniDrawable mAniDrawable) {
        LogUtils.d(TAG, "getAniDrawable mAniDrawable= " + mAniDrawable);
        if (mAniDrawable == null) {
            Gson gson = new Gson();
            Root result = null;
            InputStream inputStream = null;
            AssetManager assetManager = StoreModeApplication.sContext.getResources().getAssets();
            try {
                if (ConstantConfig.EPOS_LAYOUT_POSITION == 0 || ConstantConfig.EPOS_LAYOUT_POSITION == 2) {
                    inputStream = assetManager.open("epos_verticaltest.json");

                } else if (ConstantConfig.EPOS_LAYOUT_POSITION == 1 || ConstantConfig.EPOS_LAYOUT_POSITION == 3) {
                    inputStream = assetManager.open("epos_horizontal.json");

                }
                result = gson.fromJson(new InputStreamReader(inputStream), Root.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            DrawHelper drawHelper = new DrawHelper(result);
            drawHelper.setDrawableCache(new AndroidTvDrawableCache());
            drawHelper.getDrawableCache().injectContext(StoreModeApplication.sContext);
            drawHelper.setDuration(result.getDuration());

            mAniDrawable = new AniDrawable(drawHelper);
            mAniDrawable.setAnimationTimeCallback(new AniDrawable.AnimationTimeCallback() {
                @Override
                public void onAnimationStart() {

                }

                @Override
                public void onAnimationEnd() {
                    SeriesUtils.autoTransformEposPosition();
                }
            });
        }

        return mAniDrawable;
    }
}
